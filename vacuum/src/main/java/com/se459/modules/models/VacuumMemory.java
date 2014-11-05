package com.se459.modules.models;

import java.util.ArrayList;
import java.util.List;

import com.se459.sensor.interfaces.ICell;
import com.se459.util.log.LogFactory;

public class VacuumMemory {

	private ArrayList<ICell> finishedCells = new ArrayList<ICell>();
	private ArrayList<ICell> unfinishedCells = new ArrayList<ICell>();
	
	public static final int UNKNOWN = 0;
	public static final int  UNFINISHED = 1;
	public static final int FINISHED =2;

	public void addNewCell(ICell cell) {
		if (!unfinishedCells.contains(cell)
				&& !finishedCells.contains(cell)) {
				
			unfinishedCells.add(cell);
		}
	}
	
	public boolean ifUnfinished(ICell cell){
		return !finishedCells.contains(cell);
	}

	public void becomeFinished(ICell cell) {
		if (this.unfinishedCells.contains(cell)) {
			unfinishedCells.remove(cell);
			finishedCells.add(cell);
		}

	}

	public void addFinishedCells(ICell cell) {
		if(!this.finishedCells.contains(cell))
		finishedCells.add(cell);
	}


	public List<ICell> getAllUnfinishedCells() {
		return this.unfinishedCells;
	}
	

	public List<ICell> getAllKnownCells() {
		List<ICell> cells = new ArrayList<ICell>();
		for (ICell c : finishedCells) {
			if (!cells.contains(c)) {
				cells.add(c);
			}
		}
		for (ICell c : unfinishedCells) {
			if (!cells.contains(c)) {
				cells.add(c);
			}
		}
		return cells;
	}
	
	public List<ICell> getAllFinishedCells() {
		return this.finishedCells;
	}

	public void output() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				boolean flag = false;
				;
				for (int k = 0; k < finishedCells.size(); k++) {
					if (finishedCells.get(k).getX() == j
							&& finishedCells.get(k).getY() == i) {
						sb.append("Fin");
						flag = true;
						break;
					}
				}
				for (int k = 0; k < unfinishedCells.size(); k++) {
					if (unfinishedCells.get(k).getX() == j
							&& unfinishedCells.get(k).getY() == i) {
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

	}
	
	

}
