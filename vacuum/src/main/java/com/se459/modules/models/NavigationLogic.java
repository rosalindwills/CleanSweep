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

			return this.currentTravelingPath.remove(0);

		} else {
			return this.currentTravelingPath.remove(0);

		}

	}

	public void moveTo(ICell cell) {
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

		PathFinder pf = new PathFinder(this.memory.getAllKnownCells());

		this.currentTravelingPath = pf
				.findPath(
						this.currentPosition,
						this.memory.getAllKnownButNotTraveldCells().get(
								this.memory.getAllKnownButNotTraveldCells()
										.size() - 1));

	}

}
