package com.se459.modules.models;

import java.util.ArrayList;
import java.util.List;

import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.sensor.models.SensorSimulator;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

public class Vacuum implements Runnable {

	public static long delay = 300;

	private ICell current;
	private ICell next;

	private VacuumMemory memory;
	private NavigationLogic navLogic;
        private SurfaceType previousSurfaceType = SurfaceType.BAREFLOOR;

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
	public List<ICell> returnPath = new ArrayList<ICell>();
	// the thread the vacuum is running in
	Thread thread;

	static public Vacuum getInstance(ISensor sensor, int floor, int xPos,
			int yPos) {
		return new Vacuum(sensor, floor, xPos, yPos);
	}

	private Vacuum(ISensor sensorSimulator, int floor, int xPosition,
			int yPosition) {
		chargeRemaining = chargeCapacity;
		dirtUnits = 0;

		scnLog.append("In vacuum constructor");

		memory = new VacuumMemory();
		navLogic = new NavigationLogic(sensorSimulator, floor, memory, scnLog);
	}

	public void Start() {
		on = true;

		thread = new Thread(this);
		thread.start();

		scnLog.append("Start Vacuum...");
		log.append("Started the vacuum");
	}

	public void Stop() {
		on = false;

		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		scnLog.append("Stop Vacuum...");
		log.append("Stopped the vacuum");
	}

	public void run() {
		memory.output();
		this.current = navLogic.readCurrentCell();
		memory.addNewCell(current);
		this.navLogic.moveTo(current);
                DecreaseChargeBySurfaceTypeAfterMove(current);
		Sweep(current);
		memory.output();
		this.next = null;
		while (on) {
			if (next != null) {
				this.navLogic.moveTo(this.next);
				sleep();
				this.current = this.navLogic.readCurrentCell();
                                DecreaseChargeBySurfaceTypeAfterMove(current);
				Sweep(current);
				memory.output();
			}

			this.next = this.navLogic.findNext();

			
		}
	}

	private void travelAlonePath() {

	}

	private boolean CheckIfFinishedCleaning() {
		// @TODO add logic to check this
		if (false) {
			scnLog.append("Finished cleaning.");
			return true;
		}
		return false;
	}

	// private boolean CheckIfOutOfPower() {
	//
	// ICell currentCell = sim.readCell(1, navLogic.currentPosition.x,
	// navLogic.currentPosition.y);
	//
	// List<ICell> cells = new ArrayList<ICell>(this.memory.knownCells);
	// if(!cells.contains(currentCell)){
	// cells.add(currentCell);
	// }
	//
	// PathFinder finder = new PathFinder(cells);
	//
	// List<List<ICell>> paths = finder.findAllPath(currentCell.getX(),
	// currentCell.getY(),
	// this.memory.FindChargingCell().getX(),this.memory.FindChargingCell().getY());
	// this.currentReturnPathNum = paths.size();
	//
	// if (paths.size() == 0){
	// return false;
	// }
	//
	// log.append("Paths found: " +paths.size());
	//
	// int minimumChargeCost = Integer.MAX_VALUE;
	// int minimumChargeCostPathNum = 0;
	//
	// for(int i =0; i < paths.size(); i++){
	// int pathChargeCost = 0;
	// List<ICell> path = paths.get(i);
	// for (ICell cell : path){
	// if(!(cell.getX() == navLogic.currentPosition.x && cell.getY() ==
	// navLogic.currentPosition.y)){
	// if (cell.getSurfaceType() == SurfaceType.BAREFLOOR) {
	// pathChargeCost += 1;
	// } else if (cell.getSurfaceType() == SurfaceType.LOWPILE) {
	// pathChargeCost += 2;
	// } else if (cell.getSurfaceType() == SurfaceType.HIGHPILE) {
	// pathChargeCost += 3;
	// }
	// }
	//
	// if (pathChargeCost >= minimumChargeCost){
	// break;
	// }
	// }
	// if (pathChargeCost < minimumChargeCost){
	// minimumChargeCost = pathChargeCost;
	// minimumChargeCostPathNum = i;
	// }
	// }
	//
	// returnCost = minimumChargeCost;
	//
	// /*
	// * THIS IS THE RETURN PATH
	// *
	// */
	// returnPath = paths.get(minimumChargeCostPathNum);
	//
	// return this.chargeRemaining <= minimumChargeCost;
	//
	//
	// }
	//
	// private boolean CheckIfFullOfDirt() {
	// if (dirtUnits >= dirtCapacity) {
	// scnLog.append("Dirt capacity reached." + getStatus());
	// return true;
	// }
	// return false;
	// }
	//
	// private void processReturnToChargingStation() {
	// scnLog.append("Recharging to full capacity." + getStatus());
	// chargeRemaining = chargeCapacity;
	// if (dirtUnits >= dirtCapacity) {
	// // scnLog.append("Press any key to empty dirt.");
	// //try
	// {
	// //System.in.read();
	// EmptyDirt();
	// Charge();
	// } // catch (Exception e) {}
	// }
	// }
	//
	// private void EmptyDirt()
	// {
	// scnLog.append("Emptying dirt.");
	// dirtUnits = 0;
	// }
	//
	private void Sweep(ICell cell) {
		if (dirtUnits < dirtCapacity) {


			while (cell.getDirtUnits() != 0 && chargeRemaining > 0) {
				cell.cleanCell();
                                DecreaseChargeRemainingBySurfaceTypeAfterClean(cell);
				sleep();
			}
			
			memory.becomeFinished(current);

		}
	}

