import static org.junit.Assert.*;

import org.junit.Test;

import com.se459.modules.models.VacuumMemory;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.models.Cell;


public class VacuumMemoryTest {

	@Test
	public void testAddNewCell() {
		// Arrange
		VacuumMemory memory = new VacuumMemory();
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		
		// Act
		memory.addNewCell(cell);
		
		// Assert
		assertEquals(1, memory.getAllUnfinishedCells().size());
		assertTrue(memory.getAllUnfinishedCells().contains(cell));
	}

	@Test
	public void testIfUnfinished() {
		// Arrange
		VacuumMemory memory = new VacuumMemory();
		VacuumMemory memory2 = new VacuumMemory();
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		
		// Act
		memory.addFinishedCells(cell);
		
		// Assert
		assertFalse(memory.ifUnfinished(cell));
		assertTrue(memory2.ifUnfinished(cell));
	}

	@Test
	public void testBecomeFinished() {
		// Arrange
		VacuumMemory memory = new VacuumMemory();
		VacuumMemory memory2 = new VacuumMemory();
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		memory.addNewCell(cell);
		memory2.addNewCell(cell);
		
		// Act
		memory.becomeFinished(cell);
		
		// Assert
		assertTrue(memory.getAllFinishedCells().contains(cell));
		assertTrue(memory2.getAllUnfinishedCells().contains(cell));
	}

	@Test
	public void testAddFinishedCells() {
		// Arrange
		VacuumMemory memory = new VacuumMemory();
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		
		// Act
		memory.addFinishedCells(cell);
		
		// Assert
		assertEquals(1, memory.getAllFinishedCells().size());
		assertTrue(memory.getAllFinishedCells().contains(cell));
	}

	@Test
	public void testGetAllKnownCells() {
		// Arrange
		VacuumMemory memory = new VacuumMemory();
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell cell2 = new Cell(3, 5, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		
		// Act
		memory.addFinishedCells(cell);
		memory.addNewCell(cell2);
		
		// Assert
		assertEquals(2, memory.getAllKnownCells().size());
		assertTrue(memory.getAllKnownCells().contains(cell));
		assertTrue(memory.getAllKnownCells().contains(cell2));
	}
	
	@Test
	public void testGetAllFinishedCells() {
		// Arrange
		VacuumMemory memory = new VacuumMemory();
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell cell2 = new Cell(3, 5, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		
		// Act
		memory.addFinishedCells(cell);
		memory.addNewCell(cell2);
		
		// Assert
		assertEquals(1, memory.getAllFinishedCells().size());
		assertTrue(memory.getAllFinishedCells().contains(cell));
		assertFalse(memory.getAllFinishedCells().contains(cell2));
	}

}
