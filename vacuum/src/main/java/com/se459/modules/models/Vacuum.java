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
        
	
	private int x, y;
	private int dirtUnits;
	final int dirtCapacity = 50;
	
	// the thread the vacuum is running in
	Thread thread;

        static public Vacuum getInstance(ISensor sensor,int xPos, int yPos){ 
              return new Vacuum(sensor,xPos,yPos);
        }
	
	private Vacuum(ISensor sensorSimulator, int xPosition, int yPosition)
	{
		x = xPosition;
		y = yPosition;
		dirtUnits = 0;

                scnLog.append("In vacuum constructor");
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
			ICell currentCell = move(); 
			
			if(currentCell.getDirtUnits() > 0)
			{
				Sweep(currentCell);
			}

                        storePrevLocation.add(currentCell);
		}
	}

        private ICell move(){

            //add navigation logic  
            return moveForward(); 
        }


        private ICell moveForward(){
	    return sim.readCell(1, x, y+1);
        }

        private ICell moveBackward(){
            return sim.readCell(1,x,y-1); 
        }


        private ICell moveRight(){
            return sim.readCell(1,x+1,y);
        }

        private ICell moveLeft(){
            return sim.readCell(1,x-1,y);
        }
	
	private void CheckIfFinishedCleaning()
	{		
		// right now it only sweeps and doesn't move, so if the current space is clean it is finished
		if(sim.readCell(1, x, y).getDirtUnits() <= 0)
		{
			log.append("Finished Cleaning");
			Stop();
		}
	}
	
	private void Sweep(ICell cell)
	{
		if(dirtUnits < dirtCapacity)
		{
			cell.cleanCell();
			++dirtUnits;

			log.append("Cleaned dirt from cell: " + cell.getX() + ", " + cell.getY() + " current capacity: " + dirtUnits + "/" + dirtCapacity);			
			
			CheckIfFinishedCleaning();
		}
	}
}
