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
		this.currentPosition = sensor.getStartPoint(this.currentFloor);
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

	public double moveTo(ICell destination) {
	
		destination.setTraversed();

		double batteryUnitDrain;
		if (this.currentPosition.equals(destination)) {
			batteryUnitDrain =  0;
		}

		batteryUnitDrain = (this.currentPosition.getTraverseCost() + destination
				.getTraverseCost()) / 2;

		this.currentPosition = destination;

		if (this.sensor.getPosXPathType(currentFloor, destination.getX(),
				destination.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getPosXCell(currentFloor,
					destination.getX(), destination.getY()));

		}
		if (this.sensor.getPosYPathType(currentFloor, destination.getX(),
				destination.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getPosYCell(currentFloor,
					destination.getX(), destination.getY()));

		}
		if (this.sensor.getNegXPathType(currentFloor, destination.getX(),
				destination.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getNegXCell(currentFloor,
					destination.getX(), destination.getY()));
		}
		if (this.sensor.getNegYPathType(currentFloor, destination.getX(),
				destination.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getNegYCell(currentFloor,
					destination.getX(), destination.getY()));
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
