package models;

import interfaces.ICell;
import interfaces.IFloor;

import java.util.ArrayList;
import java.util.List;

public class Floor implements IFloor {
	private List<ICell> _cells;
	private int _level;
	
	public Floor(int level) {
		_level = level;
		_cells = new ArrayList<ICell>();
	}
	
	public int getLevel() {
		return _level;
	}

	public ICell getCell(int x, int y) {
		for(ICell cell : _cells) {
			if (cell.getX() == x && cell.getY() == y) {
				return cell;
			}
		}
		return null;
	}
	
	public void addCell(ICell cell) {
		_cells.add(cell);
	}

	public void cleanCell(int x, int y) {
		ICell cell = getCell(x, y);
		cell.cleanCell();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Floor " + getLevel() + "\n");
		for (ICell cell : _cells) {
			builder.append(cell.toString());
		}
		return builder.toString();
	}
}

