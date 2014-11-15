import static org.junit.Assert.*;

import org.junit.Test;

import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.models.Cell;


public class CellTest {

	@Test
	public void testEqualsOverride() {
		// Arrange
		Cell cell = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell cell2 = new Cell(3, 3, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		Cell cell3 = new Cell(3, 4, SurfaceType.BAREFLOOR, 3, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE, false);
		
		// Act
		int hash = cell.hashCode();
		int hash2 = cell2.hashCode();
		int hash3 = cell3.hashCode();
		
		// Assert
		assertTrue(hash == hash2);
		assertTrue(cell.equals(cell2));
		assertFalse(hash == hash3);
		assertFalse(cell.equals(cell3));
	}

	@Test
	public void testCell() {
		// Arrange
		int x = 3;
		int y = 4;
		SurfaceType surfaceType = SurfaceType.BAREFLOOR;
		int dirtUnits = 2;
		PathType posX = PathType.UNKNOWN;
		PathType negX = PathType.STAIRS;
		PathType posY = PathType.OPEN;
		PathType negY = PathType.OBSTACLE;
		boolean isChargingStation = false;
		
		// Act
		Cell cell = new Cell(x, y, surfaceType, dirtUnits, posX, negX, posY, negY, isChargingStation);
		
		// Assert
		assertEquals(x, cell.getX());
		assertEquals(y, cell.getY());
		assertEquals(surfaceType, cell.getSurfaceType());
		assertEquals(dirtUnits, cell.getDirtUnits());
		assertEquals(posX, cell.getPathPosX());
		assertEquals(negX, cell.getPathNegX());
		assertEquals(posY, cell.getPathPosY());
		assertEquals(negY, cell.getPathNegY());
		assertEquals(isChargingStation, cell.getIsChargingStation());
	}

}
