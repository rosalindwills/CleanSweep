package com.se459.modules.models;

import java.util.ArrayList;
import java.util.List;

import com.se459.sensor.interfaces.ICell;
import com.se459.util.log.LogFactory;

public class VacuumMemory {

	// known and traversed and clean 
	private ArrayList<ICell> finishedCells = new ArrayList<ICell>();
	// either known but not traversed OR known, traversed but not clean
	private ArrayList<ICell> unfinishedCells = new ArrayList<ICell>();

	// when the sweep goes to a new cell, it looks around and remembers new cells.
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
	

	// all known cells: 1. known but not traversed.  2 known, traversed but not clean
	// 3. known, traversed and clean
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
	
	public void setTraversed(ICell cell){
		for (ICell c : finishedCells) {
			if (c.equals(cell)) {
				c.setTraversed();
			}
		}
		for (ICell c : unfinishedCells) {
			if (c.equals(cell)) {
				c.setTraversed();
			}
		}
	}
	
	// cells that are known, traversed and clean.
	public List<ICell> getAllFinishedCells() {
		return this.finishedCells;
	}

}
