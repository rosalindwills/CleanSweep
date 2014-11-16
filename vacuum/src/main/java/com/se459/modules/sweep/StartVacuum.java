package com.se459.modules.sweep;

import java.io.*;

import com.se459.sensor.models.SensorSimulator;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.LogFactory;
import com.se459.modules.models.SweepLog;
import com.se459.modules.models.Vacuum;

import java.util.Scanner;

import org.xml.sax.SAXException;

import java.lang.Thread;

import static java.lang.System.*;


public class StartVacuum {

	private StartVacuum() {
	}
	
	public static void main(String[] args){
        ISensor sim = SensorSimulator.getInstance();
        Vacuum vacuum;

        try {
        	((SensorSimulator) sim).importXml("classes" + File.separator
					+ "homeLayout1.xml");

			vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newFileAndScreenLog()));
            Scanner in = new Scanner(System.in);
            String line = null;


            if (vacuum.isVacuumOn() == true){
                System.out.println("Enter 'S' to stop vacuum or 'C' to exit");
            }
            else{
                System.out.println("Enter 'R' to start vacuum or 'C' to exit");
            }
  
            while((line = in.next()) != null){
              if (line != null && line.length() > 0){
                 char letter = line.toUpperCase().charAt(0);
              
                 if (letter == 'C'){
                    System.exit(0);
                 }
                
                 if (letter == 'R'){
                    if (vacuum.isVacuumOn() == false){
                       System.out.println("Starting vacuum...");
                       vacuum.start();
                    }
                    else {
                       System.out.println("Vacuum is currently running");
                    }
                 }

                 if (letter == 'S'){
                    if (vacuum.isVacuumOn() == true){
                       System.out.println("Stopping vacuum...");
                       vacuum.stop();
                       Thread.sleep(5000);
                    }
                    else {
                       System.out.println("Vacuum is currently not running");
                    }
                 }

                 if (vacuum.isVacuumOn() == true){
                    System.out.println("Enter 'S' to stop vacuum or 'C' to exit");
                 }
                 else{
                    System.out.println("Enter 'R' to start vacuum or 'C' to exit");
                 }
  
                 
              }
             
            }
        
        } catch (SAXException e) {
        	throw new RuntimeException(e);
        } catch (IOException e) {
        	throw new RuntimeException(e);
         }catch (Exception e) {
        	throw new RuntimeException(e);
        }
	} 
}
