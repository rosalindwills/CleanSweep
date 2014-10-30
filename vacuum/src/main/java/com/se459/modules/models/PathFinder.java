package com.se459.modules.models;

import java.util.ArrayList;
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

import com.se459.sensor.interfaces.ICell;

public class PathFinder {
	private List<ICell> completedCells = new ArrayList<ICell>();
	private List<ICell> reducedCells = new ArrayList<ICell>();
	private List<List<ICell>> paths = new ArrayList<List<ICell>>();
	private int tolerance = 0;

	public PathFinder(List<ICell> cells) {
		this.completedCells = new ArrayList<ICell>(cells);
	}

	public List<List<ICell>> findAllPath(int startX, int startY, int endX,
			int endY) {
		if (!(startX == endX && startY == endY)) {
			reduceCells(startX, startY, endX, endY, tolerance);
			ICell root = getCell(startX, startY);
			List<ICell> path = new ArrayList<ICell>();
			path.add(root);

			boolean xDirection = false;
			boolean yDirection = false;
			if (startX <= endX) {
				xDirection = true;
			}
			if (startY <= endY) {
				yDirection = true;
			}
			// next(root.getX(), root.getY(), endX, endY, path);
			nextUsingReduce(root.getX(), root.getY(), endX, endY, path,
					xDirection, yDirection);

			if (paths.size() == 0) {
				next(startX, startY, endX, endY, path, xDirection, yDirection);
			}

		}

		return paths;

	}

	private void next(int startX, int startY, int endX, int endY,
			List<ICell> path,boolean xDirection,boolean yDirection) {
		ICell root = getCell(startX, startY);

		for (ICell n : getNeighbors(root, xDirection, yDirection)) {
			if (path.contains(n)) {
				continue;
			} else {
				if (n.getX() == endX && n.getY() == endY) {
					List<ICell> pathClone = new ArrayList<ICell>(path);


					this.paths.add(pathClone);

				} else {
					List<ICell> pathClone = new ArrayList<ICell>(path);
					pathClone.add(n);
					next(n.getX(), n.getY(), endX, endY, pathClone, xDirection, yDirection);
				}
			}
		}
	}

