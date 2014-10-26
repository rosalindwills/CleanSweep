package com.se459.modules.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

import java.util.ArrayList;

public class Vacuum implements Runnable {
	private ISensor sim;
        private ArrayList<ICell> storePrevLocation = new ArrayList<ICell>();
	public volatile boolean on;
	Log log = LogFactory.newFileLog();
        Log scnLog = LogFactory.newScreenLog();
        
	
	public int x, y;
	private int dirtUnits;
	final int dirtCapacity = 50;
	
	int xDir, yDir;
	
	// the thread the vacuum is running in
	Thread thread;

        static public Vacuum getInstance(ISensor sensor,int xPos, int yPos){ 
              return new Vacuum(sensor,xPos,yPos);
        }
	
	private Vacuum(ISensor sensorSimulator, int xPosition, int yPosition)
	{
		x = xPosition;
		y = yPosition;
		
		// start moving down
		xDir = 0;
		yDir = -1;
		
		dirtUnits = 0;

		sim = sensorSimulator;
	}
	
	public void Start()
	{
		on = true;
		
                scnLog.append("Start Vacuum...");
		thread = new Thread(this);
		thread.start();
		
		log.append("Started the vacuum");		
	}
	
	public void Stop()
	{
		on = false;
		
                scnLog.append("Stop Vacuum...");
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		log.append("Stopped the vacuum");
	}
	
	public void run()
	{
		while(on)
		{
			ICell currentCell = sim.readCell(1, x, y);

            storePrevLocation.add(currentCell);
			
			if(currentCell.getDirtUnits() > 0)
			{
				Sweep(currentCell);
			}
			else if(null != getSensorCellInDirection())
			{
				moveForward();
			}
			else
			{
				turn();
			}
			
            try 
            {
				Thread.sleep(500);
			} 
            catch (InterruptedException e) 
            {
				e.printStackTrace();
			}
		}
	}

	private ICell getSensorCellInDirection()
	{
		return sim.readCell(1, x + xDir, y + yDir);
	}
	
	private void moveForward()
	{
		x += xDir;
		y += yDir;
		
		scnLog.append("Moving forward, new location: " + x + ", " + y);			
	}
	
	private void turn()
	{
		int prevXDir = xDir;
		
		xDir = -yDir;
		yDir = prevXDir;

		scnLog.append("Turned, new direction: " + xDir + ", " + yDir);		
	}
	
	private void CheckIfFinishedCleaning()
	{		

	}
	
	private void Sweep(ICell cell)
	{
		if(dirtUnits < dirtCapacity)
		{
			cell.cleanCell();
			++dirtUnits;

			scnLog.append("Cleaned dirt from cell: " + cell.getX() + ", " + cell.getY() + " current capacity: " + dirtUnits + "/" + dirtCapacity);			
			
			CheckIfFinishedCleaning();
		}
	}
}
