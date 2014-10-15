package models;


public class TurnOnOffVacuum {


 public static void main(String[] args){
     
        SensorSimulator sim = new SensorSimulator();
        Vacuum vacuum = new Vacuum(sim,0,0);


        //add logic to determine if vacuum is currently running.
        //if vacuum is not running, start it
        vacuum.Start();

 	//if vacuum is running, stop it
        vacuum.Stop();
        
 } 



}
