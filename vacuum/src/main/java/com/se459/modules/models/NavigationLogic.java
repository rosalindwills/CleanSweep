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

	// path that sweep is currently traveling along
	// could be a return path, or a path to a unfinished cell, which is not
	// adjacent to the current cell
	// if this list is empty, it means no path to follow currently,
	// vacuum is exploring the cells around.
	private List<ICell> currentTravelingPath = new ArrayList<ICell>();
	// keep track of a return path
	private List<ICell> returnPath = new ArrayList<ICell>();
	private int currentFloor = 1;
	private ISensor sensor;
	private double returnCost = 0;
	private boolean isReturning = false;

	public boolean cleanedEntireFloor = false;

	public ICell readCurrentCell(int floor) {
		return currentPosition;
	}

	public NavigationLogic(ISensor sensor, VacuumMemory vacuumMemory) {
		this.currentPosition = sensor.getStartPoint(this.currentFloor);
		this.sensor = sensor;
		this.memory = vacuumMemory;
	}

	public ICell readCurrentCell() {
		return currentPosition;
	}

	public List<ICell> getReturnPath() {
		return this.returnPath;
	}

	public List<ICell> getCurrentPath() {
		return this.currentTravelingPath;
	}

	public double getReturnCost() {
		return this.returnCost;
	}

	public double getPathCost() {
		if (null != this.currentTravelingPath) {
			return PathFinder.calculateCost(this.currentTravelingPath);
		}

		return 0;
	}

	// if can take another move(return cost from next cell <= charge remaining -
	// next move cost ),
	// return the next cell that the sweep will move to.
	// if cannot take another move, calculate return path, set return path as
	// current traveling path, and
	// return the first cell in the return path.
	public ICell checkAndGetNext(double remaining) {

		// sweep is returning
		if (this.isReturning) {
			return this.currentTravelingPath.remove(0);

		} else {
			// vacuum finds out its done
			if (memory.getAllUnfinishedCells().isEmpty()) {
				// vacuum needs to return to the charging point
				if (!this.currentPosition.equals(this.sensor
						.getStartPoint(this.currentFloor))) {
					this.isReturning = true;
					this.currentTravelingPath = this.returnPath;
					this.currentTravelingPath.remove(0);
					return this.currentTravelingPath.remove(0);
				} else {
					// vacuum has already returned to the charging point.
					cleanedEntireFloor = true;

					return null;
				}
			}

			// vacuum is traveling along a path.
			// The path could be a return path to the charging point,
			// or a path to the next unfinished cell, which is not adjacent to
			// current cell.
			if (!this.currentTravelingPath.isEmpty()) {
				ICell cell = this.currentTravelingPath.get(0);
				double nextMoveCost = (cell.getTraverseCost() + this.currentPosition
						.getTraverseCost()) / 2;
				List<ICell> destination = new ArrayList<ICell>();
				destination.add(sensor.getStartPoint(this.currentFloor));

				PathFinder pf = new PathFinder(this.memory.getAllKnownCells());
				List<ICell> returnPathFromNext = pf.findPath(cell, destination);
				double returnCostFromNext = PathFinder
						.calculateCost(returnPathFromNext);

				if (remaining - nextMoveCost >= returnCostFromNext) {
					return this.currentTravelingPath.remove(0);
				} else {
					this.isReturning = true;
					this.currentTravelingPath = this.returnPath;
					this.currentTravelingPath.remove(0);
					return this.returnPath.remove(0);
				}

			} else {
				// no path to follow along, vacuum is exploring the cells
				// around.
				findPathToNextKnownCell();

				PathFinder pf = new PathFinder(this.memory.getAllKnownCells());

				List<ICell> baseStation = new ArrayList<ICell>();
				baseStation.add(sensor.getStartPoint(this.currentFloor));

				ICell destinationCell = this.currentTravelingPath
						.get(this.currentTravelingPath.size() - 1);
				
				List<ICell> copy = new ArrayList(this.currentTravelingPath);
				copy.add(0, this.currentPosition);
				double costToDestination = PathFinder.calculateCost(copy);
				
				List<ICell> returnPathFromNext = pf.findPath(destinationCell,
						baseStation);
				double returnCostFromNextDestination = PathFinder
						.calculateCost(returnPathFromNext);

				if (returnCostFromNextDestination + costToDestination < remaining
						- destinationCell.getVacuumCost()) {
					ICell cell = this.currentTravelingPath.remove(0);
					if (cell.equals(currentPosition)) {
						cell = this.currentTravelingPath.remove(0);
					}

					return cell;
				} else {
					return returnToBaseStation();
				}
			}
		}
	}

	private ICell returnToBaseStation() {
		// All cells around are either obstacles or finished or we don't have
		// enough charges to go to that cell, so we need to return now.

		// If we are already at the charging station don't need to do anything
		if (this.currentPosition.getIsChargingStation()) {
			return null;
		} else {
			// If we aren't at the charging station then start us on the path
			// back to it.
			this.isReturning = true;
			this.currentTravelingPath = this.returnPath;
			this.currentTravelingPath.remove(0);
			return this.returnPath.remove(0);
		}
	}

	// check if can do vacuum one more time on a cell
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

	// given a target cell, go to it
	public double moveTo(ICell destination) {
		if (this.isReturning) {
			this.memory.setTraversed(destination);
			double batteryUnitDrain = (this.currentPosition.getTraverseCost() + destination
					.getTraverseCost()) / 2;
			this.currentPosition = destination;

			return batteryUnitDrain;

		} else {
			this.memory.setTraversed(destination);

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

	// calculate a path to next unfinished cell, which is not adjacent to the
	// current cell
	// call this method when the sweep finds out all cells around are either
	// obstacles or finished.
	private void findPathToNextKnownCell() {

		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());
		this.currentTravelingPath = pf.findPath(this.currentPosition,
				this.memory.getAllUnfinishedCells());
		this.currentTravelingPath.remove(0);
	}

	// calculate a return path to the charging point from current cell
	public void calculateReturnPath() {
		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());
		List<ICell> destination = new ArrayList<ICell>();
		destination.add(sensor.getStartPoint(this.currentFloor));
		this.returnPath = pf.findPath(this.currentPosition, destination);
		this.returnCost = PathFinder.calculateCost(this.returnPath);
	}

	// when the sweep goes back to charging point, call this method to reset.
	public void reset() {
		this.isReturning = false;
	}

	// call this method to return now(when the sweep finds out no enough charge
	// to do vacuum one more time, call this method)
	public void returnNow() {
		this.currentTravelingPath = this.returnPath;
		this.isReturning = true;
	}

	public boolean getIsReturning() {
		return this.isReturning;
	}

	public String sensorChecks() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.sensor.getPosXPathType(this.currentFloor,
				this.currentPosition.getX(), this.currentPosition.getY()) == PathType.OPEN ? "T"
				: "F");
		sb.append(this.sensor.getNegXPathType(this.currentFloor,
				this.currentPosition.getX(), this.currentPosition.getY()) == PathType.OPEN ? "T"
				: "F");
		sb.append(this.sensor.getPosYPathType(this.currentFloor,
				this.currentPosition.getX(), this.currentPosition.getY()) == PathType.OPEN ? "T"
				: "F");
		sb.append(this.sensor.getNegYPathType(this.currentFloor,
				this.currentPosition.getX(), this.currentPosition.getY()) == PathType.OPEN ? "T"
				: "F");
		return sb.toString();
	}
}
