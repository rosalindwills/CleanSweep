package com.se459.modules.models;

import java.util.ArrayList;

import com.se459.util.log.Log;

public class Path {
	public ArrayList<Vector2> path = new ArrayList<Vector2>();
	public int startingPositionX, startingPositionY;
	public int endingPositionX, endingPositionY;
	
	int currentIndex = 0;
	
	public static Path GetPathToDestination(int startingLocationX, int startingLocationY, int destinationX, int destinationY, VacuumMemory memory, Log log)
	{
		Path path = new Path();
		
		path.startingPositionX = startingLocationX;
		path.startingPositionY = startingLocationY;
		
		path.endingPositionX = destinationX;
		path.endingPositionY = destinationY;
		
		path.path.add(new Vector2(path.startingPositionX, path.startingPositionY));
		
		int currentPositionX = path.startingPositionX;
		int currentPositionY = path.startingPositionY;
		
		// find the path between the start and the end in a straight line
		while(currentPositionX != path.endingPositionX || currentPositionY != path.endingPositionY)
		{
			int xDist = path.endingPositionX - currentPositionX;
			int yDist = path.endingPositionY - currentPositionY;
			
			int xSign = (xDist < 0) ? -1 : 1;
			int ySign = (yDist < 0) ? -1 : 1;
			
			if(Math.abs(xDist) > Math.abs(yDist) && null == memory.GetKnownObstacleAtPoint(currentPositionX + xSign, currentPositionY) || 
					(null != memory.GetKnownObstacleAtPoint(currentPositionX, currentPositionY + ySign) && xDist != 0))
			{
				currentPositionX += xSign;
			}
			else if(null == memory.GetKnownObstacleAtPoint(currentPositionX, currentPositionY + ySign))
			{
				currentPositionY += ySign;
			}
			else
			{
				log.append("Could not find path to destination");
				
				return path;
			}
			
			path.path.add(new Vector2(currentPositionX, currentPositionY));
		}
		
		return path;
	}
	
	public Vector2 GetNextPosition()
	{
		++currentIndex;
		
		if(currentIndex < path.size())
		{
			return path.get(currentIndex);
		}
		
		return new Vector2(endingPositionX, endingPositionY);
	}
	
	public boolean HasNext()
	{
		return currentIndex < path.size() - 1;
	}
}
