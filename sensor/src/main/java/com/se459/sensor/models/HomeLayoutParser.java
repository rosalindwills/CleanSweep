package com.se459.sensor.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IFloor;
import com.se459.sensor.interfaces.IHomeLayout;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;

public class HomeLayoutParser extends DefaultHandler {

	private HomeLayout homeLayout = new HomeLayout();
	private int currentFloor = -1;
	
    public HomeLayoutParser() {
    	super();
    }

    public void startElement (String uri, String name, String qName, Attributes atts) {
		if ("floor".equals(name)) {
			int level = Integer.parseInt(atts.getValue("level"));
			IFloor floor = new Floor(level);
			homeLayout.addFloor(floor);
			currentFloor = level;
		}
		if ("cell".equals(name)) {
			int x = Integer.parseInt(atts.getValue("xs"));
			int y = Integer.parseInt(atts.getValue("ys"));
			SurfaceType surfaceType = SurfaceType.byValue(Integer.parseInt(atts.getValue("ss")));
			int dirtUnits = Integer.parseInt(atts.getValue("ds"));
			String pathInfo = atts.getValue("ps");
			int intPosX = Integer.parseInt(pathInfo.substring(0, 1));
			int intNegX = Integer.parseInt(pathInfo.substring(1, 2));
			int intPosY = Integer.parseInt(pathInfo.substring(2, 3));
			int intNegY = Integer.parseInt(pathInfo.substring(3, 4));
			PathType pathPosX = PathType.byValue(intPosX);
			PathType pathNegX = PathType.byValue(intNegX);
			PathType pathPosY = PathType.byValue(intPosY);
			PathType pathNegY = PathType.byValue(intNegY);
			boolean isChargingStation = Integer.parseInt(atts.getValue("cs")) == 1 ? true : false;
			ICell cell = new Cell(x, y, surfaceType, dirtUnits, pathPosX, pathNegX, pathPosY, pathNegY, isChargingStation);
			homeLayout.addCell(currentFloor, cell);
		}
	}
    
    public IHomeLayout getHomeLayout() {
    	return homeLayout;
    }
}
