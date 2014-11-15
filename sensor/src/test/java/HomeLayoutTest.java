import static org.junit.Assert.*;

import org.junit.Test;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.models.Cell;
import com.se459.sensor.models.Floor;
import com.se459.sensor.models.HomeLayout;


public class HomeLayoutTest {

	@Test
	public void testHomeLayout() {
		// Arrange
		Floor floor = new Floor(1);
		
		// Act
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		
		// Assert
		assertEquals(layout.getFloors().size(), 1);
		assertEquals(layout.getFloor(1), floor);
	}

	@Test
	public void testGetCell() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		
		// Act
		layout.addCell(1, cell);
		
		// Assert
		assertEquals(cell, layout.getCell(1, 3, 4));
	}
	@Test
	public void testCleanCell() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		
		// Act
		layout.cleanCell(1, 3, 4);
		
		// Assert
		assertEquals(2, layout.getCell(1, 3, 4).getDirtUnits());
	}
}
