package com.se459.modules.models;

import java.util.ArrayList;
import java.util.List;

import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

public class Vacuum implements Runnable {

	public static long delay = 2000;

	private ICell current;
	private ICell next;

	private VacuumMemory memory;
	private NavigationLogic navLogic;
	private ISensor sensor;
	private int currentFloor;

	public volatile boolean on = false;

	Log log = LogFactory.newFileLog();
	Log scnLog = LogFactory.newScreenLog();

	private int dirtUnits;
	final double dirtCapacity = 50;

	double chargeRemaining;
	final double chargeCapacity = 100;


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
		this.sensor = sensorSimulator;
		this.currentFloor = floor;

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
		current = sensor.getStartPoint(this.currentFloor);
		memory.addNewCell(current);
		navLogic.moveTo(current);
		Sweep(current);
		this.next = null;
		while (on) {
			if (next != null) {
				double batteryUnitDrain = this.navLogic.moveTo(this.next);
				this.chargeRemaining -= batteryUnitDrain;
				sleep();
				this.current = this.navLogic.readCurrentCell();
				if(this.current.equals(this.sensor.getStartPoint(this.currentFloor))){
					this.chargeRemaining = this.chargeCapacity;
				}
				Sweep(current);
				memory.output();
			}
			this.next = this.navLogic.findNext();
			this.navLogic.checkBattery(this.chargeRemaining);
		}
	}

	private void Sweep(ICell cell) {
		if (dirtUnits < dirtCapacity) {

			while (!cell.isClean()) {
				cell.cleanCell();
				this.chargeRemaining -= cell.getVacuumCost();
				sleep();
			}

			memory.becomeFinished(current);

			if (cell.getSurfaceType() == SurfaceType.BAREFLOOR) {
				// chargeRemaining -= 1;
			} else if (cell.getSurfaceType() == SurfaceType.LOWPILE) {
				// chargeRemaining -= 2;
			} else if (cell.getSurfaceType() == SurfaceType.HIGHPILE) {
				// chargeRemaining -= 3;
			}
		}
	}

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

	public double getChargeRemaining() {
		return chargeRemaining;
	}

	private void sleep() {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public VacuumMemory getMemory() {
		return this.memory;
	}
	
	public NavigationLogic getNavigationLogic(){
		return this.navLogic;
	}
}
