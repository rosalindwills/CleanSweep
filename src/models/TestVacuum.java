package models;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;

import org.junit.Test;
import org.xml.sax.SAXException;

public class TestVacuum {

	private Vacuum vacuum;
	private SensorSimulator sim = new SensorSimulator();

	public TestVacuum(){
		try 
		{
			sim.importXml("src"+ File.separator + "sampleXml"+ File.separator + "homeLayout1.xml");
		} 
		catch (SAXException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
			
	@Test
	public void test1() 
	{
		vacuum = new Vacuum(sim, 0,0);
		
		vacuum.Start();
		
		while(vacuum.on)
		{
			// wait for the vacuum to finish
		}
		
		// check that it cleaned it's current location
		Assert.assertEquals(0, sim.readCell(1, 0, 0).getDirtUnits());
	}
}
