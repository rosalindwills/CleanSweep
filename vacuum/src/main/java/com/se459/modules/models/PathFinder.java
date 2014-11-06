package com.se459.modules.models;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Hashtable;
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
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.interfaces.ICell;

public class PathFinder {
	private List<ICell> cellsCanBeUsed = new ArrayList<ICell>();

	public PathFinder(List<ICell> cells) {
		this.cellsCanBeUsed = new ArrayList<ICell>(cells);
	}

	class ValueComparator implements Comparator<ICell> {

		Map<ICell, List<ICell>> map;

		public ValueComparator(Map<ICell, List<ICell>> base) {
			this.map = base;
		}

		public int compare(ICell a, ICell b) {
			if (PathFinder.calculateCost(map.get(a)) >= PathFinder
					.calculateCost(map.get(b))) {
				return 1;
			} else {
				return -1;
			}
		}
	}

	private Map<ICell, List<ICell>> sortByValue(
			Map<ICell, List<ICell>> unsortedMap) {
		Map<ICell, List<ICell>> sortedMap = new TreeMap<ICell, List<ICell>>(
				new ValueComparator(unsortedMap));
		sortedMap.putAll(unsortedMap);
		return sortedMap;
	}

	// given a start cell, and a collection of unfinished cells,
	// using Dijkstra's shortest paths algorithm to calculate the shortest paths
	// from start cell to each unfinished cell,
	// and then pick a shortest one among all shortest paths.
	public List<ICell> findPath(ICell start, List<ICell> unfinishedCells) {

		Map<ICell, List<ICell>> finished = new Hashtable<ICell, List<ICell>>();
		Map<ICell, List<ICell>> heads = new Hashtable<ICell, List<ICell>>();
		List<ICell> list = new ArrayList<ICell>();
		list.add(start);
		heads.put(start, list);

		while (heads.size() > 0) {
			Map<ICell, List<ICell>> sortedMap = this.sortByValue(heads);
			Set<Entry<ICell, List<ICell>>> sortedSet = sortedMap.entrySet();

			ICell head = sortedSet.iterator().next().getKey();

			if (head.getPathPosX() == PathType.OPEN) {
				for (ICell cell : this.cellsCanBeUsed) {
					if (cell.getX() == head.getX() + 1
							&& cell.getY() == head.getY()) {

						if (!heads.containsKey(cell)) {

							List<ICell> path = new ArrayList<ICell>(
									heads.get(head));
							path.add(cell);
							heads.put(cell, path);

						} else {
							List<ICell> oldPath = new ArrayList<ICell>(
									heads.get(cell));
							List<ICell> newPath = new ArrayList<ICell>(
									heads.get(head));
							newPath.add(cell);
							if (calculateCost(newPath) < calculateCost(oldPath)) {
								heads.put(cell, newPath);
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

						if (!heads.containsKey(cell)) {
							List<ICell> path = new ArrayList<ICell>(
									heads.get(head));
							path.add(cell);
							heads.put(cell, path);

						} else {
							List<ICell> oldPath = new ArrayList<ICell>(
									heads.get(cell));
							List<ICell> newPath = new ArrayList<ICell>(
									heads.get(head));
							newPath.add(cell);
							if (calculateCost(newPath) < calculateCost(oldPath)) {
								heads.put(cell, newPath);
							}

						}
						break;
					}

				}
			}

			System.out.println(head);
			System.out.println(this.cellsCanBeUsed);
			if (head.getPathNegX() == PathType.OPEN) {
				System.out.println(0);
				for (ICell cell : this.cellsCanBeUsed) {
					System.out.println(cell);
					if (cell.getX() == head.getX() - 1
							&& cell.getY() == head.getY()) {
						System.out.println(1);
						if (!heads.containsKey(cell)) {
							System.out.println(2);
							List<ICell> path = new ArrayList<ICell>(
									heads.get(head));
							path.add(cell);
							heads.put(cell, path);

						} else {
							System.out.println(3);
							List<ICell> oldPath = new ArrayList<ICell>(
									heads.get(cell));
							List<ICell> newPath = new ArrayList<ICell>(
									heads.get(head));
							newPath.add(cell);
							if (calculateCost(newPath) < calculateCost(oldPath)) {
								heads.put(cell, newPath);
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

						if (!heads.containsKey(cell)) {
							List<ICell> path = new ArrayList<ICell>(
									heads.get(head));
							path.add(cell);
							heads.put(cell, path);

						} else {
							List<ICell> oldPath = new ArrayList<ICell>(
									heads.get(cell));
							List<ICell> newPath = new ArrayList<ICell>(
									heads.get(head));
							newPath.add(cell);
							if (calculateCost(newPath) < calculateCost(oldPath)) {
								heads.put(cell, newPath);
							}

						}
						break;
					}

				}
			}

			finished.put(head, heads.get(head));
			this.cellsCanBeUsed.remove(head);
			heads.remove(head);

		}

		double minCost = Double.MAX_VALUE;
		List<ICell> minCostPath = new ArrayList<ICell>();
		for (ICell c : unfinishedCells) {
			List<ICell> path = finished.get(c);
			if (calculateCost(path) < minCost) {
				minCostPath = path;
				minCost = calculateCost(path);
			}
		}

		return minCostPath;

	}

	// calculate then cost to travel along a path
	public static double calculateCost(List<ICell> path) {
		double pathCost = 0;

		for (int i = 0; i < path.size() - 1; i++) {
			ICell current = path.get(i);
			ICell next = path.get(i + 1);
			double moveCost = (current.getTraverseCost() + next
					.getTraverseCost()) / 2.0;
			pathCost += moveCost;
		}

		return pathCost;
	}

}
