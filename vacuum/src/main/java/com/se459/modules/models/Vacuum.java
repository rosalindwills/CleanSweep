package com.se459.modules.models;

import java.util.ArrayList;
import java.util.List;

import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

public class Vacuum implements Runnable {
	private ISensor sim;

	VacuumMemory memory;
	NavigationLogic navLogic;
	
	public volatile boolean on;
	
	Log log = LogFactory.newFileLog();
    Log scnLog = LogFactory.newScreenLog();

	private int dirtUnits;
	final int dirtCapacity = 50;
	
	int chargeRemaining;
	final int chargeCapacity = 50;
	
	// for testing, variables below should be removed finally.
	public int currentReturnPathNum = 0;
	public int returnCost = 0;
	// the thread the vacuum is running in
	Thread thread;

    static public Vacuum getInstance(ISensor sensor,int xPos, int yPos)
    { 
    	return new Vacuum(sensor,xPos,yPos);
    }
	
	private Vacuum(ISensor sensorSimulator, int xPosition, int yPosition)
	{
		chargeRemaining = chargeCapacity;
		dirtUnits = 0;

        scnLog.append("In vacuum constructor");
		sim = sensorSimulator;
		
		memory = new VacuumMemory();
		navLogic = new NavigationLogic(new Vector2(xPosition, yPosition), sim, memory, scnLog);
	}
	
	public void Start()
	{
		on = true;
		
		thread = new Thread(this);
		thread.start();

        scnLog.append("Start Vacuum...");
		log.append("Started the vacuum");
	}
	
	public void Stop()
	{
		on = false;
		
		try 
		{
			thread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}

        scnLog.append("Stop Vacuum...");
		log.append("Stopped the vacuum");
	}
	
	public void run()
	{
		while(on)
		{
			ICell currentCell = sim.readCell(1, navLogic.currentPosition.x, navLogic.currentPosition.y);
			memory.AddCell(currentCell);
			
			if (currentCell.getIsChargingStation() && chargeRemaining < chargeCapacity) 
			{
			//	log1.append("1");
				processReturnToChargingStation();
			}
			else if(currentCell.getDirtUnits() > 0 && !CheckIfFullOfDirt())
			{
			//	log1.append("2");
				Sweep(currentCell);
			}
			else if(null == navLogic.currentPath)
			{	
			//	log1.append("3");
				SetNextDestination();
			}
			else
			{
			//	log1.append("4");
				navLogic.MoveTowardsDestination();
			}
			
			//log1.append("@");
			boolean b = CheckIfOutOfPower();
			//log1.append("@");
					
			if (CheckIfFinishedCleaning() || b || CheckIfFullOfDirt()) {
				navLogic.SetDestinationToBaseStation();
			}			
			
            try 
            {
				Thread.sleep(300);
			} 
            catch (InterruptedException e) 
            {
				e.printStackTrace();
			}
		}
	}
	
	private boolean CheckIfFinishedCleaning()
	{
		//@TODO add logic to check this
		if (false) {
			scnLog.append("Finished cleaning.");
			return true;
		}
		return false;
	}
	
	private boolean CheckIfOutOfPower() {
		
		ICell currentCell = sim.readCell(1, navLogic.currentPosition.x, navLogic.currentPosition.y);
		
		List<ICell> cells = new ArrayList(this.memory.knownCells);
		if(!cells.contains(currentCell)){
			cells.add(currentCell);
		}
		
		PathFinder finder = new PathFinder(cells);
		
		List<List<ICell>> paths = finder.findAllPath(currentCell.getX(), currentCell.getY(), 
				this.memory.FindChargingCell().getX(),this.memory.FindChargingCell().getY());
		this.currentReturnPathNum = paths.size();
		
		if (paths.size() == 0){
			return false;
		}
		
		log.append("Paths found: " +paths.size());
		
		int minimumChargeCost = Integer.MAX_VALUE;
		int minimumChargeCostPathNum = 0;
		
		for(int i =0; i < paths.size(); i++){
			int pathChargeCost = 0;
			List<ICell> path = paths.get(i);
			for (ICell cell : path){
				if (cell.getSurfaceType() == SurfaceType.BAREFLOOR) {
					pathChargeCost += 1;
				} else if (cell.getSurfaceType() == SurfaceType.LOWPILE) {
					pathChargeCost += 2;
				} else if (cell.getSurfaceType() == SurfaceType.HIGHPILE) {
					pathChargeCost += 3;
				}
				
				if (pathChargeCost >= minimumChargeCost){
					break;
				}
			}
			if (pathChargeCost < minimumChargeCost){
				minimumChargeCost = pathChargeCost;
				minimumChargeCostPathNum = i;
			}
		}
		
		returnCost = minimumChargeCost;
		return this.chargeRemaining <= minimumChargeCost;

	
	}
	
	private boolean CheckIfFullOfDirt() {
		if (dirtUnits >= dirtCapacity) {
			scnLog.append("Dirt capacity reached." + getStatus());
			return true;
		}
		return false;
	}
	
	private void processReturnToChargingStation() {
		scnLog.append("Recharging to full capacity." + getStatus());
		chargeRemaining = chargeCapacity;
		if (dirtUnits >= dirtCapacity) {
			// scnLog.append("Press any key to empty dirt.");
	        //try
	        {
	            //System.in.read();
	            EmptyDirt();
	            Charge();
	        } // catch (Exception e) {}
		}
	}
	
	private void EmptyDirt()
	{
        scnLog.append("Emptying dirt.");
		dirtUnits = 0;
	}
	
	private void Sweep(ICell cell)
	{
		if(dirtUnits < dirtCapacity)
		{
			cell.cleanCell();
			++dirtUnits;

			if (cell.getSurfaceType() == SurfaceType.BAREFLOOR) {
				chargeRemaining -= 1;
			} else if (cell.getSurfaceType() == SurfaceType.LOWPILE) {
				chargeRemaining -= 2;
			} else if (cell.getSurfaceType() == SurfaceType.HIGHPILE) {
				chargeRemaining -= 3;
			}
			
			scnLog.append("Cleaned dirt from cell: " + cell.getX() + ", " + cell.getY() + getStatus());			
		}
	}
	
	private void Charge()
	{
		chargeRemaining = chargeCapacity;
	}
	
	// pick the next destination for the vacuum to go to
	private void SetNextDestination()
	{
		// do we need to return to the charger? right now this just sees if we are below 50% charge, could be smarter about route planning in the future
		if(CheckIfOutOfPower())
		{
			navLogic.SetDestinationToBaseStation();
		}
		else if(dirtUnits == dirtCapacity)
		{
			navLogic.SetDestinationToBaseStation();
		}
		else
		{
			navLogic.SetPathToDirtyOrUnknown();
		}
	}
	
	public int GetX()
	{
		return navLogic.currentPosition.x;
	}
	
	public int GetY()
	{
		return navLogic.currentPosition.y;
	}
	
	public int GetDestinationX()
	{
		return navLogic.navigationDestination.x;
	}
	
	public int GetDestinationY()
	{
		return navLogic.navigationDestination.y;
	}
	
	private String getStatus() {
		return " current capacity: " + dirtUnits + "/" + dirtCapacity + ", current charge: " + chargeRemaining + "/" + chargeCapacity;
	}

	public int getDirtUnits() {
		return dirtUnits;
	}

	public int getChargeRemaining() {
		return chargeRemaining;
	}
}