        private void DecreaseChargeRemainingBySurfaceTypeAfterClean(ICell cell){
                 if (cell.getSurfaceType() == SurfaceType.BAREFLOOR){
                     if (chargeRemaining >= SurfaceType.BAREFLOOR.getValue()){
                         chargeRemaining -= SurfaceType.BAREFLOOR.getValue();     
                     }
                     else {
                         chargeRemaining = 0;
                     }
                 }
                 else if (cell.getSurfaceType() == SurfaceType.LOWPILE){
                     if (chargeRemaining >= SurfaceType.LOWPILE.getValue()){
                         chargeRemaining -= SurfaceType.LOWPILE.getValue();     
                     }
                     else {
                         chargeRemaining = 0;
                     }
                 }
                 else if (cell.getSurfaceType() == SurfaceType.HIGHPILE){
                     if (chargeRemaining >= SurfaceType.HIGHPILE.getValue()){
                         chargeRemaining -= SurfaceType.HIGHPILE.getValue();     
                     }
                     else {
                         chargeRemaining = 0;
                     }
                 } 
        }  

        private void DecreaseChargeBySurfaceTypeAfterMove(ICell cell){
                 int chargeUnitsToDecrease = (previousSurfaceType.getValue() + cell.getSurfaceType().getValue())/2;

                 if (chargeRemaining >= chargeUnitsToDecrease){
                     chargeRemaining -= chargeUnitsToDecrease;
                 }
                 else {
                     chargeRemaining = 0;
                 }

                 previousSurfaceType = cell.getSurfaceType();
        }

	//
	// private void Charge()
	// {
	// chargeRemaining = chargeCapacity;
	// }

	// pick the next destination for the vacuum to go to
	// private void SetNextDestination()
	// {
	// if(CheckIfOutOfPower())
	// {
	// navLogic.SetDestinationToBaseStation();
	// }
	// else if(dirtUnits == dirtCapacity)
	// {
	// navLogic.SetDestinationToBaseStation();
	// }
	// else
	// {
	// navLogic.SetPathToDirtyOrUnknown();
	// }
	// }

	public int GetX() {
		return this.current.getX();
	}

	public int GetY() {
		return this.current.getY();
	}

	public int GetDestinationX() {
		return this.next.getX();
	}

	public int GetDestinationY() {
		return this.next.getY();
	}

	private String getStatus() {
		return " current capacity: " + dirtUnits + "/" + dirtCapacity
				+ ", current charge: " + chargeRemaining + "/" + chargeCapacity;
	}

	public int getDirtUnits() {
		return dirtUnits;
	}

	public int getChargeRemaining() {
		return chargeRemaining;
	}
	
	private void sleep(){
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
