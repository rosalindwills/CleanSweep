package com.se459.modules.models;

import java.util.ArrayList;
import java.util.List;

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

	public ICell findNext() {
		if (this.currentTravelingPath.size() == 0) {
			// check if there is a known but unfinished cell around
			if (sensor.getPosXPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getPosXCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifKnownButUnfinished(c)) {
					return c;
				}

			}
			if (sensor.getNegXPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getNegXCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifKnownButUnfinished(c)) {
					return c;
				}

			}
			if (sensor.getPosYPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getPosYCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifKnownButUnfinished(c)) {
					return c;
				}
			}
			if (sensor.getNegYPathType(currentFloor, currentPosition.getX(),
					currentPosition.getY()) == PathType.OPEN) {

				ICell c = sensor.getNegYCell(currentFloor,
						currentPosition.getX(), currentPosition.getY());
				if (this.memory.ifKnownButUnfinished(c)) {
					return c;
				}
			}

			findPathToNextKnownCell();

			return this.currentTravelingPath.remove(0);

		} else {
			return this.currentTravelingPath.remove(0);

		}

	}

	public void moveTo(ICell cell) {
		memory.becomeTraveled(cell);
		this.currentPosition = cell;
		if (this.sensor.getPosXPathType(currentFloor, cell.getX(), cell.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getPosXCell(currentFloor,
					cell.getX(), cell.getY()));
		}
		if (this.sensor.getPosYPathType(currentFloor, cell.getX(), cell.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getPosYCell(currentFloor,
					cell.getX(), cell.getY()));
		}
		if (this.sensor.getNegXPathType(currentFloor, cell.getX(), cell.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getNegXCell(currentFloor,
					cell.getX(), cell.getY()));
		}
		if (this.sensor.getNegYPathType(currentFloor, cell.getX(), cell.getY()) == PathType.OPEN) {
			this.memory.addNewCell(this.sensor.getNegYCell(currentFloor,
					cell.getX(), cell.getY()));
		}
	}

	public ICell readCurrentCell() {
		return currentPosition;
	}

	private void findPathToNextKnownCell() {

		LogFactory.newFileLog("/Users/wenhaoliu/Desktop/1.txt").append("In");
		List<List<ICell>> allPossiblePath = new ArrayList<List<ICell>>();
		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());
		LogFactory.newFileLog("/Users/wenhaoliu/Desktop/1.txt").append(
				this.memory.getAllKnownCells().size() + "");

		ICell closet = this.getCloset(this.currentPosition);
		allPossiblePath.addAll(pf.findAllPath(this.currentPosition, closet));

		int minimumCost = Integer.MAX_VALUE;
		int minimumCostPathNum = 0;
		for (int i = 0; i < allPossiblePath.size(); i++) {
			int pathChargeCost = 0;
			List<ICell> path = allPossiblePath.get(i);
			for (ICell cell : path) {
				if (cell.getSurfaceType() == SurfaceType.BAREFLOOR) {
					pathChargeCost += 1;
				} else if (cell.getSurfaceType() == SurfaceType.LOWPILE) {
					pathChargeCost += 2;
				} else if (cell.getSurfaceType() == SurfaceType.HIGHPILE) {
					pathChargeCost += 3;
				}

				if (pathChargeCost >= minimumCost) {
					break;
				}
			}

			if (pathChargeCost < minimumCost) {
				minimumCost = pathChargeCost;
				minimumCostPathNum = i;
			}

		}

		this.currentTravelingPath = allPossiblePath.get(minimumCostPathNum);
	}

	public ICell getCloset(ICell current){
		int minDistance = Integer.MAX_VALUE;
		int minIndex = 0;
		for(int i =0; i<this.memory.getAllKnownButNotTraveldCells().size() ; i++){
			ICell c = this.memory.getAllKnownButNotTraveldCells().get(i);
			int distance = getDistance(current, c);
			if(distance < minDistance){
				minDistance = distance;
				minIndex = i;
			}
		}
		
		return this.memory.getAllKnownButNotTraveldCells().get(minIndex);			
		}

	public int getDistance(ICell start, ICell end) {
		return Math.abs(start.getX() - end.getX())
				+ Math.abs(start.getY() - end.getY());
	}

}
