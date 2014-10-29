package com.se459.modules.models;

import java.util.ArrayList;

import com.se459.sensor.interfaces.ICell;

public class VacuumMemory {

	public ArrayList<ICell> knownCells = new ArrayList<ICell>();
    public ArrayList<ICell> knownDirtyCells = new ArrayList<ICell>();
    public ArrayList<Obstacle> knownObstacles = new ArrayList<Obstacle>();
    
    void AddCell(ICell cell)
    {
    	if(!knownCells.contains(cell))
    	{
    		knownCells.add(cell);
    	}
    }
    
    void AddObstacle(Obstacle obstacle)
    {
    	if(!knownObstacles.contains(obstacle))
    	{
    		knownObstacles.add(obstacle);
    	}
    }
    
    void AddDirtyCell(ICell cell)
    {
    	if(!knownDirtyCells.contains(cell))
    	{
    		knownDirtyCells.remove(cell);
    	}
    }
    
    void RemoveDirtyCell(ICell cell)
    {
    	if(knownDirtyCells.contains(cell))
    	{
    		knownDirtyCells.remove(cell);
    	}
    }
	
    boolean CellHasKnownNeighbor(int x, int y)
	{
		return null != GetKnownCellAtPoint(x + 1, y) || null != GetKnownCellAtPoint(x - 1, y) || null != GetKnownCellAtPoint(x, y + 1) || null != GetKnownCellAtPoint(x, y - 1);
	}
	
	ICell GetKnownCellAtPoint(int x, int y)
	{
		for(int i = 0; i < knownCells.size(); ++i)
		{
			ICell cell = knownCells.get(i);
			if(cell.getX() == x && cell.getY() == y)
			{
				return cell;
			}
		}
		
		return null;
	}
	
	Obstacle GetKnownObstacleAtPoint(int x, int y)
	{
		for(int i = 0; i < knownObstacles.size(); ++i)
		{
			Obstacle obstacle = knownObstacles.get(i);
			if(obstacle.GetX() == x && obstacle.GetY() == y)
			{
				return obstacle;
			}
		}
		
		return null;
	}
	
	ICell FindChargingCell()
	{
		for(int i = 0; i < knownCells.size(); ++i)
		{
			if(knownCells.get(i).getIsChargingStation())
			{
				return knownCells.get(i);
			}
		}
		
		// scnLog.append("Could not find charging station in list of known cells.");
		return null;
	}
	
	boolean CellIsUnkownAndHasKnownNeighbor(int x, int y)
	{
		return null == GetKnownCellAtPoint(x, y) && 
				null == GetKnownObstacleAtPoint(x, y) && 
				CellHasKnownNeighbor(x, y);
	}
}
