package com.se459.modules.interfaces;

import java.util.List;

public interface IHomeLayout {
	ICell getCell(int floorLevel, int x, int y);
	IFloor getFloor(int floorLevel);
	List<IFloor> getFloors();
	void addFloor(IFloor floor);
	void cleanCell(int floorLevel, int x, int y);
}
