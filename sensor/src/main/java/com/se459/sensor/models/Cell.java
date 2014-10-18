package com.se459.sensor.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;

public class Cell implements ICell {
	private int _x;
	private int _y;
	private SurfaceType _surfaceType;
	private int _dirtUnits;
	private PathType _pathPosX;
	private PathType _pathNegX;
	private PathType _pathPosY;
	private PathType _pathNegY;
	private boolean _isChargingStation;
	
	public Cell(int x, int y, SurfaceType surfaceType, int dirtUnits, PathType pathPosX, PathType pathNegX, PathType pathPosY, PathType pathNegY, boolean isChargingStation) {
		_x = x;
		_y = y;
		_surfaceType = surfaceType;
		_dirtUnits = dirtUnits;
		_pathPosX = pathPosX;
		_pathNegX = pathNegX;
		_pathPosY = pathPosY;
		_pathNegY = pathNegY;
		_isChargingStation = isChargingStation;
	}

	public int getX() {
		return _x;
	}

	public int getY() {
		return _y;
	}
	
	public SurfaceType getSurfaceType() {
		return _surfaceType;
	}
	
	public int getDirtUnits() {
		return _dirtUnits;
	}
	
	public PathType getPathPosX() {
		return _pathPosX;
	}
	
	public PathType getPathNegX() {
		return _pathNegX;
	}
	
	public PathType getPathPosY() {
		return _pathPosY;
	}
	
	public PathType getPathNegY() {
		return _pathNegY;
	}
	
	public boolean getIsChargingStation() {
		return _isChargingStation;
	}
	
	public void cleanCell() {
		_dirtUnits--;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("     Cell " + _x + ", " + _y + ": \n");
		builder.append("          Surface Type: " + _surfaceType.toString());
		builder.append("          Dirt: " + _dirtUnits + " units\n");
		builder.append("          Positive X Path: " + _pathPosX.toString() + "\n");
		builder.append("          Negative X Path: " + _pathNegX.toString() + "\n");
		builder.append("          Positive Y Path: " + _pathPosY.toString() + "\n");
		builder.append("          Negative Y Path: " + _pathNegY.toString() + "\n");
		return builder.toString();
	}
}
