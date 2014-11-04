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
import com.se459.util.log.LogFactory;

public class NavigationLogic {

	private ICell currentPosition;
	private VacuumMemory memory;

	private List<ICell> currentTravelingPath = new ArrayList<ICell>();

	private int currentFloor = 1;

	private ISensor sensor;

	Log log;

	private double returnCost = 0;

	private List<ICell> returnPath = new ArrayList<ICell>();

	private boolean isReturning = false;

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

	public double getReturnCost() {
		return this.returnCost;
	}

	public ICell checkAndGetNext(double remaining) {

		if (this.isReturning) {
			return this.currentTravelingPath.remove(0);

		} else {

			if (!this.currentTravelingPath.isEmpty()) {
				ICell cell = this.currentTravelingPath.get(0);
				double nextMoveCost = (cell.getTraverseCost() + this.currentPosition
						.getTraverseCost()) / 2;
				List<ICell> destination = new ArrayList<ICell>();
				destination.add(sensor.getStartPoint(this.currentFloor));

				PathFinder pf = new PathFinder(this.memory.getAllKnownCells());
				List<ICell> returnPathFromNext = pf.findPath(cell, destination);
				double returnCostFromNext = pf
						.calculateCost(returnPathFromNext);

				if (remaining - nextMoveCost >= returnCostFromNext) {
					return this.currentTravelingPath.remove(0);
				} else {
					this.isReturning = true;
					this.currentTravelingPath = this.returnPath;
					return this.returnPath.remove(0);
				}

			} else {
				ICell posXCell = sensor.getPosXCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				boolean posX = sensor.getPosXPathType(currentFloor,
						currentPosition.getX(), currentPosition.getY()) == PathType.OPEN;

				ICell posYCell = sensor.getPosYCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				boolean posY = sensor.getPosYPathType(currentFloor,
						currentPosition.getX(), currentPosition.getY()) == PathType.OPEN;

				ICell negXCell = sensor.getNegXCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				boolean negX = sensor.getNegXPathType(currentFloor,
						currentPosition.getX(), currentPosition.getY()) == PathType.OPEN;

				ICell negYCell = sensor.getNegYCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				boolean negY = sensor.getNegYPathType(currentFloor,
						currentPosition.getX(), currentPosition.getY()) == PathType.OPEN;

				if ((!posX || !this.memory.ifUnfinished(posXCell))
						&& (!posY || !this.memory.ifUnfinished(posYCell))
						&& (!negX || !this.memory.ifUnfinished(negXCell))
						&& (!negY || !this.memory.ifUnfinished(negYCell))) {

					findPathToNextKnownCell();
					ICell cell = this.currentTravelingPath.remove(0);
					if (cell.equals(currentPosition)) {
						cell = this.currentTravelingPath.remove(0);
					}
					return cell;

				} else {

					if (posX && this.memory.ifUnfinished(posXCell)) {
						double nextMoveCost = (posXCell.getTraverseCost() + this.currentPosition
								.getTraverseCost()) / 2;

						List<ICell> destination = new ArrayList<ICell>();
						destination
								.add(sensor.getStartPoint(this.currentFloor));

						PathFinder pf = new PathFinder(
								this.memory.getAllKnownCells());
						List<ICell> returnPathFromNext = pf.findPath(posXCell,
								destination);
						double returnCostFromNext = pf
								.calculateCost(returnPathFromNext);

						if (remaining - nextMoveCost >= returnCostFromNext) {
							return posXCell;
						}
					}
					if (negX && this.memory.ifUnfinished(negXCell)) {
						double nextMoveCost = (negXCell.getTraverseCost() + this.currentPosition
								.getTraverseCost()) / 2;

						List<ICell> destination = new ArrayList<ICell>();
						destination
								.add(sensor.getStartPoint(this.currentFloor));

						PathFinder pf = new PathFinder(
								this.memory.getAllKnownCells());
						List<ICell> returnPathFromNext = pf.findPath(negXCell,
								destination);
						double returnCostFromNext = pf
								.calculateCost(returnPathFromNext);

						if (remaining - nextMoveCost >= returnCostFromNext) {
							return negXCell;
						}
					}

					if (posY && this.memory.ifUnfinished(posYCell)) {
						double nextMoveCost = (posYCell.getTraverseCost() + this.currentPosition
								.getTraverseCost()) / 2;

						List<ICell> destination = new ArrayList<ICell>();
						destination
								.add(sensor.getStartPoint(this.currentFloor));

						PathFinder pf = new PathFinder(
								this.memory.getAllKnownCells());
						List<ICell> returnPathFromNext = pf.findPath(posYCell,
								destination);
						double returnCostFromNext = pf
								.calculateCost(returnPathFromNext);

						if (remaining - nextMoveCost >= returnCostFromNext) {
							return posYCell;
						}
					}

					if (negY && this.memory.ifUnfinished(negYCell)) {
						double nextMoveCost = (negYCell.getTraverseCost() + this.currentPosition
								.getTraverseCost()) / 2;

						List<ICell> destination = new ArrayList<ICell>();
						destination
								.add(sensor.getStartPoint(this.currentFloor));

						PathFinder pf = new PathFinder(
								this.memory.getAllKnownCells());
						List<ICell> returnPathFromNext = pf.findPath(negYCell,
								destination);
						double returnCostFromNext = pf
								.calculateCost(returnPathFromNext);

						if (remaining - nextMoveCost >= returnCostFromNext) {
							return negYCell;
						}
					}

					this.isReturning = true;
					this.currentTravelingPath = this.returnPath;
					return this.returnPath.remove(0);

				}

			}

		}

	}

	public boolean checkIfCanDoVacuum(double remaining, ICell cell) {
		double nextVacuumCost = cell.getVacuumCost();
		if (remaining - nextVacuumCost < this.returnCost) {
			this.currentTravelingPath = this.returnPath;
			this.isReturning = true;
			return false;
		} else {
			return true;
		}

	}

	public double moveTo(ICell destination) {

		if (this.isReturning) {
			destination.setTraversed();
			double batteryUnitDrain = (this.currentPosition.getTraverseCost() + destination
					.getTraverseCost()) / 2;
			this.currentPosition = destination;

			return batteryUnitDrain;

		} else {
			destination.setTraversed();

			double batteryUnitDrain;
			if (this.currentPosition.equals(destination)) {
				batteryUnitDrain = 0;
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
				calculateReturnPath();
			}

			return batteryUnitDrain;
		}

	}

	private void findPathToNextKnownCell() {

		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());

		this.currentTravelingPath = pf.findPath(this.currentPosition,
				this.memory.getAllKnownButNotTraveldCells());
		this.currentTravelingPath.remove(0);
	}

	public void forceToReturn() {

	}

	public void calculateReturnPath() {
		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());
		List<ICell> destination = new ArrayList<ICell>();
		destination.add(sensor.getStartPoint(this.currentFloor));
		this.returnPath = pf.findPath(this.currentPosition, destination);
		this.returnCost = pf.calculateCost(this.returnPath);

	}

	public void reset() {
		this.isReturning = false;
	}

}
