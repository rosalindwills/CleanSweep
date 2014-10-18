package com.se459.sensor.interfaces;

import com.se459.sensor.enums.PathType;


/** This IDetect interface for detecting obstacles. In our system, SensorSimulator class,
 *  which wants to be able to detect obstacles, implements this interface so that 
 *  it can provide CleanSweep information about presence of obstacles. 
 *  By implementing this interface, SenorSimulator is able to detect obstacles 
 *  around a given point on a certain floor.

*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
public interface IDetect {
	
	public PathType getPosXPathType(int floorLevel, int x, int y );
	
	public PathType getNegXPathType(int floorLevel, int x, int y);
	
	public PathType getPosYPathType(int floorLevel, int x, int y);
	
	public PathType getNegYPathType(int floorLevel, int x, int y);

}
