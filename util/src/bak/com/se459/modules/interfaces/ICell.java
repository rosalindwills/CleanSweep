package com.se459.modules.interfaces;

import com.se459.modules.enums.PathType;
import com.se459.modules.enums.SurfaceType;

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
