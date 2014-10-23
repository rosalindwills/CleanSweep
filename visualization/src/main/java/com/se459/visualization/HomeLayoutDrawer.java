package com.se459.visualization;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IFloor;
import com.se459.sensor.interfaces.IHomeLayout;
import com.se459.sensor.models.HomeLayoutParser;

class HomeLayoutPanel extends JPanel {

	IFloor floor;
	
	public HomeLayoutPanel(IFloor Floor)
	{		
		floor = Floor;
	}
	
    private void draw(Graphics g) 
    {
        Graphics2D g2d = (Graphics2D) g;
        
        Dimension dimension = getSize();
        
        int xMult = dimension.width / (floor.getMaxX() - floor.getMinX());
        int yMult = dimension.height / (floor.getMaxY() - floor.getMinY());
        
        for(int x = floor.getMinX(); x < floor.getMaxX(); ++x)
        {
        	for(int y = floor.getMinY(); y < floor.getMaxY(); ++y)
        	{
        		ICell cell = floor.getCell(x, y);

        		if(null != cell)
        		{        		
        			g2d.setColor(getSurfaceColor(cell.getSurfaceType(), cell.getDirtUnits()));
        			
        			Rectangle2D rect = new Rectangle2D.Float(x * xMult, y * yMult, xMult, yMult);
        			
        			g2d.fill(rect);
        			
        			g2d.setColor(Color.black);
        			g2d.draw(rect);
        			
        			if(cell.getIsChargingStation())
        			{
        				g2d.drawOval(x * xMult, y * yMult, xMult, yMult);
        			}
        		}
        	}
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
    	
    	r *= Math.pow(0.9, dirtUnits);
    	g *= Math.pow(0.9, dirtUnits);
    	b *= Math.pow(0.9, dirtUnits);    	

    	Color color = new Color(r, g, b, 1.0f);    	
    	return color;
    }
    
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);
    }
}

public class HomeLayoutDrawer extends JFrame {

	IHomeLayout homeLayout;
	
    public HomeLayoutDrawer(IHomeLayout layout) 
    {
    	homeLayout = layout;
    	
        initUI();
    }

    private void initUI() 
    {
        setTitle("CleanSweep");

        add(new HomeLayoutPanel(homeLayout.getFloor(1)));

        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) 
    {
		try {
			XMLReader xr = XMLReaderFactory.createXMLReader();
			HomeLayoutParser handler = new HomeLayoutParser();
			xr.setContentHandler(handler);
			xr.setErrorHandler(handler);
			try {
				FileReader r = new FileReader("src"+ File.separator + "resources"+ File.separator + "homeLayout1.xml");
			    try {
					xr.parse(new InputSource(r));
					final IHomeLayout _homeLayout = handler.getHomeLayout();
				    
				    SwingUtilities.invokeLater(new Runnable() 
			        {
			            public void run() {

			                HomeLayoutDrawer sk = new HomeLayoutDrawer(_homeLayout);
			                sk.setVisible(true);
			            }
			        });
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		}
    }
}