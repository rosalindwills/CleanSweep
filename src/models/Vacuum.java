package models;

import interfaces.ICell;

import java.io.File;
import java.io.IOException;

import org.xml.sax.SAXException;

public class Vacuum implements Runnable {
	private SensorSimulator sim;
	public volatile boolean on;
	
	private int x, y;
	private int dirtUnits;
	final int dirtCapacity = 50;
	
	// the thread the vacuum is running in
	Thread thread;
	
	public Vacuum(SensorSimulator sensorSimulator, int xPosition, int yPosition)
	{
		x = xPosition;
		y = yPosition;
		dirtUnits = 0;

		sim = sensorSimulator;
	}
	
	public void Start()
	{
		on = true;
		
		thread = new Thread(this);
		thread.start();
	}
	
	public void Stop()
	{
		on = false;
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		while(on)
		{
			ICell currentCell = sim.readCell(1, x, y);
			
			if(currentCell.getDirtUnits() > 0)
			{
				Sweep(currentCell);
			}
		}
	}
	
	private void CheckIfFinishedCleaning()
	{		
		// right now it only sweeps and doesn't move, so if the current space is clean it is finished
		if(sim.readCell(1, x, y).getDirtUnits() <= 0)
		{
			System.out.println("Finished Cleaning");
			Stop();
		}
	}
	
	private void Sweep(ICell cell)
	{
		if(dirtUnits < dirtCapacity)
		{
			cell.cleanCell();
			++dirtUnits;

			System.out.println("Cleaned dirt from cell: " + cell.getX() + ", " + cell.getY() + " current capacity: " + dirtUnits + "/" + dirtCapacity);			
			
			CheckIfFinishedCleaning();
		}
	}
}
