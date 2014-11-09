package com.se459.sensor.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;

public class Cell implements ICell {
	private volatile boolean traversed;
	private int x;
	private int y;
	private SurfaceType surfaceType;
	private int dirtUnits;
	private PathType pathPosX;
	private PathType pathNegX;
	private PathType pathPosY;
	private PathType pathNegY;
	private boolean isChargingStation;

	public Cell(int passedX, int passedY, SurfaceType passedSurfaceType, int passedDirtUnits,
			PathType passedPathPosX, PathType passedPathNegX, PathType passedPathPosY,
			PathType passedPathNegY, boolean passedIsChargingStation) {
		x = passedX;
		y = passedY;
		surfaceType = passedSurfaceType;
		dirtUnits = passedDirtUnits;
		pathPosX = passedPathPosX;
		pathNegX = passedPathNegX;
		pathPosY = passedPathPosY;
		pathNegY = passedPathNegY;
		isChargingStation = passedIsChargingStation;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public SurfaceType getSurfaceType() {
		return surfaceType;
	}

	public PathType getPathPosX() {
		return pathPosX;
	}

	public PathType getPathNegX() {
		return pathNegX;
	}

	public PathType getPathPosY() {
		return pathPosY;
	}

	public PathType getPathNegY() {
		return pathNegY;
	}

	public boolean getIsChargingStation() {
		return isChargingStation;
	}

	public void cleanCell() {
		dirtUnits--;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("     Cell " + x + ", " + y + ": \n");
		builder.append("          Surface Type: " + surfaceType.toString());
		builder.append("          Dirt: " + dirtUnits + " units\n");
		builder.append("          Positive X Path: " + pathPosX.toString()
				+ "\n");
		builder.append("          Negative X Path: " + pathNegX.toString()
				+ "\n");
		builder.append("          Positive Y Path: " + pathPosY.toString()
				+ "\n");
		builder.append("          Negative Y Path: " + pathNegY.toString()
				+ "\n");
		return builder.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Cell)) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		Cell cell = (Cell) obj;
		return cell.x == this.x && cell.y == this.y;
	}
	
	@Override
	public int hashCode() {
		int hash = 17;
        hash = hash * 23 + x;
        hash = hash * 23 + y;
        return hash;
	}

	@Override
	public double getTraverseCost() {
		if (this.traversed) {
			switch (this.getSurfaceType()) {
			case BAREFLOOR:
				return ICell.BAREFLOOR_COST;
			case LOWPILE:
				return ICell.LOWPILE_COST;
			case HIGHPILE:
				return ICell.HIGHPILE_COST;
			default:
				return ICell.UNKNOWN_COST;
			}
		} else {
			return ICell.UNKNOWN_COST;
		}

	}

	@Override
	public double getVacuumCost() {
		if (this.traversed) {
			switch (this.getSurfaceType()) {
			case BAREFLOOR:
				return ICell.BAREFLOOR_COST;
			case LOWPILE:
				return ICell.LOWPILE_COST;
			case HIGHPILE:
				return ICell.HIGHPILE_COST;
			default:
				return ICell.UNKNOWN_COST;
			}
		} else {
			// assume no cost to vacuum, don't want to not visit a cell because we assumed the vacuum
			// cost is too high, can always not vacuum once we are in the cell if the cost is too high
			return 0;
		}
	}

	@Override
	public void setTraversed() {
		this.traversed = true;

	}

	@Override
	public boolean isTraversed() {
		return this.traversed;
	}

	@Override
	public boolean isClean() {
		return this.dirtUnits == 0;
	}

	@Override
	public int getDirtUnits() {
		return this.dirtUnits;
	}
}
