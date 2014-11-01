package com.se459.modules.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;

public class NavigationLogic {

	private ICell currentPosition;
	private VacuumMemory memory;

	private List<ICell> currentTravelingPath = new ArrayList<ICell>();

	private int currentFloor = 1;

	private ISensor sensor;

	Log log;

	private int returnCost = 0;

	private List<ICell> returnPath = new ArrayList<ICell>();

	private boolean isReturning;

	public ICell readCurrentCell(int floor) {
		return currentPosition;
	}

	public NavigationLogic(ISensor sensor, int floor,
			VacuumMemory vacuumMemory, Log log) {
		currentPosition = sensor.getStartPoint(floor);
		this.sensor = sensor;
		this.log = log;
		this.memory = vacuumMemory;
	}

	public ICell readCurrentCell() {
		return currentPosition;
	}

	public List<ICell> getReturnPath() {
		return this.returnPath;
	}

	public int getReturnCost() {
		return this.returnCost;
	}

	public ICell findNext() {
		if (this.currentTravelingPath.size() == 0) {
			// check if there is a known but unfinished cell around
			if (sensor.getPosXPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getPosXCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifUnfinished(c)) {
					return c;
				}

			}
			if (sensor.getNegXPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getNegXCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifUnfinished(c)) {
					return c;
				}

			}
			if (sensor.getPosYPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getPosYCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifUnfinished(c)) {
					return c;
				}
			}
			if (sensor.getNegYPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getNegYCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifUnfinished(c)) {
					return c;
				}
			}

			findPathToNextKnownCell();

			return this.currentTravelingPath.get(0);

		} else {
			return this.currentTravelingPath.remove(0);

		}

	}

	public void checkBattery(double remaining) {

		// int nextMoveCost = 0;
		//
		// switch (this.currentPosition.getSurfaceType()) {
		// case BAREFLOOR:
		// nextMoveCost = 1;
		// case LOWPILE:
		// nextMoveCost = 2;
		// case HIGHPILE:
		// nextMoveCost = 3;
		// }
		//
		// switch (this.findNext().getSurfaceType()) {
		// case BAREFLOOR:
		// nextMoveCost = 1;
		// case LOWPILE:
		// nextMoveCost = 2;
		// case HIGHPILE:
		// nextMoveCost = 3;
		// }
		//
		// // if (calculateReturnCost(this.currentPosition)
		// // > remaining) {
		// // this.currentTravelingPath = this.returnPath;
		// // }

	}

	public double moveTo(ICell next) {
		double batteryUnitDrain = 0;
		
		 switch (this.currentPosition.getSurfaceType()) {
		 case BAREFLOOR:
		 batteryUnitDrain += 1;
		 break;
		 case LOWPILE:
		 batteryUnitDrain += 2;
		 break;
		 case HIGHPILE:
		 batteryUnitDrain += 3;
		 break;
		 }
		
		 switch (next.getSurfaceType()) {
		 case BAREFLOOR:
		 batteryUnitDrain += 1;
		 break;
		 case LOWPILE:
		 batteryUnitDrain += 2;
		 break;
		 case HIGHPILE:
		 batteryUnitDrain += 3;
		 break;
		 }
		
		 batteryUnitDrain = batteryUnitDrain / 2;

		this.currentPosition = next;

		if (this.sensor.getPosXPathType(currentFloor, next.getX(), next.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getPosXCell(currentFloor,
					next.getX(), next.getY()));

		}
		if (this.sensor.getPosYPathType(currentFloor, next.getX(), next.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getPosYCell(currentFloor,
					next.getX(), next.getY()));

		}
		if (this.sensor.getNegXPathType(currentFloor, next.getX(), next.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getNegXCell(currentFloor,
					next.getX(), next.getY()));
		}
		if (this.sensor.getNegYPathType(currentFloor, next.getX(), next.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getNegYCell(currentFloor,
					next.getX(), next.getY()));
		}
		if (!this.isReturning) {
			// calculateReturnCost();
		}

		return batteryUnitDrain;
	}

	private void findPathToNextKnownCell() {

		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());

		this.currentTravelingPath = pf.findPath(this.currentPosition,
				this.memory.getAllKnownButNotTraveldCells());
		this.currentTravelingPath.remove(0);
	}

}
