package sweep;

import java.io.File;
import java.io.IOException;

import com.se459.sensor.models.Cell;
import com.se459.sensor.models.SensorSimulator;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.util.log.LogFactory;
import com.se459.modules.models.SweepLog;
import com.se459.modules.models.Vacuum;

import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

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
	public void testStartStop() 
	{
		vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newScreenLog()));
		
		assertFalse(vacuum.isOn());
		
		vacuum.start();

		assertTrue(vacuum.isOn());
		
		vacuum.stop();
		
		assertFalse(vacuum.isOn());
	}
	
	@Test
	public void testSweep()
	{
		Cell cell = new Cell(0, 0, SurfaceType.HIGHPILE, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newScreenLog()));	

		assertEquals(cell.getDirtUnits(), 1);
		
		try
		{
			Method method = vacuum.getClass().getDeclaredMethod("sweep", ICell.class);
			method.setAccessible(true);
			method.invoke(vacuum, cell);
		}
		catch(Exception e)
		{
			
		}
		
		assertEquals(cell.getDirtUnits(), 0);
	}

	@Test
	public void testEnergyUse()
	{
		Cell highPile = new Cell(0, 0, SurfaceType.HIGHPILE, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell lowPile = new Cell(0, 0, SurfaceType.LOWPILE, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell bareFloor = new Cell(0, 0, SurfaceType.BAREFLOOR, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);		
		
		vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newScreenLog()));
		
		double initialCharge = vacuum.getChargeRemaining();
		
		try
		{
			Method method = vacuum.getClass().getDeclaredMethod("sweep", ICell.class);
			method.setAccessible(true);
			method.invoke(vacuum, highPile);
		}
		catch(Exception e)
		{
			
		}
		
		assertEquals(vacuum.getChargeRemaining(), initialCharge - highPile.getVacuumCost(), .1);
		
		initialCharge = vacuum.getChargeRemaining();
		
		try
		{
			Method method = vacuum.getClass().getDeclaredMethod("sweep", ICell.class);
			method.setAccessible(true);
			method.invoke(vacuum, lowPile);
		}
		catch(Exception e)
		{
			
		}

		assertEquals(vacuum.getChargeRemaining(), initialCharge - lowPile.getVacuumCost(), .1);

		initialCharge = vacuum.getChargeRemaining();
		
		try
		{
			Method method = vacuum.getClass().getDeclaredMethod("sweep", ICell.class);
			method.setAccessible(true);
			method.invoke(vacuum, bareFloor);
		}
		catch(Exception e)
		{
			
		}

		assertEquals(vacuum.getChargeRemaining(), initialCharge - bareFloor.getVacuumCost(), .1);
	}
	
	@Test
	public void testPosition()
	{
		vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newScreenLog()));
		
		vacuum = Vacuum.getInstance(sim, 1, new SweepLog(LogFactory.newScreenLog()));
	}
}