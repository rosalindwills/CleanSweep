package com.se459.modules.interfaces; 

public interface ISensor {
	ICell readCell(int floorLevel, int x, int y);
	void cleanCell(int floorLevel, int x, int y);
	void importXml(String path) throws Exception;
}
