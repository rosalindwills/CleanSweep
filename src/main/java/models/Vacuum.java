package models;

import interfaces.ICell;
import log.Log;
import log.LogFactory;

public class Vacuum implements Runnable {
	private SensorSimulator sim;
	public volatile boolean on;
	Log log = LogFactory.newFileLog();
	
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
		
		log.append("Started the vacuum");		
	}
	
	public void Stop()
	{
		on = false;
		
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


        private Boolean moveRight(){
            return sim.readCell(1,x+1,y);
        }


        private Boolean moveLeft(){
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