import static org.junit.Assert.*;

import org.junit.Test;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.models.Cell;
import com.se459.sensor.models.Floor;


public class FloorTest {

	@Test
	public void testFloor() {
		// Arrange
		int level = 2;
		
		// Act
		Floor floor = new Floor(level);
		
		// Assert
		assertEquals(level, floor.getLevel());
	}
	
	@Test
	public void testAddCell() {
		// Arrange
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Floor floor = new Floor(1);
		
		// Act
		floor.addCell(cell);
		
		// Assert
		assertEquals(cell, floor.getCell(3, 3));
	}

	@Test
	public void testCleanCell() {
		// Arrange
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Floor floor = new Floor(1);
		floor.addCell(cell);
		
		// Act
		floor.cleanCell(3, 3);
		
		// Assert
		assertEquals(2, floor.getCell(3, 3).getDirtUnits());
	}

	@Test
	public void testMinMax() {
		// Arrange
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell cell2 = new Cell(4, 5, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Floor floor = new Floor(1);
		
		// Act
		floor.addCell(cell);
		floor.addCell(cell2);
		
		// Assert
		assertEquals(cell.getX(), floor.getMinX());
		assertEquals(cell.getY(), floor.getMinY());
		assertEquals(cell2.getX(), floor.getMaxX());
		assertEquals(cell2.getY(), floor.getMaxY());
	}

}
