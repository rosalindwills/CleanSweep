package com.se459.sensor.interfaces; 

public interface ISensor {
	ICell readCell(int floorLevel, int x, int y);
	void cleanCell(int floorLevel, int x, int y);
	void importXml(String path) throws Exception;
}
