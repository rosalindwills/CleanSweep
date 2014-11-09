package com.se459.modules.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

public class Vacuum implements Runnable {

	private Observer observer;

	final static long delay = 50;
	final static double chargeCapacity = 50;
	final static double dirtCapacity = 50;
	private int currentFloor;

	private ICell current;
	private ICell next;
	private volatile boolean on = false;
	private int dirtUnits = 0;
	double chargeRemaining;

	private VacuumMemory memory;
	private NavigationLogic navLogic;
	private ISensor sensor;

	Log log = LogFactory.newFileLog();
	Log scnLog = LogFactory.newScreenLog();

	static public Vacuum getInstance(ISensor sensor, int floor, int xPos,
			int yPos) {
		return new Vacuum(sensor, floor);
	}

	private Vacuum(ISensor sensorSimulator, int floor) {
		chargeRemaining = chargeCapacity;
		dirtUnits = 0;
		this.sensor = sensorSimulator;
		this.currentFloor = floor;
		memory = new VacuumMemory();
		navLogic = new NavigationLogic(sensorSimulator, memory);
		on = true;
	
		current = sensor.getStartPoint(this.currentFloor);
		
		scnLog.append("In vacuum constructor");
	}

	public void stop() {
		on = false;

		scnLog.append("Stop Vacuum...");
		log.append("Stopped the vacuum");
	}

	public void run() {
		memory.addNewCell(current);
		navLogic.moveTo(current);
		this.next = current;
		this.observer.update();
		sleep();
		sweep(current);

		while (on) {
			if (!next.equals(current)) {
				double batteryUnitDrain = this.navLogic.moveTo(this.next);
				this.chargeRemaining -= batteryUnitDrain;
				this.current = this.navLogic.readCurrentCell();
				if (this.current.equals(this.sensor
						.getStartPoint(this.currentFloor))) {
					this.observer.update();
					this.observer.sendNotification("Returned! Charging Left: "
							+ this.chargeRemaining);
					this.chargeRemaining = this.chargeCapacity;
					this.navLogic.reset();
					if (this.dirtUnits == this.dirtCapacity) {
						this.observer.sendNotification("Empty me!");
						this.dirtUnits = 0;
					}
				}
				this.observer.update();
				sleep();
				sweep(current);
			}
			this.next = this.navLogic.checkAndGetNext(this.chargeRemaining);
			// when navlgoic returns a null, no next position to go, all done!
			if (next == null) {
				stop();
				break;
			}
			this.observer.update();
			sleep();
		}

		this.observer.sendNotification("Done");
	}

	private void sweep(ICell cell) {
		while (!cell.isClean()) {
			// check if the sweep has enough charge to do engage vacuum one more time
			if (this.navLogic.checkIfCanDoVacuum(chargeRemaining, cell)) {
				// check if have met the dirt capacity
				if (this.dirtUnits < this.dirtCapacity) {
					cell.cleanCell();
					this.chargeRemaining -= cell.getVacuumCost();
					this.dirtUnits += 1;
					this.observer.update();
					sleep();
				} else {
					this.navLogic.returnNow();
					return;
				}
			} else {
				return;
			}
		}
		memory.becomeFinished(current);
	}

	public int getX() {
		return this.current.getX();
	}

	public int getY() {
		return this.current.getY();
	}

	public int getDestinationX() {
		return this.next.getX();
	}

	public int getDestinationY() {
		return this.next.getY();
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
			throw new RuntimeException(e);
		}
	}

	public VacuumMemory getMemory() {
		return this.memory;
	}

	public NavigationLogic getNavigationLogic() {
		return this.navLogic;
	}

	public void registerObserver(Observer ob) {
		this.observer = ob;
	}
	
	public boolean getIsOn() {
		return this.on;
	}
}
