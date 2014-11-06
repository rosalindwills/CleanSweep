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
	
	private List<ICell> returnPath = new ArrayList<ICell>();

	private int currentFloor = 1;

	private ISensor sensor;

	private double returnCost = 0;

	private boolean isReturning = false;
	
	private Log log;

	public ICell readCurrentCell(int floor) {
		return currentPosition;
	}

	public NavigationLogic(ISensor sensor, int floor,
			VacuumMemory vacuumMemory) {
		this.currentPosition = sensor.getStartPoint(this.currentFloor);
		this.sensor = sensor;
		this.memory = vacuumMemory;
		this.log = LogFactory.newFileLog();
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
			// vacuum finds out its done 
			if(memory.getAllUnfinishedCells().isEmpty()){
				// vacuum needs to return to the charging point
				if(!this.currentPosition.equals(this.sensor.getStartPoint(this.currentFloor))){
					this.isReturning = true;
					this.currentTravelingPath = this.returnPath;
					this.currentTravelingPath.remove(0);
					return this.currentTravelingPath.remove(0);
				}
				// vacuum has already returned to the charging point. 
				else{
					return null;
					
				}
			}

			// vacuum is traveling along a path. 
			// The path could be a return path to the charging point, 
			// or a path to the next unfinished cell, which is not adjacent to current cell.
			// 
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
					this.currentTravelingPath.remove(0);
					return this.returnPath.remove(0);
				}

			} 
			
			// vacuum is exploring the cells around. 
			else {
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

				// vacuum realizes all cells around are either obstacles or finished.
				// now it needs to go to a unfinished cell, which is not adjacent to the current cell.
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

				} 	
				// pick a adjacent cell to go 
				else {
					// test if posx is open and unfinished
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
					// test if negx is open and unfinished
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
					// test if posy is open and unfinished
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
					// test if negy is open and unfinished
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

					// All cells around are either obstacles or finished or we don't have 
					// enough charges to go to that cell, so we need to return now.
					this.isReturning = true;
					this.currentTravelingPath = this.returnPath;
					this.currentTravelingPath.remove(0);
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

	private void findPathToNextKnownCell() {

		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());

		this.currentTravelingPath = pf.findPath(this.currentPosition, this.memory.getAllUnfinishedCells());
		this.currentTravelingPath.remove(0);
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
	
	public void returnNow(){
		this.currentTravelingPath = this.returnPath;
		this.isReturning = true;
	}

}
