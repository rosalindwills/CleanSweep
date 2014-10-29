package com.se459.modules.models;

import java.util.ArrayList;

public class Path {
	public ArrayList<Vector2> path;
	public Vector2 startingPosition;
	public Vector2 endingPosition;
	
	public static Path GetPathToDestination(Vector2 startingLocation, Vector2 destination)
	{
		Path path = new Path();
		
		path.startingPosition = startingLocation;
		path.endingPosition = destination;
		
		return path;
	}
}
