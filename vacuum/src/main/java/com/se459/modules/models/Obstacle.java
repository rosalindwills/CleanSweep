package com.se459.modules.models;

public class Obstacle {
	
	int x, y;
	
	public Obstacle(int xPosition, int yPosition)
	{
		x = xPosition;
		y = yPosition;
	}
	
	public int GetX()
	{
		return x;
	}
	
	public int GetY()
	{
		return y;
	}
}
