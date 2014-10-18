package com.se459.modules.models;

import com.se459.modules.interfaces.ICell;
import com.se459.modules.interfaces.IFloor;
import com.se459.modules.interfaces.IHomeLayout;
import java.util.ArrayList;
import java.util.List;

public class HomeLayout implements IHomeLayout {
	private List<IFloor> _floors;
	
	public HomeLayout() {
		_floors = new ArrayList<IFloor>();
	}

	public ICell getCell(int floorLevel, int x, int y) {
		IFloor floor = getFloor(floorLevel);
		if (floor == null) {
			return null;
		}
		return floor.getCell(x, y);
	}
	
	public void addCell(int floorLevel, ICell cell) {
		IFloor floor = getFloor(floorLevel);
		floor.addCell(cell);
	}

	public IFloor getFloor(int floorLevel) {
		IFloor floorToSearch = null;
		for(IFloor floor : _floors) {
			if (floor.getLevel() == floorLevel) {
				floorToSearch = floor;
			}
		}
		return floorToSearch;
	}
	
	public List<IFloor> getFloors() {
		return _floors;
	}
	
	public void addFloor(IFloor floor) {
		_floors.add(floor);
	}

	public void cleanCell(int floorLevel, int x, int y) {
		IFloor floor = getFloor(floorLevel);
		floor.cleanCell(x, y);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Floors: " + _floors.size() + "\n\n");
		for(IFloor floor : _floors) {
			builder.append(floor.toString());
		}
		return builder.toString();
	}
}
