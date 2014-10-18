package com.se459.modules.sweep;

import java.io.File;
import java.io.IOException;
import com.se459.sensor.models.SensorSimulator;
import com.se459.sensor.interfaces.ISensor;

import org.xml.sax.SAXException;

public class App 
{
    public static void main( String[] args )
    {
        ISensor simulator = SensorSimulator.getInstance();
		try {
			simulator.importXml("src"+ File.separator + "main"+ File.separator + "resources" + File.separator +"homeLayout1.xml");
		    System.out.println(((SensorSimulator)simulator).getHomeLayout().toString());
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception ex){
                        ex.printStackTrace();
                }
    }
}
