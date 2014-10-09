package interfaces;

import org.xml.sax.SAXException;

import models.Cell;

public interface ISensorSimulator {
	ICell readCell(int floorLevel, int x, int y);
	void cleanCell(int floorLevel, int x, int y);
	void importXml(String path) throws Exception;
}
