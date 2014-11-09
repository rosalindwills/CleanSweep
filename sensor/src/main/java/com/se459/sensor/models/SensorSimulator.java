package com.se459.sensor.models;

import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.IHomeLayout;
import com.se459.sensor.interfaces.ISensor;
import java.io.FileReader;
import java.io.IOException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import java.io.File;

import com.se459.sensor.enums.PathType;

public class SensorSimulator implements ISensor {

	static final public String HOME_LAYOUT_FILE = "sensor/src" + File.separator
			+ "main" + File.separator + "resources" + File.separator
			+ "homeLayout1.xml";

	private IHomeLayout homeLayout;

	static public ISensor getInstance() {
		return new SensorSimulator();
	}

	private SensorSimulator() {

	}

	public IHomeLayout getHomeLayout() {
		return homeLayout;
	}

	private ICell readCell(int floorLevel, int x, int y) {
		return homeLayout.getCell(floorLevel, x, y);
	}

	public void importXml(String path) throws SAXException, IOException {
		XMLReader xr = XMLReaderFactory.createXMLReader();
		HomeLayoutParser handler = new HomeLayoutParser();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		FileReader r = new FileReader(path);
		xr.parse(new InputSource(r));
		homeLayout = handler.getHomeLayout();
	}

	/**
	 * Specify a point and floor, this method returns the PathType in the
	 * positive x direction of that point on that floor.
	 * 
	 * @param floorLevel
	 *            the floor level
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return the PathType in positive x direction
	 */
	public PathType getPosXPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathPosX();
	}

	/**
	 * Specify a point and floor, this method returns the PathType in the
	 * negative x direction of that point on that floor.
	 * 
	 * @param floorLevel
	 *            the floor level
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return the PathType in negative x direction
	 */
	public PathType getNegXPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathNegX();
	}

	/**
	 * Specify a point and floor, this method returns the PathType in the
	 * positive y direction of that point on that floor.
	 * 
	 * @param floorLevel
	 *            the floor level
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return the PathType in positive y direction
	 */
	public PathType getPosYPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathPosY();
	}

	/**
	 * Specify a point and floor, this method returns the PathType in the
	 * negative y direction of that point on that floor.
	 * 
	 * @param floorLevel
	 *            the floor level
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * @return the PathType in negative y direction
	 */
	public PathType getNegYPathType(int floorLevel, int x, int y) {
		return this.readCell(floorLevel, x, y).getPathNegY();
	}
	
	
	public ICell getPosXCell(int floorLevel, int x, int y){
		return this.readCell(floorLevel, x+1, y);
	}

	public ICell getNegXCell(int floorLevel, int x, int y){
		return this.readCell(floorLevel, x-1, y);
	}

	public ICell getPosYCell(int floorLevel, int x, int y){
		return this.readCell(floorLevel, x, y+1);
	}

	public ICell getNegYCell(int floorLevel, int x, int y){
		return this.readCell(floorLevel, x, y-1);
	}
	
	public ICell getStartPoint(int floorLevel){
		return this.readCell(floorLevel, 0, 0);
	}

}
