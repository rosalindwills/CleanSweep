package sweep;

import java.io.File;
import java.io.IOException;

import com.se459.visualization.HomeLayoutPanel;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.models.Cell;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;

import java.awt.Color;
import java.lang.reflect.Method;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.xml.sax.SAXException;

public class HomeLayoutDrawerTest {

	private HomeLayoutPanel panel;

	public HomeLayoutDrawerTest(){
		panel = new HomeLayoutPanel(null, null);
	}
	
	@Test
	public void TestCellColorFloorType()
	{
		Cell highPile = new Cell(0, 0, SurfaceType.HIGHPILE, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell lowPile = new Cell(0, 0, SurfaceType.LOWPILE, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell bareFloor = new Cell(0, 0, SurfaceType.BAREFLOOR, 1, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);		
		
		try
		{
			Method method = panel.getClass().getDeclaredMethod("getSurfaceColor", ICell.class, int.class);
			method.setAccessible(true);
			
			Color highPileColor = (Color)method.invoke(panel, highPile, 0);
			Color lowPileColor = (Color)method.invoke(panel, lowPile, 0);
			Color bareFloorColor = (Color)method.invoke(panel, bareFloor, 0);
			
			assertTrue(highPileColor.getRGB() != lowPileColor.getRGB());
			assertTrue(highPileColor.getRGB() != bareFloorColor.getRGB());
			assertTrue(lowPileColor.getRGB() != bareFloorColor.getRGB());
		}
		catch(Exception e)
		{
			assertTrue(false);
		}
	}
}