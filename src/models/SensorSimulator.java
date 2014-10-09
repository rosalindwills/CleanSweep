package models;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import interfaces.ICell;
import interfaces.IHomeLayout;
import interfaces.ISensorSimulator;

public class SensorSimulator implements ISensorSimulator {

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
	
	public static void main(String args[]) {
		SensorSimulator simulator = new SensorSimulator();
		try {
			simulator.importXml("src\\sampleXml\\homeLayout1.xml");
		    System.out.println(simulator.getHomeLayout().toString());
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
