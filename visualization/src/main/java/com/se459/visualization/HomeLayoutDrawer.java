package main.java.com.se459.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.xml.sax.SAXException;

import com.se459.modules.models.Vacuum;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IFloor;
import com.se459.sensor.interfaces.IHomeLayout;
import com.se459.sensor.interfaces.ISensor;
import com.se459.sensor.models.SensorSimulator;

class HomeLayoutPanel extends JPanel {

	IFloor floor;
	Vacuum vacuum;
	
	public HomeLayoutPanel(IFloor Floor, Vacuum vac)
	{		
		floor = Floor;
		vacuum = vac;
	}
	
    private void draw(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        
        Dimension dimension = getSize();
        
        int xMult = dimension.width / (floor.getMaxX() - floor.getMinX() + 1);
        int yMult = dimension.height / (floor.getMaxY() - floor.getMinY() + 1);
        
        for(int x = floor.getMinX(); x <= floor.getMaxX(); ++x)
        {
        	for(int y = floor.getMinY(); y <= floor.getMaxY(); ++y)
        	{
        		ICell cell = floor.getCell(x, y);

        		if(null != cell)
        		{
        			g2d.setColor(getSurfaceColor(cell.getSurfaceType(), cell.getDirtUnits()));
        			
        			Rectangle2D rect = new Rectangle2D.Float(x * xMult, y * yMult , xMult, yMult);
        			
        			g2d.fill(rect);
        			
        			g2d.setColor(Color.black);
        			g2d.draw(rect);
        			
        			if(cell.getIsChargingStation())
        			{
        				g2d.drawOval(x * xMult, y * yMult, xMult, yMult);
        			}
        		}
        	}

			g2d.setColor(Color.green);
        	g2d.drawOval(vacuum.GetDestinationX() * xMult + (int)(xMult / 2.6), vacuum.GetDestinationY() * yMult + (int)(yMult / 2.6), xMult / 4, yMult / 4);
        	
        	// draw the vacuum
			g2d.setColor(Color.black);
        	g2d.drawOval(vacuum.GetX() * xMult + xMult / 4, vacuum.GetY() * yMult + yMult / 4, xMult / 2, yMult / 2);
        }
        
    	for(ICell c : vacuum.returnPath){
    		g2d.setColor(Color.red);
    		g2d.drawOval(c.getX() * xMult + xMult * 4 / 10, c.getY() * yMult + yMult *4 / 10, xMult/5, yMult/5);
    		
    	}
    }

    private Color getSurfaceColor(SurfaceType type, int dirtUnits)
    {
    	float r = 0;
    	float g = 0;
    	float b = 0;
    	
    	switch(type)
    	{
    		case BAREFLOOR:
    			r = 1.0f;
    			g = 0.5f;
				b = 0.25f;
    			break;
    		case LOWPILE:
    			r = 0.25f;
    			g = 0.5f;
				b = 1.0f;
    			break;
    		case HIGHPILE:
    			r = 1.0f;
    			g = 0.3f;
				b = 0.5f;
    			break;
			default:
				break;    				
    	}
    	
    	r *= Math.pow(0.8, dirtUnits);
    	g *= Math.pow(0.8, dirtUnits);
    	b *= Math.pow(0.8, dirtUnits);    	

    	Color color = new Color(r, g, b, 1.0f);    	
    	return color;
    }
    
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }
}

public class HomeLayoutDrawer extends JFrame implements Runnable {
	
	static boolean windowOpen = true;
	HomeLayoutPanel layoutPanel;
	JPanel statusPanel;
	static Thread thread;
	static int maximumWindowlWidth = 600;
	static int statusPanelHeight = 50;
	static int maximumLayoutPanelHeight = 500;
	static int maximumCellSize = 100;
	static int minimumCellSize = 5;
	
	static int padding = 50;
	
	
    public HomeLayoutDrawer(IHomeLayout layout, Vacuum vacuum) 
    {    	
        initUI(layout, vacuum);
    }

