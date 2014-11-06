package com.se459.modules.models;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

public class Vacuum implements Runnable {

	private Observer observer;

	public static long delay = 75;

	private ICell current;
	private ICell next;

	private VacuumMemory memory;
	private NavigationLogic navLogic;
	private ISensor sensor;
	private int currentFloor;

	public volatile boolean on = false;

	Log log = LogFactory.newFileLog();
	Log scnLog = LogFactory.newScreenLog();

	private int dirtUnits = 0;
	final double dirtCapacity = 20;

	double chargeRemaining;
	final double chargeCapacity = 64;

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
		navLogic = new NavigationLogic(sensorSimulator, floor, memory);

		on = true;

		current = sensor.getStartPoint(this.currentFloor);
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
		memory.addNewCell(current);
		navLogic.moveTo(current);
		this.next = current;
		this.observer.update();
		sleep();
		Sweep(current);

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
					if(this.dirtUnits == this.dirtCapacity){
						this.observer.sendNotification("Empty me!");
						this.dirtUnits = 0;
					}
				}
				this.observer.update();
				sleep();
				Sweep(current);
				memory.output();
			}
			this.next = this.navLogic.checkAndGetNext(this.chargeRemaining);
			// when navlgoic returns a null, no next position to go, all done!
			if (next == null) {
				break;
			}
			this.observer.update();
			sleep();
		}

		this.observer.sendNotification("Done");
	}

	private void Sweep(ICell cell) {
		while (!cell.isClean()) {
			if (this.navLogic.checkIfCanDoVacuum(chargeRemaining, cell)) {
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

	public NavigationLogic getNavigationLogic() {
		return this.navLogic;
	}

	public void registerObserver(Observer ob) {
		this.observer = ob;
	}
}
