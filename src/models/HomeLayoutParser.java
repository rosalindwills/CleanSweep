package models;

import interfaces.ICell;
import interfaces.IFloor;
import interfaces.IHomeLayout;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import enums.PathType;
import enums.SurfaceType;

public class HomeLayoutParser extends DefaultHandler {

	private HomeLayout _homeLayout = new HomeLayout();
	private int _currentFloor = -1;
	
    public HomeLayoutParser() {
    	super();
    }

    public void startElement (String uri, String name,
		      String qName, Attributes atts)
	{
		if (name.equals("floor")) {
			int level = Integer.parseInt(atts.getValue("level"));
			IFloor floor = new Floor(level);
			_homeLayout.addFloor(floor);
			_currentFloor = level;
		}
		if (name.equals("cell")) {
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
			_homeLayout.addCell(_currentFloor, cell);
		}
	}
    
    public IHomeLayout getHomeLayout() {
    	return _homeLayout;
    }
}