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
			currentPath = Path.GetPathToDestination(currentPosition.x, currentPosition.y, navigationDestination.x, navigationDestination.y, memory, log);
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
			
			++offset;
		}
		
		navigationDestination = destination;
		currentPath = Path.GetPathToDestination(currentPosition.x, currentPosition.y, navigationDestination.x, navigationDestination.y, memory, log);
		
		log.append("Setting destination to: " + navigationDestination.x + ", " + navigationDestination.y);
	}
	
	public void SetDestinationToBaseStation()
	{
		ICell chargingCell = memory.FindChargingCell();
		
		if(null != chargingCell)
		{
			navigationDestination =  new Vector2(chargingCell.getX(), chargingCell.getY());
			currentPath = Path.GetPathToDestination(currentPosition.x, currentPosition.y, navigationDestination.x, navigationDestination.y, memory, log);
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
		if(null != currentPath)
		{
			if(currentPath.HasNext())
			{
				Vector2 desiredPosition = currentPath.GetNextPosition();
				
				if(null == sensor.readCell(1, desiredPosition.x, desiredPosition.y))
				{
					memory.AddObstacle(new Obstacle(desiredPosition.x, desiredPosition.y));
					SetPathToDirtyOrUnknown();
				}
				else
				{
					currentPosition = desiredPosition;
				}
			}
			else
			{
				currentPath = null;
			}
		}
	}	
}
