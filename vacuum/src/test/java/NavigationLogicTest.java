package sweep;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.se459.sensor.models.Cell;
import com.se459.sensor.models.SensorSimulator;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.modules.models.NavigationLogic;
import com.se459.modules.models.VacuumMemory;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class NavigationLogicTest {

	private NavigationLogic navigation;
	private ISensor sim = SensorSimulator.getInstance();
	private VacuumMemory memory;

	public NavigationLogicTest(){
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

		Cell cell = new Cell(0, 0, SurfaceType.HIGHPILE, 0, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, true);
		memory = new VacuumMemory();
		memory.addFinishedCells(cell);
		
		navigation = new NavigationLogic(sim, memory);
	}
			
	@Test
	public void TestInitialization() 
	{
		assertEquals(navigation.getReturnPath().size(), 0);		
		assertEquals(navigation.getReturnCost(), 0, .1);
		
		assertEquals(navigation.getCurrentPath().size(), 0);
	}
	
	@Test
	public void TestCheckAndGetNext() 
	{
		Cell cell = new Cell(1, 0, SurfaceType.HIGHPILE, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, true);
		
		memory.addNewCell(cell);
		
		ICell next = navigation.checkAndGetNext(50.0);
		
		assertEquals(next.getX(), cell.getX());
		assertEquals(next.getY(), cell.getY());
	}
	
	@Test
	public void TestCalculateReturnPath() 
	{
		Cell cell = new Cell(1, 0, SurfaceType.HIGHPILE, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, true);
		
		memory.addNewCell(cell);
		
		try
		{
			Field field = navigation.getClass().getDeclaredField("currentPosition");

			field.set(navigation, cell);			
		}
		catch(Exception e)
		{
			
		}
		
		navigation.calculateReturnPath();

		assertEquals(navigation.getReturnPath().size(), 1);
		assertEquals(navigation.getReturnCost(), cell.getVacuumCost(), .1);
	}
}