	private List<ICell> getNeighbors(ICell current, boolean xDirection,
			boolean yDirection) {
		List<ICell> neighbors = new ArrayList<ICell>();

		for (ICell cell : this.completedCells) {
			if (!cell.equals(current)) {
				if (xDirection && yDirection) {
					if (cell.getY() == current.getY() + 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() + 1) {
						neighbors.add(cell);
					}
					
					if (neighbors.size() == 0){
						if (cell.getY() == current.getY()
								&& cell.getX() == current.getX() - 1) {
							neighbors.add(cell);
						}
						if (cell.getY() == current.getY() - 1
								&& cell.getX() == current.getX()) {
							neighbors.add(cell);
						}
					}
				}

				else if (xDirection && !yDirection) {
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() + 1) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY() - 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
					
					if (neighbors.size() == 0){
						if (cell.getY() == current.getY()
								&& cell.getX() == current.getX() + 1) {
							neighbors.add(cell);
						}
						if (cell.getY() == current.getY() - 1
								&& cell.getX() == current.getX()) {
							neighbors.add(cell);
						}
					}
				}

				else if (!xDirection && yDirection) {
					if (cell.getY() == current.getY() + 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() - 1) {
						neighbors.add(cell);
					}
					
					if (neighbors.size() == 0){
						if (cell.getY() == current.getY()
								&& cell.getX() == current.getX() + 1) {
							neighbors.add(cell);
						}
						if (cell.getY() == current.getY() - 1
								&& cell.getX() == current.getX()) {
							neighbors.add(cell);
						}
					}
					
				} else {
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() - 1) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY() - 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
					if (neighbors.size() == 0){
						if (cell.getY() == current.getY()
								&& cell.getX() == current.getX() + 1) {
							neighbors.add(cell);
						}
						if (cell.getY() == current.getY() + 1
								&& cell.getX() == current.getX()) {
							neighbors.add(cell);
						}
					}
				}
			}
		}

		return neighbors;
	}

	private void nextUsingReduce(int startX, int startY, int endX, int endY,
			List<ICell> path, boolean xDirection, boolean yDirection) {
		ICell root = getCell(startX, startY);
		for (ICell n : getNeighborsUsingReduce(root, xDirection, yDirection)) {
			if (path.contains(n)) {
				continue;
			} else {
				if (n.getX() == endX && n.getY() == endY) {
					List<ICell> pathClone = new ArrayList<ICell>(path);
					pathClone.add(n);
					this.paths.add(pathClone);
				} else {
					List<ICell> pathClone = new ArrayList<ICell>(path);
					pathClone.add(n);
					nextUsingReduce(n.getX(), n.getY(), endX, endY, pathClone,
							xDirection, yDirection);
				}
			}
		}
	}

	private List<ICell> getNeighborsUsingReduce(ICell current,
			boolean xDirection, boolean yDirection) {
		List<ICell> neighbors = new ArrayList<ICell>();

		for (ICell cell : this.reducedCells) {
			if (!cell.equals(current)) {
				if (xDirection && yDirection) {
					if (cell.getY() == current.getY() + 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() + 1) {
						neighbors.add(cell);
					}
				}

				else if (xDirection && !yDirection) {
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() + 1) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY() - 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
				}

				else if (!xDirection && yDirection) {
					if (cell.getY() == current.getY() + 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() - 1) {
						neighbors.add(cell);
					}
				} else {
					if (cell.getY() == current.getY()
							&& cell.getX() == current.getX() - 1) {
						neighbors.add(cell);
					}
					if (cell.getY() == current.getY() - 1
							&& cell.getX() == current.getX()) {
						neighbors.add(cell);
					}
				}
			}
		}

		return neighbors;
	}

	private ICell getCell(int x, int y) {
		for (ICell c : completedCells) {
			if (c.getX() == x && c.getY() == y) {
				return c;
			}
		}
		return null;
	}

	private void reduceCells(int startX, int startY, int endX, int endY,
			int tolerance) {
		this.reducedCells = new ArrayList<ICell>(this.completedCells);

		if (startX >= endX) {
			// end
			// start
			if (startY >= endY) {

				for (int i = 0; i < this.reducedCells.size(); i++) {
					ICell c = reducedCells.get(i);
					if (c.getX() > startX + tolerance
							|| c.getX() < endX - tolerance
							|| c.getY() > startY + tolerance
							|| c.getY() < endY - tolerance) {
						reducedCells.remove(i);
						i--;
					}
				}

			}
			// start
			// end
			else {
				for (int i = 0; i < this.reducedCells.size(); i++) {
					ICell c = reducedCells.get(i);
					if (c.getX() > startX + tolerance
							|| c.getX() < endX - tolerance
							|| c.getY() > endY + tolerance
							|| c.getY() < startY - tolerance) {
						reducedCells.remove(i);
						i--;
					}
				}
			}
		} else if (startX < endX) {
			// end
			// start
			if (startY >= endY) {
				for (int i = 0; i < this.reducedCells.size(); i++) {
					ICell c = reducedCells.get(i);
					if (c.getX() > endX + tolerance
							|| c.getX() < startX - tolerance
							|| c.getY() > startY + tolerance
							|| c.getY() < endY - tolerance) {
						reducedCells.remove(i);
						i--;
					}
				}
			}
			// start
			// end
			else {
				for (int i = 0; i < this.reducedCells.size(); i++) {
					ICell c = reducedCells.get(i);
					if (c.getX() < startX - tolerance
							|| c.getX() > endX + tolerance
							|| c.getY() > endY + tolerance
							|| c.getY() < startY - tolerance) {
						reducedCells.remove(i);
						i--;
					}
				}
			}
		}

	}

}
