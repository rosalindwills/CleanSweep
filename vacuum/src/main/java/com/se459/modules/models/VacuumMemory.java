package com.se459.modules.models;

import java.util.ArrayList;
import java.util.List;

import com.se459.sensor.interfaces.ICell;
import com.se459.util.log.LogFactory;

public class VacuumMemory {

	private ArrayList<ICell> traveledFinishedCells = new ArrayList<ICell>();
	private ArrayList<ICell> traveledUnfinishedCells = new ArrayList<ICell>();
	private ArrayList<ICell> untraveledCells = new ArrayList<ICell>();

	public void addNewCell(ICell cell) {
		if (!untraveledCells.contains(cell)
				&& !traveledFinishedCells.contains(cell)
				&& !traveledUnfinishedCells.contains(cell)) {
			untraveledCells.add(cell);
		}
	}

	public void becomeTraveled(ICell cell) {
		if (untraveledCells.contains(cell)) {
			untraveledCells.remove(cell);
			traveledUnfinishedCells.add(cell);
		}
	}

	public void becomeFinished(ICell cell) {
		if (this.traveledUnfinishedCells.contains(cell)) {
			traveledUnfinishedCells.remove(cell);
			traveledFinishedCells.add(cell);
		}

	}

	public void addFinishedCells(ICell cell) {
		if(!this.traveledFinishedCells.contains(cell))
		traveledFinishedCells.add(cell);
	}

	public boolean ifKnownButUnfinished(ICell cell) {
		return traveledUnfinishedCells.contains(cell)
				|| untraveledCells.contains(cell);
	}

	public void setFinished(ICell cell) {
		traveledUnfinishedCells.remove(cell);
		traveledFinishedCells.add(cell);
	}

	public List<ICell> getAllKnownButNotTraveldCells() {
		return this.untraveledCells;
	}

	public List<ICell> getAllKnownCells() {
		List<ICell> cells = new ArrayList<ICell>();
		for (ICell c : traveledFinishedCells) {
			if (!cells.contains(c)) {
				cells.add(c);
			}
		}
		for (ICell c : traveledUnfinishedCells) {
			if (!cells.contains(c)) {
				cells.add(c);
			}
		}
		for (ICell c : untraveledCells) {
			if (!cells.contains(c)) {
				cells.add(c);
			}
		}
		return cells;
	}

	public void output() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				boolean flag = false;
				;
				for (int k = 0; k < traveledFinishedCells.size(); k++) {
					if (traveledFinishedCells.get(k).getX() == j
							&& traveledFinishedCells.get(k).getY() == i) {
						sb.append("Fin");
						flag = true;
						break;
					}
				}
				for (int k = 0; k < traveledUnfinishedCells.size(); k++) {
					if (traveledUnfinishedCells.get(k).getX() == j
							&& traveledUnfinishedCells.get(k).getY() == i) {
						sb.append("Unf");
						flag = true;
						break;
					}
				}
				for (int k = 0; k < untraveledCells.size(); k++) {
					if (untraveledCells.get(k).getX() == j
							&& untraveledCells.get(k).getY() == i) {
						sb.append("Knw");
						flag = true;
						break;
					}
				}
				if (!flag) {
					sb.append("Unk");
				}
				sb.append("   ");
			}
			sb.append("\n");
		}

		LogFactory.newFileLog("/Users/wenhaoliu/Desktop/1.txt").append(
				sb.toString());
		LogFactory.newFileLog("/Users/wenhaoliu/Desktop/1.txt").append(
				"---------------------------\n");
	}

}
