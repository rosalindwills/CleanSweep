package com.se459.sensor.interfaces;

public interface IFloor {
	ICell getCell(int x, int y);
	int getLevel();
	void addCell(ICell cell);
	void cleanCell(int x, int y);
	int getMinX();
	int getMaxX();
	int getMinY();
	int getMaxY();
}
