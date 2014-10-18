package com.se459.modules.sweep;

import java.io.IOException;
import com.se459.modules.models.SensorSimulator;
import com.se459.modules.models.Vacuum;
import com.se459.modules.interfaces.ISensor;
import org.xml.sax.SAXException;
import java.lang.Thread;


public class StartVacuum {

 public static void main(String[] args){
        ISensor sim;
        Vacuum vacuum;

        try {
            sim = SensorSimulator.getInstance();;
            sim.importXml(SensorSimulator.HOME_LAYOUT_FILE);
            vacuum = Vacuum.getInstance(sim,0,0);

            //add logic to determine if vacuum is currently running.
            //if vacuum is not running, start it
            vacuum.Start();
 
            Thread.sleep(5000);

 	   //if vacuum is running, stop it
           vacuum.Stop();
        
        }catch(SAXException ex){
           ex.printStackTrace(); 
        }catch(IOException ex){
           ex.printStackTrace();
        }catch(Exception ex){
           ex.printStackTrace();
        }
 } 
}
