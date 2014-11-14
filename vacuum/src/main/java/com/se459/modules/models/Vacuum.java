package com.se459.modules.models;

import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Config;
import com.se459.util.log.LogFactory;

public class Vacuum implements Runnable {

	private Observer observer;

	private double chargeCapacity = Config.getInstance().chargeCapacity;
	private double dirtCapacity = Config.getInstance().dirtCapacity;
	private int currentFloor;

	private ICell current;
	private ICell next;
	private volatile boolean on = false;
	private int dirtUnits = 0;
	double chargeRemaining;

	private VacuumMemory memory;
	private NavigationLogic navLogic;
	private ISensor sensor;
	
	private Thread runningThread;

	SweepLog log = new SweepLog(LogFactory.newFileLog());

	static public Vacuum getInstance(ISensor sensor, int floor) {
		return new Vacuum(sensor, floor);
	}

	private Vacuum(ISensor sensorSimulator, int floor) {
		chargeRemaining = chargeCapacity;
		dirtUnits = 0;
		this.sensor = sensorSimulator;
		this.currentFloor = floor;
		memory = new VacuumMemory();
		navLogic = new NavigationLogic(sensorSimulator, memory);
	
		current = sensor.getStartPoint(this.currentFloor);
	}
	
	public void start()
	{
		on = true;
		
		runningThread = new Thread(this);
		runningThread.start();
	}
	
	public void stop() {
		on = false;
		log.stop();
		
		try 
		{
			runningThread.join();
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void FinishedCleaning(){
		on = false;
		
		if(navLogic.cleanedEntireFloor)
		{
			sendNotification("Finished cleaning the entire floor.");
		}
		else
		{
			sendNotification("Finished cleaning, did not have enough energy to clean the entire floor.");
		}
		log.done();
	}

	public void run() {
		log.start();
		memory.addNewCell(current);
		navLogic.moveTo(current);
		this.next = current;
		log.recordStatus(this);
		updateObserver();
		sleep();
		sweep(current);

		while (on) {
			if (!next.equals(current)) {
				double batteryUnitDrain = this.navLogic.moveTo(this.next);
				this.chargeRemaining -= batteryUnitDrain;	
				this.current = this.navLogic.readCurrentCell();		
				log.recordStatus(this);
				if (isInChargingPoint()) {
					updateObserver();
					sendNotification("Returned! Charging Left: " + this.chargeRemaining);
					this.chargeRemaining = this.chargeCapacity;
					this.navLogic.reset();
					if (this.dirtUnits == this.dirtCapacity) {
						sendNotification("Empty me!");
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
				FinishedCleaning();
				break;
			}
			updateObserver();
			
			sleep();
		}

		sendNotification("Done");
	}

	private void sweep(ICell cell) {
		while (!cell.isClean()) {
			// check if the sweep has enough charge to do vacuum one more time
			if (this.navLogic.checkIfCanDoVacuum(chargeRemaining, cell)) {
				// check if have met the dirt capacity
				if (this.dirtUnits < this.dirtCapacity) {
					cell.cleanCell();
					this.chargeRemaining -= cell.getVacuumCost();
					this.dirtUnits += 1;
					updateObserver();
					log.engageVacuum(this);
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
			Thread.sleep(Config.getInstance().delay);
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
	
	public boolean isOn() {
		return this.on;
	}
	
	public boolean isReturning() {
		return this.navLogic.getIsReturning();
	}
	
	public SurfaceType getCurrentSurface(){
		return this.current.getSurfaceType();
	}
	
	public double getReturnCost(){
		return this.navLogic.getReturnCost();
	}
	
	public boolean isInChargingPoint(){
		return this.current.equals(this.sensor
				.getStartPoint(this.currentFloor));
	}
	
	public ICell getCurrentCell(){
		return this.current;
	}
	
	public ICell getNextCell(){
		return this.next;
	}
	
	private void updateObserver()
	{
		if(null != observer)
		{
			observer.update();
		}
	}
	
	private void sendNotification(String message)
	{
		if(null != observer)
		{
			observer.sendNotification(message);
		}
	}
}
