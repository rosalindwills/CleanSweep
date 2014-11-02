package com.se459.modules.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/*
 class Cell{
 int x;
 int y;
 boolean nx;
 boolean ny;
 boolean px;
 boolean py;

 public Cell(int x, int y, boolean px, boolean py, boolean nx, boolean ny) {
 super();
 this.x = x;
 this.y = y;
 this.nx = nx;
 this.ny = ny;
 this.px = px;
 this.py = py;
 }

 public String toString(){
 return "(" + x + ", " + y + ")";
 }

 public boolean equals(Object obj) {
 if (!(obj instanceof Cell))
 return false;
 if (obj == this)
 return true;
 Cell cell = (Cell) obj;
 return cell.x == this.x && cell.y == this.y;
 }
 }
 */
import java.util.Map;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.interfaces.ICell;

public class PathFinder {
	private List<ICell> cellsCanBeUsed = new ArrayList<ICell>();

	public PathFinder(List<ICell> cells) {
		this.cellsCanBeUsed = new ArrayList<ICell>(cells);
	}

	public List<ICell> findPath(ICell start, List<ICell> unfinishedCells) {

		List<ICell> heads = new ArrayList<ICell>();
		heads.add(start);

		List<ICell> path = new ArrayList<ICell>();
		path.add(start);

		Map<ICell, List<ICell>> paths = new HashMap<ICell, List<ICell>>();

		paths.put(start, path);

		for (int i = 0; i < heads.size(); i++) {
			ICell head = heads.get(i);

			if (head.getPathPosX() == PathType.OPEN) {
				for (ICell cell : this.cellsCanBeUsed) {
					if (cell.getX() == head.getX() + 1
							&& cell.getY() == head.getY()) {
						if (!paths.containsKey(cell)) {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							paths.put(cell, copy);
							heads.add(cell);

						} else {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							List<ICell> oldPath = paths.get(cell);
							double oldPathCost = calculateCost(oldPath);
							double newPathCost = calculateCost(newPath);
							if (newPathCost < oldPathCost) {
								paths.put(cell, copy);
							}
						}
						break;
					}
				}
			}

			if (head.getPathPosY() == PathType.OPEN) {
				for (ICell cell : this.cellsCanBeUsed) {
					if (cell.getX() == head.getX()
							&& cell.getY() == head.getY() + 1) {
						if (!paths.containsKey(cell)) {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							paths.put(cell, copy);
							heads.add(cell);

						} else {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							List<ICell> oldPath = paths.get(cell);
							double oldPathCost = calculateCost(oldPath);
							double newPathCost = calculateCost(newPath);
							if (newPathCost < oldPathCost) {
								paths.put(cell, copy);
							}
						}
						break;
					}
				}
			}

			if (head.getPathNegX() == PathType.OPEN) {
				for (ICell cell : this.cellsCanBeUsed) {
					if (cell.getX() == head.getX() - 1
							&& cell.getY() == head.getY()) {
						if (!paths.containsKey(cell)) {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							paths.put(cell, copy);
							heads.add(cell);

						} else {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							List<ICell> oldPath = paths.get(cell);
							double oldPathCost = calculateCost(oldPath);
							double newPathCost = calculateCost(newPath);
							if (newPathCost < oldPathCost) {
								paths.put(cell, copy);
							}
						}
						break;
					}
				}
			}

			if (head.getPathNegY() == PathType.OPEN) {
				for (ICell cell : this.cellsCanBeUsed) {
					if (cell.getX() == head.getX()
							&& cell.getY() == head.getY() - 1) {
						if (!paths.containsKey(cell)) {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							paths.put(cell, copy);
							heads.add(cell);

						} else {
							List<ICell> newPath = paths.get(head);
							List<ICell> copy = new ArrayList<ICell>(newPath);
							copy.add(cell);
							List<ICell> oldPath = paths.get(cell);
							double oldPathCost = calculateCost(oldPath);
							double newPathCost = calculateCost(newPath);
							if (newPathCost < oldPathCost) {
								paths.put(cell, copy);
							}
						}
						break;
					}
				}
			}

			heads.remove(i);
			i--;
			// paths.remove(head);

		}

		double minCost = Double.MAX_VALUE;
		List<ICell> mimCostPath = new ArrayList<ICell>();
		for (ICell cell : unfinishedCells) {
			List<ICell> p = paths.get(cell);
			double cost = calculateCost(p);
			if (cost < minCost) {
				minCost = cost;
				mimCostPath = p;
			}
		}

		return mimCostPath;

	}

	public double calculateCost(List<ICell> path) {
		double pathCost = 0;

		for (int i = 0; i < path.size() - 1; i++) {
			ICell current = path.get(i);
			ICell next = path.get(i + 1);
			double moveCost = (current.getTraverseCost() + next.getTraverseCost())/2;

			pathCost += moveCost;
		}

		return pathCost;
	}

}
