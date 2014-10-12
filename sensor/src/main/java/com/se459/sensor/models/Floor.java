package com.se459.sensor.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IFloor;
import java.util.ArrayList;
import java.util.List;

public class Floor implements IFloor {
	private List<ICell> _cells;
	private int _level;
	private int minX, maxX, minY, maxY;
	
	public Floor(int level) {
		_level = level;
		_cells = new ArrayList<ICell>();
		
		minX = Integer.MAX_VALUE;
		maxX = Integer.MIN_VALUE;
		
		minY = Integer.MAX_VALUE;
		maxY = Integer.MIN_VALUE;
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
		AdjustMinMax(cell);
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
	
	public int getMinX()
	{
		return minX;
	}
	
	public int getMaxX()
	{
		return maxX;
	}
	
	public int getMinY()
	{
		return minY;
	}
	
	public int getMaxY()
	{
		return maxY;
	}
	
	private void AdjustMinMax(ICell newCell)
	{
		if(newCell.getX() < minX)
		{
			minX = newCell.getX();
		}
		if(newCell.getX() > maxX)
		{
			maxX = newCell.getX();
		}
		if(newCell.getY() < minY)
		{
			minY = newCell.getY();
		}
		if(newCell.getY() > maxY)
		{
			maxY = newCell.getY();
		}
	}
}

