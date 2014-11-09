package com.se459.sensor.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IFloor;
import com.se459.sensor.interfaces.IHomeLayout;
import java.util.ArrayList;
import java.util.List;

public class HomeLayout implements IHomeLayout {
	private List<IFloor> floors;
	
	public HomeLayout() {
		floors = new ArrayList<IFloor>();
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
		for(IFloor floor : floors) {
			if (floor.getLevel() == floorLevel) {
				floorToSearch = floor;
			}
		}
		return floorToSearch;
	}
	
	public List<IFloor> getFloors() {
		return floors;
	}
	
	public void addFloor(IFloor floor) {
		floors.add(floor);
	}

	public void cleanCell(int floorLevel, int x, int y) {
		IFloor floor = getFloor(floorLevel);
		floor.cleanCell(x, y);
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Floors: " + floors.size() + "\n\n");
		for(IFloor floor : floors) {
			builder.append(floor.toString());
		}
		return builder.toString();
	}
}
