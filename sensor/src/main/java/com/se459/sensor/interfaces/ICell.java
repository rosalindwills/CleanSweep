package com.se459.sensor.interfaces;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;

public interface ICell {
	public static final double UNKNOWN_COST = 3;
	public static final double LOWPILE_COST = 2;
	public static final double HIGHPILE_COST = 3;
	public static final double BAREFLOOR_COST = 1;
	int getX();
	int getY();
	SurfaceType getSurfaceType();
	boolean isClean();
	PathType getPathPosX();
	PathType getPathNegX();
	PathType getPathPosY();
	PathType getPathNegY();
	boolean getIsChargingStation();
	double getTraverseCost();
	double getVacuumCost();
	void cleanCell();
	void setTraversed();
	boolean isTraversed();
	
	// test only
	int getDirtUnits();
}
