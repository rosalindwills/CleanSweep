package interfaces;

import enums.PathType;
import enums.SurfaceType;

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