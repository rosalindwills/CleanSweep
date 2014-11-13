package sweep;

import java.io.File;
import java.io.IOException;

import com.se459.sensor.models.Cell;
import com.se459.sensor.models.SensorSimulator;
import com.se459.sensor.interfaces.ISensor;
import com.se459.modules.models.Vacuum;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class VacuumTest {

	private Vacuum vacuum;
	private ISensor sim = SensorSimulator.getInstance();

	public VacuumTest(){
		try 
		{
			((SensorSimulator)sim).importXml("src"+ File.separator + "main"+ File.separator + "resources" + File.separator +"homeLayout1.xml");
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
		vacuum = Vacuum.getInstance(sim, 1
				, 0, 0);
		
//		vacuum.Start();

/*		
		while(vacuum.on)
		{
			// wait for the vacuum to finish
		}
*/
		
		// check that it cleaned it's current location
//		Assert.assertEquals(0, sim.readCell(1, 0, 0).getDirtUnits());
	}
}