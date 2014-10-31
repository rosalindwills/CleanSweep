package com.se459.sensor.interfaces;

import com.se459.sensor.enums.PathType;

public interface ISensor {

	void importXml(String path) throws Exception;

	public PathType getPosXPathType(int floorLevel, int x, int y);

	public PathType getNegXPathType(int floorLevel, int x, int y);

	public PathType getPosYPathType(int floorLevel, int x, int y);

	public PathType getNegYPathType(int floorLevel, int x, int y);
	
	public ICell getPosXCell(int floorLevel, int x, int y);

	public ICell getNegXCell(int floorLevel, int x, int y);

	public ICell getPosYCell(int floorLevel, int x, int y);

	public ICell getNegYCell(int floorLevel, int x, int y);
	
	public ICell getStartPoint(int floorLevel);

}
