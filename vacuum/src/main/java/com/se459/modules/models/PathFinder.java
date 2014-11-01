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

import com.se459.sensor.enums.PathType;
import com.se459.sensor.interfaces.ICell;

public class PathFinder {
	private List<ICell> cellsCanBeUsed = new ArrayList<ICell>();
	private List<ICell> reducedCells = new ArrayList<ICell>();
	private List<List<ICell>> paths = new ArrayList<List<ICell>>();

	private int maxPaths = 10000;

	public PathFinder(List<ICell> cells) {
		this.cellsCanBeUsed = new ArrayList<ICell>(cells);
	}

	public List<List<ICell>> findAllPath(ICell start, ICell end) {
		if (!start.equals(end)) {
			for (int toleranceX = 0; toleranceX <= 6; toleranceX++) {
				for (int toleranceY = 0; toleranceY <= 6; toleranceY++) {
					this.reduceCells(start.getX(), start.getY(), end.getX(),
							end.getY(), toleranceX, toleranceY);
					this.nextUsingReduce(start, end, new ArrayList());
					if (paths.size() != 0) {
						return paths;
					}
				}
			}

			next(start, end, new ArrayList());
		}

		return paths;
	}

	public void next(ICell start, ICell end, List<ICell> path) {

		if (this.paths.size() >= this.maxPaths) {
			return;
		}

		List<ICell> copy = new ArrayList<ICell>(path);

		if (start.getPathPosX() == PathType.OPEN) {
			for (ICell cell : this.cellsCanBeUsed) {
				if (cell.getX() == start.getX() + 1
						&& cell.getY() == start.getY()) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}

		}
		if (start.getPathPosY() == PathType.OPEN) {
			for (ICell cell : this.cellsCanBeUsed) {
				if (cell.getX() == start.getX()
						&& cell.getY() == start.getY() + 1) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}
		}
		if (start.getPathNegX() == PathType.OPEN) {
			for (ICell cell : this.cellsCanBeUsed) {
				if (cell.getX() == start.getX() - 1
						&& cell.getY() == start.getY()) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}
		}
		if (start.getPathNegY() == PathType.OPEN) {
			for (ICell cell : this.cellsCanBeUsed) {
				if (cell.getX() == start.getX()
						&& cell.getY() == start.getY() - 1) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}
		}
	}

	public void nextUsingReduce(ICell start, ICell end, List<ICell> path) {

		List<ICell> copy = new ArrayList<ICell>(path);

		if (start.getPathPosX() == PathType.OPEN) {
			for (ICell cell : this.reducedCells) {
				if (cell.getX() == start.getX() + 1
						&& cell.getY() == start.getY()) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}

		}
		if (start.getPathPosY() == PathType.OPEN) {
			for (ICell cell : this.reducedCells) {
				if (cell.getX() == start.getX()
						&& cell.getY() == start.getY() + 1) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}
		}
		if (start.getPathNegX() == PathType.OPEN) {
			for (ICell cell : this.reducedCells) {
				if (cell.getX() == start.getX() - 1
						&& cell.getY() == start.getY()) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}
		}
		if (start.getPathNegY() == PathType.OPEN) {
			for (ICell cell : this.reducedCells) {
				if (cell.getX() == start.getX()
						&& cell.getY() == start.getY() - 1) {
					if (cell.equals(end)) {
						copy.remove(0);
						copy.add(cell);
						this.paths.add(copy);
					} else if (!copy.contains(cell)) {
						copy.add(cell);
						next(cell, end, copy);
					}
				}
			}
		}
	}

	private void reduceCells(int startX, int startY, int endX, int endY,
			int toleranceX, int toleranceY) {
		this.reducedCells = new ArrayList<ICell>(this.cellsCanBeUsed);

		if (startX >= endX) {
			// end
			// start
			if (startY >= endY) {

				for (int i = 0; i < this.reducedCells.size(); i++) {
					ICell c = reducedCells.get(i);
					if (c.getX() > startX + toleranceX
							|| c.getX() < endX - toleranceX
							|| c.getY() > startY + toleranceY
							|| c.getY() < endY - toleranceY) {
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
					if (c.getX() > startX + toleranceX
							|| c.getX() < endX - toleranceX
							|| c.getY() > endY + toleranceY
							|| c.getY() < startY - toleranceY) {
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
					if (c.getX() > endX + toleranceX
							|| c.getX() < startX - toleranceX
							|| c.getY() > startY + toleranceY
							|| c.getY() < endY - toleranceY) {
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
					if (c.getX() < startX - toleranceX
							|| c.getX() > endX + toleranceX
							|| c.getY() > endY + toleranceY
							|| c.getY() < startY - toleranceY) {
						reducedCells.remove(i);
						i--;
					}
				}
			}
		}

	}

}
