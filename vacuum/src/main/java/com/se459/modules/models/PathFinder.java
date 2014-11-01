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
	private List<ICell> reducedCells = new ArrayList<ICell>();

	private int maxPaths = 10000;

	public PathFinder(List<ICell> cells) {
		this.cellsCanBeUsed = new ArrayList<ICell>(cells);
	}

	public List<ICell> findPath(ICell start, ICell end) {

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
							if (cell.equals(end)) {
								List<ICell> returnPath = paths.get(end);
								returnPath.remove(start);
								return returnPath;
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
							if (cell.equals(end)) {
								List<ICell> returnPath = paths.get(end);
								returnPath.remove(start);
								return returnPath;
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
							if (cell.equals(end)) {
								List<ICell> returnPath = paths.get(end);
								returnPath.remove(start);
								return returnPath;
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
							if (cell.equals(end)) {
								List<ICell> returnPath = paths.get(end);
								returnPath.remove(start);
								return returnPath;
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
		
		List<ICell> returnPath = paths.get(end);
		returnPath.remove(start);
		return returnPath;

		

	}

	// Iterator<Map.Entry<ICell, List<ICell>>> it = paths.entrySet()
	// .iterator();
	// while (it.hasNext()) {
	// Map.Entry<ICell, List<ICell>> pairs = (Map.Entry<ICell,
	// List<ICell>>) it.next();
	// ICell head = pairs.getKey();
	// List<ICell> path = pairs.getValue();
	//
	// if(head.equals(end)){
	//
	// }
	//
	// it.remove(); // avoids a ConcurrentModificationException
	// }

	// }

	// public void next(ICell start, ICell end, List<ICell> path, int maxDepth,
	// int curDepth) {
	//
	// curDepth ++;
	//
	// if(curDepth > maxDepth){
	// return;
	// }
	//
	// ICell posX = null;
	// ICell posY = null;
	// ICell negX = null;
	// ICell negY = null;
	//
	// List<ICell> copy = new ArrayList<ICell>(path);
	//
	// if (start.getPathPosX() == PathType.OPEN) {
	// for (ICell cell : this.cellsCanBeUsed) {
	// if (cell.getX() == start.getX() + 1
	// && cell.getY() == start.getY()) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if (!copy.contains(cell) ) {
	// posX = cell;
	// }
	// }
	// }
	//
	// }
	// if (start.getPathPosY() == PathType.OPEN) {
	// for (ICell cell : this.cellsCanBeUsed) {
	// if (cell.getX() == start.getX()
	// && cell.getY() == start.getY() + 1) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if (!copy.contains(cell)) {
	// posY = cell;
	// }
	// }
	// }
	// }
	// if (start.getPathNegX() == PathType.OPEN) {
	// for (ICell cell : this.cellsCanBeUsed) {
	// if (cell.getX() == start.getX() - 1
	// && cell.getY() == start.getY()) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if(!copy.contains(cell)) {
	// negX = cell;
	// }
	// }
	// }
	// }
	// if (start.getPathNegY() == PathType.OPEN) {
	// for (ICell cell : this.cellsCanBeUsed) {
	// if (cell.getX() == start.getX()
	// && cell.getY() == start.getY() - 1) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if (!copy.contains(cell)) {
	// negY = cell;
	// }
	// }
	// }
	// }
	//
	// if(posX != null){
	// copy.add(posX);
	// next(posX, end, copy, maxDepth, curDepth);
	// copy.remove(posX)
	// }
	// }

	// public void nextUsingReduce(ICell start, ICell end, List<ICell> path) {
	//
	// List<ICell> copy = new ArrayList<ICell>(path);
	//
	// if (start.getPathPosX() == PathType.OPEN) {
	// for (ICell cell : this.reducedCells) {
	// if (cell.getX() == start.getX() + 1
	// && cell.getY() == start.getY()) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if (!copy.contains(cell)) {
	// copy.add(cell);
	// next(cell, end, copy);
	// }
	// }
	// }
	//
	// }
	// if (start.getPathPosY() == PathType.OPEN) {
	// for (ICell cell : this.reducedCells) {
	// if (cell.getX() == start.getX()
	// && cell.getY() == start.getY() + 1) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if (!copy.contains(cell)) {
	// copy.add(cell);
	// next(cell, end, copy);
	// }
	// }
	// }
	// }
	// if (start.getPathNegX() == PathType.OPEN) {
	// for (ICell cell : this.reducedCells) {
	// if (cell.getX() == start.getX() - 1
	// && cell.getY() == start.getY()) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if (!copy.contains(cell)) {
	// copy.add(cell);
	// next(cell, end, copy);
	// }
	// }
	// }
	// }
	// if (start.getPathNegY() == PathType.OPEN) {
	// for (ICell cell : this.reducedCells) {
	// if (cell.getX() == start.getX()
	// && cell.getY() == start.getY() - 1) {
	// if (cell.equals(end)) {
	// copy.remove(0);
	// copy.add(cell);
	// this.paths.add(copy);
	// } else if (!copy.contains(cell)) {
	// copy.add(cell);
	// next(cell, end, copy);
	// }
	// }
	// }
	// }
	// }

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
