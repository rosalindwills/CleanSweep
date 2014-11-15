import static org.junit.Assert.*;

import org.junit.Test;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.interfaces.ISensor;
import com.se459.sensor.models.Cell;
import com.se459.sensor.models.Floor;
import com.se459.sensor.models.HomeLayout;
import com.se459.sensor.models.SensorSimulator;


public class SensorSimulatorTest {
	
	@Test
	public void testGetPosXPathType() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		PathType type = simulator.getPosXPathType(1, 3, 4);
		
		// Assert
		assertEquals(PathType.OBSTACLE, type);
	}

	@Test
	public void testGetNegXPathType() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		PathType type = simulator.getNegXPathType(1, 3, 4);
		
		// Assert
		assertEquals(PathType.OPEN, type);
	}

	@Test
	public void testGetPosYPathType() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		PathType type = simulator.getPosYPathType(1, 3, 4);
		
		// Assert
		assertEquals(PathType.STAIRS, type);
	}

	@Test
	public void testGetNegYPathType() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		PathType type = simulator.getNegYPathType(1, 3, 4);
		
		// Assert
		assertEquals(PathType.UNKNOWN, type);
	}

	@Test
	public void testGetPosXCell() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Cell cell2 = new Cell(4, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		layout.addCell(1, cell2);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		ICell result = simulator.getPosXCell(1, 3, 4);
		
		// Assert
		assertEquals(cell2, result);
	}

	@Test
	public void testGetNegXCell() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Cell cell2 = new Cell(2, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		layout.addCell(1, cell2);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		ICell result = simulator.getNegXCell(1, 3, 4);
		
		// Assert
		assertEquals(cell2, result);
	}

	@Test
	public void testGetPosYCell() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Cell cell2 = new Cell(3, 5, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		layout.addCell(1, cell2);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		ICell result = simulator.getPosYCell(1, 3, 4);
		
		// Assert
		assertEquals(cell2, result);
	}

	@Test
	public void testGetNegYCell() {
		// Arrange
		Cell cell = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Cell cell2 = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		layout.addCell(1, cell2);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		ICell result = simulator.getNegYCell(1, 3, 4);
		
		// Assert
		assertEquals(cell2, result);
	}

	@Test
	public void testGetStartPoint() {
		// Arrange
		Cell cell = new Cell(0, 0, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OPEN, PathType.STAIRS, PathType.UNKNOWN, false);
		Floor floor = new Floor(1);
		HomeLayout layout = new HomeLayout();
		layout.addFloor(floor);
		layout.addCell(1, cell);
		ISensor simulator = SensorSimulator.getInstance(layout);
		
		// Act
		ICell result = simulator.getStartPoint(1);
		
		// Assert
		assertEquals(cell, result);
	}

}
