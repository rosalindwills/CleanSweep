package com.se459.modules.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;

public class NavigationLogic {

	Vector2 currentPosition;
	Path currentPath;
	Vector2 navigationDestination;

	private ISensor sensor;
	
	private VacuumMemory memory;
	
	Log log;
	
	public NavigationLogic(Vector2 position, ISensor iSensor, VacuumMemory vacuumMemory, Log log)
	{
		currentPosition = position;
		navigationDestination = currentPosition;
		
		sensor = iSensor;
		memory = vacuumMemory;
		
		this.log = log;
	}
	
	public void SetPathToDirtyOrUnknown()
	{
		if(memory.knownDirtyCells.size() > 0)
		{
			SetPathToNearestDirtyCell();
		}
		
		SetPathToNearestUnknownCell();
	}
	
	private void SetPathToNearestDirtyCell()
	{
		if(memory.knownDirtyCells.size() > 0)
		{
			// find the nearest dirty cell
			int closestDistance = Integer.MAX_VALUE;
			ICell closestCell = null;
			
			for(int i = 0; i < memory.knownDirtyCells.size(); ++i)
			{
				int distance = GetDistanceToCell(memory.knownDirtyCells.get(i));
				if(closestDistance > distance)
				{
					closestCell = memory.knownDirtyCells.get(i);
					closestDistance = distance;
				}
			}

			navigationDestination = new Vector2(closestCell.getX(), closestCell.getY());
			currentPath = Path.GetPathToDestination(currentPosition, navigationDestination);
		}
	}
	
	private void SetPathToNearestUnknownCell()
	{
		// find the nearest cell that we don't know about
		int offset = 1;
		Vector2 destination = null;
				
		while(true)
		{
			int offset2 = 0;
			
			while(offset2 <= offset)
			{
				if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x - offset, currentPosition.y - offset2))
				{
					destination = new Vector2(currentPosition.x - offset, currentPosition.y - offset2);
				}
				else if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x - offset, currentPosition.y + offset2))
				{
					destination = new Vector2(currentPosition.x - offset, currentPosition.y + offset2);
				}
				else if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x + offset, currentPosition.y - offset2))
				{
					destination = new Vector2(currentPosition.x + offset, currentPosition.y - offset2);
				}
				else if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x + offset, currentPosition.y + offset2))
				{
					destination = new Vector2(currentPosition.x + offset, currentPosition.y + offset2);
				}
				else if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x - offset2, currentPosition.y - offset))
				{
					destination = new Vector2(currentPosition.x - offset2, currentPosition.y - offset);
				}
				else if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x + offset2, currentPosition.y - offset))
				{
					destination = new Vector2(currentPosition.x + offset2, currentPosition.y - offset);
				}
				else if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x - offset2, currentPosition.y + offset))
				{
					destination = new Vector2(currentPosition.x - offset2, currentPosition.y + offset);
				}
				else if(memory.CellIsUnkownAndHasKnownNeighbor(currentPosition.x + offset2, currentPosition.y + offset))
				{
					destination = new Vector2(currentPosition.x + offset2, currentPosition.y + offset);	
				}
				
				if(null != destination)
				{
					break;
				}
				
				++offset2;
			}
			
			if(null != destination)
			{
				break;
			}
		}
		
		navigationDestination = destination;
		currentPath = Path.GetPathToDestination(currentPosition, destination);
		
		log.append("Setting destination to: " + navigationDestination.x + ", " + navigationDestination.y);
	}
	
	public void SetDestinationToBaseStation()
	{
		ICell chargingCell = memory.FindChargingCell();
		
		if(null != chargingCell)
		{
			navigationDestination =  new Vector2(chargingCell.getX(), chargingCell.getY());
			currentPath = Path.GetPathToDestination(currentPosition, navigationDestination);
		}
	}

	private int GetDistanceToCell(ICell cell)
	{
		return GetDistanceToPoint(cell.getX(), cell.getY());
	}
	
	private int GetDistanceToPoint(int pointX, int pointY)
	{
		return Math.abs(currentPosition.x - pointX) + Math.abs(currentPosition.y - pointY);
	}
	
	public void MoveTowardsDestination()
	{
		int xDist = currentPath.endingPosition.x - currentPosition.x;
		int yDist = currentPath.endingPosition.y - currentPosition.y;
		
		int xDir = 0;
		int yDir = 0;
		
		if(xDist == 0 && yDist == 0)
		{
			currentPath = null;
			return;
		}
		else if(Math.abs(xDist) > Math.abs(yDist))
		{
			if(xDist > 0)
			{
				xDir = 1;
			}
			else
			{
				xDir = -1;
			}
		}
		else
		{
			if(yDist > 0)
			{
				yDir = 1;
			}
			else
			{
				yDir = -1;
			}
		}
		
		if(null == sensor.readCell(1, currentPosition.x + xDir, currentPosition.y + yDir))
		{
			memory.AddObstacle(new Obstacle(currentPosition.x + xDir, currentPosition.y + yDir));
			SetPathToDirtyOrUnknown();
		}
		else
		{
			currentPosition.x += xDir;
			currentPosition.y += yDir;
		}
	}	
}
