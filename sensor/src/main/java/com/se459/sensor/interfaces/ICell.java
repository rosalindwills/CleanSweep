package com.se459.sensor.interfaces;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;

public interface ICell {
	int getX();
	int getY();
	SurfaceType getSurfaceType();
	int getDirtUnits();
	PathType getPathPosX();
	PathType getPathNegX();
	PathType getPathPosY();
	PathType getPathNegY();
	boolean getIsChargingStation();
	
	void cleanCell();
}
