package com.se459.sensor.interfaces;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;

public interface ICell {
	public static final int UNKNOWN_COST = 3;
	public static final int LOWPILE_COST = 2;
	public static final int HIGHPILE_COST = 3;
	public static final int BAREFLOOR_COST = 1;
	int getX();
	int getY();
	SurfaceType getSurfaceType();
	boolean isClean();
	PathType getPathPosX();
	PathType getPathNegX();
	PathType getPathPosY();
	PathType getPathNegY();
	boolean getIsChargingStation();
	int getTraverseCost();
	int getVacuumCost();
	void cleanCell();
	void setTraversed();
	boolean isTraversed();
	
	// test only
	int getDirtUnits();
}
