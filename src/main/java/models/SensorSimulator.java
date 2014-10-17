package models;

import interfaces.ICell;
import interfaces.IDetect;
import interfaces.IHomeLayout;
import interfaces.ISensorSimulator;
import java.io.FileReader;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import enums.PathType;

public class SensorSimulator implements ISensorSimulator, IDetect {

	private IHomeLayout _homeLayout;
	
	public IHomeLayout getHomeLayout() {
		return _homeLayout;
	}
	
	public ICell readCell(int floorLevel, int x, int y) {
		ICell cell = _homeLayout.getCell(floorLevel, x, y);
		return cell;
	}

	public void cleanCell(int floorLevel, int x, int y) {
		_homeLayout.cleanCell(floorLevel, x, y);
	}

	public void importXml(String path) throws SAXException, IOException {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		HomeLayoutParser handler = new HomeLayoutParser();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
	    FileReader r = new FileReader(path);
	    xr.parse(new InputSource(r));
	    _homeLayout = handler.getHomeLayout();
	}
	
	/** Specify a point and floor, this method returns the PathType 
	 * in the positive x direction of that point on that floor. 
	 * 
	 * @param floorLevel			the floor level
	 * @param x			x coordinate
	 * @param y			y coordinate
	 * @return the PathType in positive x direction
	 */
	public PathType getPosXPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathPosX();
	}

	
	/** Specify a point and floor, this method returns the PathType 
	 * in the negative x direction of that point on that floor. 
	 * 
	 * @param floorLevel			the floor level
	 * @param x			x coordinate
	 * @param y			y coordinate
	 * @return the PathType in negative x direction
	 */
	public PathType getNegXPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathNegX();
	}

	/** Specify a point and floor, this method returns the PathType 
	 * in the positive y direction of that point on that floor. 
	 * 
	 * @param floorLevel			the floor level
	 * @param x			x coordinate
	 * @param y			y coordinate
	 * @return the PathType in positive y direction
	 */
	public PathType getPosYPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathPosY();
	}

	
	/** Specify a point and floor, this method returns the PathType 
	 * in the negative y direction of that point on that floor. 
	 * 
	 * @param floorLevel			the floor level
	 * @param x			x coordinate
	 * @param y			y coordinate
	 * @return the PathType in negative y direction
	 */
	public PathType getNegYPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathNegY();
	}

	

}