    private void initUI(IHomeLayout layout, Vacuum vacuum) 
    {
        setTitle("CleanSweep");
        
        int rows = layout.getFloor(1).getMaxX() -layout.getFloor(1).getMinX() + 1;
        int cols = layout.getFloor(1).getMaxY() - layout.getFloor(1).getMinY() + 1;
        
     // given a maximum size of window, call getCellSize to calculate
     // a proper size for cell based on floor plan.
        int actualCellSize = calculateCellSize(rows, cols, maximumWindowlWidth, 
        		maximumLayoutPanelHeight, maximumCellSize, minimumCellSize); 
        int actualLayoutPanelWidth = actualCellSize * rows;
        int actualLayoutPanelHeight = actualCellSize * cols;   
        setSize(actualLayoutPanelWidth + padding, actualLayoutPanelHeight + statusPanelHeight + padding);
        
        FlowLayout UILayout = new FlowLayout();
        UILayout.setHgap(0);
        UILayout.setVgap(0);
        setLayout(UILayout);
      
        
        statusPanel = new JPanel();
        statusPanel.setPreferredSize(new Dimension(actualLayoutPanelWidth,statusPanelHeight));
        
        layoutPanel = new HomeLayoutPanel(layout.getFloor(1), vacuum);
        layoutPanel.setPreferredSize(new Dimension(actualLayoutPanelWidth,actualLayoutPanelHeight));
        
        add(statusPanel);
        add(layoutPanel);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) 
    {
		final ISensor sim = SensorSimulator.getInstance();
		
		try 
		{
			((SensorSimulator)sim).importXml("classes"+ File.separator + "homeLayout1.xml");
			
			final IHomeLayout _homeLayout = ((SensorSimulator) sim).getHomeLayout();
		    
		    SwingUtilities.invokeLater(new Runnable() 
	        {
	            public void run() {

	        		ICell chargingCell = GetChargingStationLocation(_homeLayout.getFloor(1));
	        		
	        		Vacuum vacuum = Vacuum.getInstance(sim, chargingCell.getX(), chargingCell.getY());
	        		vacuum.Start();
	        		
	                HomeLayoutDrawer sk = new HomeLayoutDrawer(_homeLayout, vacuum);               
	                sk.setVisible(true);
	                
	                sk.addWindowListener(new java.awt.event.WindowAdapter() {
	                    @Override
	                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
	                    	HomeLayoutDrawer.windowOpen = false;
	                    }
	                });
	                
	                thread = new Thread(sk);
	                thread.start();
	            }
	        });
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
    }

	private static ICell GetChargingStationLocation(IFloor floor)
	{
		for(int x = floor.getMinX(); x < floor.getMaxX(); ++x)
	    {
	    	for(int y = floor.getMinY(); y < floor.getMaxY(); ++y)
	    	{
	    		if(floor.getCell(x, y).getIsChargingStation())
	    		{
	    			return floor.getCell(x,y);
	    		}
	    	}
	    }
		
		return null;
	}
	
	public void run() {

		while(windowOpen)
		{
			statusPanel.removeAll();
			String locationStr = "(" + layoutPanel.vacuum.GetX()+", "+layoutPanel.vacuum.GetY()+")";
			String dirtStatusStr = "DirtUnits: " + layoutPanel.vacuum.getDirtUnits();
			String chargeStatusStr = "ChargeRemaining: " + layoutPanel.vacuum.getChargeRemaining();
			String returnPathNum = "#ReturnPath: " + layoutPanel.vacuum.currentReturnPathNum;
			String pathCost = "ReturnCost: " + layoutPanel.vacuum.returnCost;
			String dispaly = dirtStatusStr + "    " + chargeStatusStr + "    " +pathCost + "    "+ returnPathNum;

			statusPanel.add(new JLabel(dispaly));
			statusPanel.validate();
			layoutPanel.repaint();
			repaint();
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private int calculateCellSize(int cols, int rows, int maximumWidth, int maximumHeight, int maximumCellSize, int minimumCellSize){
        
		for(int cellSizeCandidate = maximumCellSize; cellSizeCandidate > minimumCellSize; cellSizeCandidate--){
        	if(cols * cellSizeCandidate <= maximumWidth && rows * cellSizeCandidate <= maximumHeight){
        		return cellSizeCandidate;
        	}
        }
        
        return minimumCellSize;
	}
}