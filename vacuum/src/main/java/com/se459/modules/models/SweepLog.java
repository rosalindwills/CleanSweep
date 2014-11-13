package com.se459.modules.models;

import com.se459.util.log.Log;

public class SweepLog {

	Log log;

	public SweepLog(Log log) {
		super();
		this.log = log;
		log.horizontalLine();
	}

	public void recordStatus(Vacuum vacuum) {
		StringBuilder sb = new StringBuilder();
		sb.append("Moved to (" + vacuum.getX() + ", " + vacuum.getY()
				+ ")  ");
		sb.append("Charge Units: " + vacuum.getChargeRemaining() + "  ");
		sb.append("Dirt Units: " + vacuum.getDirtUnits() + "  ");
		sb.append("Surface Type: " + vacuum.getCurrentSurface() + "  ");
		
		sb.append("Sensor Checks: " + vacuum.getNavigationLogic().sensorChecks() + "  ");

		if (vacuum.isInChargingPoint()) {
			sb.append("At Charging Point  ");
		} else {
			if (vacuum.isReturning()) {
				sb.append("Returning  ");
			} else {
				sb.append("Return Cost: " + vacuum.getReturnCost() + "  ");
			}
		}
		this.log.append(sb.toString());
	}

	public void start() {
		this.log.append("Sweep started!");
	}

	public void stop() {
		this.log.append("Sweep stopped!");
	}

	public void engageVacuum(Vacuum vacuum) {
		StringBuilder sb = new StringBuilder();
		sb.append("Vacuum Engaged at (" + vacuum.getX() + ", " + vacuum.getY()
				+ ")  ");
		sb.append("Cost: " + vacuum.getCurrentCell().getVacuumCost() + "  ");
		sb.append("Charge Units: " + vacuum.getChargeRemaining() + "  ");
		sb.append("Dirt Units: " + vacuum.getDirtUnits() + "  ");
		sb.append("Surface Type: " + vacuum.getCurrentSurface() + "  ");
		
		log.append(sb.toString());
	}
	
	public void done(){
		this.log.append("Sweep has finished cleaning all cells that it can reach!");
	}

}
