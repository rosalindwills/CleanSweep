package com.se459.modules.models;

import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

import java.util.ArrayList;

public class Vacuum implements Runnable {
	private ISensor sim;
	
    private ArrayList<ICell> knownCells = new ArrayList<ICell>();
    private ArrayList<ICell> knownDirtyCells = new ArrayList<ICell>();
    private ArrayList<Obstacle> knownObstacles = new ArrayList<Obstacle>();
        
	public volatile boolean on;
	
	Log log = LogFactory.newFileLog();
    Log scnLog = LogFactory.newScreenLog();
	
	private int x, y;
	private int destinationX, destinationY;
	
	private int dirtUnits;
	final int dirtCapacity = 50;
	
	int chargeRemaining;
	final int chargeCapacity = 50;
	
	// the thread the vacuum is running in
	Thread thread;

    static public Vacuum getInstance(ISensor sensor,int xPos, int yPos)
    { 
          return new Vacuum(sensor,xPos,yPos);
    }
	
	private Vacuum(ISensor sensorSimulator, int xPosition, int yPosition)
	{
		x = xPosition;
		y = yPosition;
				
		chargeRemaining = chargeCapacity;
		dirtUnits = 0;

        scnLog.append("In vacuum constructor");
		sim = sensorSimulator;
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
			ICell currentCell = sim.readCell(1, x, y);
			if (currentCell.getIsChargingStation()) {
				processReturnToChargingStation();
			}

			if(!knownCells.contains(currentCell))
			{
				knownCells.add(currentCell);
			}
			
			if(currentCell.getDirtUnits() > 0 && !CheckIfFullOfDirt())
			{
				Sweep(currentCell);
			}
			else if(x == destinationX && y == destinationY)
			{
				SetNextDestination();
			}
			else
			{
				MoveTowardsDestination();
			}
			scnLog.append("Next destination: " + destinationX + ", " + destinationY);
			if (CheckIfFinishedCleaning() || CheckIfOutOfPower() || CheckIfFullOfDirt()) {
				returnToChargingStation();
			}
			
			
            try 
            {
				Thread.sleep(250);
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
		// do we need to return to the charger? right now this just sees if we are below 50% charge, could be smarter about route planning in the future
		if(chargeRemaining < (chargeCapacity / 2))
		{
			scnLog.append("Charge level necessitates return to base." + getStatus());
			return true;
		}
		return false;
	}
	
	private boolean CheckIfFullOfDirt() {
		if (dirtUnits >= dirtCapacity) {
			scnLog.append("Dirt capacity reached." + getStatus());
			return true;
		}
		return false;
	}
	
	private void returnToChargingStation() {
		ICell chargingCell = FindChargingCell();
		destinationX = chargingCell.getX();
		destinationY = chargingCell.getY();
	}
	
	private void processReturnToChargingStation() {
		scnLog.append("Recharging to full capacity." + getStatus());
		chargeRemaining = chargeCapacity;
		if (dirtUnits >= dirtCapacity) {
			scnLog.append("Press any key to empty dirt.");
	        try
	        {
	            System.in.read();
	            scnLog.append("Emptying dirt.");
	            dirtUnits = 0;
	        } catch (Exception e) {}
		}
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
	
	// pick the next destination for the vacuum to go to
	private void SetNextDestination()
	{
		if(knownDirtyCells.size() > 0)
		{
			// find the nearest dirty cell
			int closestDistance = Integer.MAX_VALUE;
			ICell closestCell = null;
			
			for(int i = 0; i < knownDirtyCells.size()	; ++i)
			{
				int distance = GetDistanceToCell(knownDirtyCells.get(i));
				if(closestDistance > distance)
				{
					closestCell = knownDirtyCells.get(i);
					closestDistance = distance;
				}
			}

			destinationX = closestCell.getX();
			destinationY = closestCell.getY();
		}
		else
		{
			// find the nearest cell that we don't know about
			int offset = 1;
			
			while(true)
			{
				int offset2 = 0;
				
				while(offset2 <= offset)
				{
					if(null == GetKnownCellAtPoint(x - offset, y - offset2) && null == GetKnownObstacleAtPoint(x - offset, y - offset2))
					{
						destinationX = x - offset;
						destinationY = y - offset2;
						return;
					}
					else if(null == GetKnownCellAtPoint(x - offset, y + offset2) && null == GetKnownObstacleAtPoint(x - offset, y + offset2))
					{
						destinationX = x - offset;
						destinationY = y + offset2;
						return;
					}
					else if(null == GetKnownCellAtPoint(x + offset, y - offset2) && null == GetKnownObstacleAtPoint(x + offset, y - offset2))
					{
						destinationX = x + offset;
						destinationY = y - offset2;
						return;
					}
					else if(null == GetKnownCellAtPoint(x + offset, y + offset2) && null == GetKnownObstacleAtPoint(x + offset, y + offset2))
					{
						destinationX = x + offset;
						destinationY = y + offset2;
						return;
					}
					else if(null == GetKnownCellAtPoint(x - offset2, y - offset) && null == GetKnownObstacleAtPoint(x - offset2, y - offset))
					{
						destinationX = x - offset2;
						destinationY = y - offset;
						return;
					}
					else if(null == GetKnownCellAtPoint(x + offset2, y - offset) && null == GetKnownObstacleAtPoint(x + offset2, y - offset))
					{
						destinationX = x + offset2;
						destinationY = y - offset;
						return;
					}
					else if(null == GetKnownCellAtPoint(x - offset2, y + offset) && null == GetKnownObstacleAtPoint(x - offset2, y + offset))
					{
						destinationX = x - offset2;
						destinationY = y + offset;
						return;
					}
					else if(null == GetKnownCellAtPoint(x + offset2, y + offset) && null == GetKnownObstacleAtPoint(x + offset2, y + offset))
					{
						destinationX = x + offset2;
						destinationY = y + offset;
						return;
					}
					
					++offset2;
				}
			}
		}
	}
	
	private void SetNextDestination(ICell cell) {
		destinationX = cell.getX();
		destinationY = cell.getY();
	}
	
	private int GetDistanceToCell(ICell cell)
	{
		return GetDistanceToPoint(cell.getX(), cell.getY());
	}
	
	private int GetDistanceToPoint(int pointX, int pointY)
	{
		return Math.abs(x - pointX) + Math.abs(y - pointY);
	}
	
	private ICell GetKnownCellAtPoint(int x, int y)
	{
		for(int i = 0; i < knownCells.size(); ++i)
		{
			ICell cell = knownCells.get(i);
			if(cell.getX() == x && cell.getY() == y)
			{
				return cell;
			}
		}
		
		return null;
	}
	
	private Obstacle GetKnownObstacleAtPoint(int x, int y)
	{
		for(int i = 0; i < knownObstacles.size(); ++i)
		{
			Obstacle obstacle = knownObstacles.get(i);
			if(obstacle.GetX() == x && obstacle.GetY() == y)
			{
				return obstacle;
			}
		}
		
		return null;
	}
	
	private void MoveTowardsDestination()
	{
		int xDist = GetDestinationX() - x;
		int yDist = GetDestinationY() - y;
		
		int xDir = 0;
		int yDir = 0;
		
		if(Math.abs(xDist) > Math.abs(yDist))
		{
			if(xDist > 0)
			{
				xDir = 1;
			}
			else
			{
				xDir = -1;
			}
		}
		else
		{
			if(yDist > 0)
			{
				yDir = 1;
			}
			else
			{
				yDir = -1;
			}
		}		
		
		if(null == sim.readCell(1, x + xDir, y + yDir))
		{
			knownObstacles.add(new Obstacle(x + xDir, y + yDir));
			SetNextDestination();
		}
		else
		{
			x += xDir;
			y += yDir;
		}
	}
	
	private ICell FindChargingCell()
	{
		for(int i = 0; i < knownCells.size(); ++i)
		{
			if(knownCells.get(i).getIsChargingStation())
			{
				return knownCells.get(i);
			}
		}
		
		scnLog.append("Could not find charging station in list of known cells.");
		return null;
	}
	
	public int GetX()
	{
		return x;
	}
	
	public int GetY()
	{
		return y;
	}
	
	public int GetDestinationX()
	{
		return destinationX;
	}
	
	public int GetDestinationY()
	{
		return destinationY;
	}
	
	private String getStatus() {
		return " current capacity: " + dirtUnits + "/" + dirtCapacity + ", current charge: " + chargeRemaining + "/" + chargeCapacity;
	}
}
