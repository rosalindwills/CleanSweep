import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.se459.modules.models.PathFinder;
import com.se459.sensor.enums.PathType;
import com.se459.sensor.enums.SurfaceType;
import com.se459.sensor.interfaces.ICell;
import com.se459.sensor.models.Cell;


public class PathFinderTest {

	@Test
	public void test1() {
		
		ICell c1= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OBSTACLE,PathType.OBSTACLE, false);
		c1.setTraversed();
		List<ICell> cells = new ArrayList<ICell>();
		cells.add(c1);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c1, cells);
		assertEquals(c1, result.get(0));
	}
	
	@Test
	public void test2() {
		List<ICell> cells = new ArrayList<ICell>();
		List<ICell> destinations = new ArrayList<ICell>();
		ICell c1= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OBSTACLE,PathType.OBSTACLE, false);
		c1.setTraversed();
		cells.add(c1);
		ICell c2= new Cell(1,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c2.setTraversed();
		cells.add(c2);
		destinations.add(c2);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c1, destinations);
		assertEquals(c1, result.get(0));
		assertEquals(c2, result.get(1));
	}
	
	@Test
	public void test3() {
		List<ICell> cells = new ArrayList<ICell>();
		List<ICell> destinations = new ArrayList<ICell>();
		ICell c1= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OBSTACLE,PathType.OBSTACLE, false);
		c1.setTraversed();
		cells.add(c1);
		ICell c2= new Cell(1,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c2.setTraversed();
		cells.add(c2);
		destinations.add(c2);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c1, destinations);
		assertEquals(result.size(), 0);
	}
	
	@Test
	public void test4() {
		List<ICell> cells = new ArrayList<ICell>();
		List<ICell> destinations = new ArrayList<ICell>();
		ICell c1= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c1.setTraversed();
		cells.add(c1);
		ICell c2= new Cell(2,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c2.setTraversed();
		cells.add(c2);
		destinations.add(c2);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c1, destinations);
		assertEquals(result.size(), 0);
	}
	
	@Test
	public void test5() {
		List<ICell> cells = new ArrayList<ICell>();
		List<ICell> destinations = new ArrayList<ICell>();
		ICell c1= new Cell(0,1,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c1.setTraversed();
		cells.add(c1);
		ICell c2= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OBSTACLE, false);
		c2.setTraversed();
		cells.add(c2);
		ICell c3= new Cell(0,2,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OBSTACLE,PathType.OPEN, false);
		c3.setTraversed();
		cells.add(c3);
		ICell c4= new Cell(1,0,SurfaceType.HIGHPILE, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OBSTACLE, false);
		c4.setTraversed();
		cells.add(c4);
		ICell c5= new Cell(1,2,SurfaceType.LOWPILE, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OBSTACLE,PathType.OPEN, false);
		c5.setTraversed();
		cells.add(c5);
		ICell c6= new Cell(1,1,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c6.setTraversed();
		cells.add(c6);
		
		destinations.add(c6);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c1, destinations);
		assertEquals(c1, result.get(0));
		assertEquals(c3, result.get(1));
		assertEquals(c5, result.get(2));
		assertEquals(c6, result.get(3));
		assertEquals(4, result.size());
		assertEquals(4.0, PathFinder.calculateCost(result),0);
	}
	
	@Test
	public void test6() {
		List<ICell> cells = new ArrayList<ICell>();
		List<ICell> destinations = new ArrayList<ICell>();
		ICell c1= new Cell(0,1,SurfaceType.HIGHPILE, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c1.setTraversed();
		cells.add(c1);
		ICell c2= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OBSTACLE, false);
		c2.setTraversed();
		cells.add(c2);
		ICell c3= new Cell(0,2,SurfaceType.HIGHPILE, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c3.setTraversed();
		cells.add(c3);
		ICell c4= new Cell(1,0,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OBSTACLE, false);
		c4.setTraversed();
		cells.add(c4);
		ICell c5= new Cell(1,2,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c5.setTraversed();
		cells.add(c5);
		ICell c6= new Cell(1,1,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c6.setTraversed();
		cells.add(c6);
		ICell c7= new Cell(0,3,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OBSTACLE,PathType.OPEN, false);
		c7.setTraversed();
		cells.add(c7);
		ICell c8= new Cell(1,3,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OBSTACLE,PathType.OPEN, false);
		c8.setTraversed();
		cells.add(c8);
		
		destinations.add(c7);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c2, destinations);
		assertEquals(c2, result.get(0));
		assertEquals(c4, result.get(1));
		assertEquals(c6, result.get(2));
		assertEquals(c5, result.get(3));
		assertEquals(c8, result.get(4));
		assertEquals(c7, result.get(5));
		assertEquals(6, result.size());
		assertEquals(5.0, PathFinder.calculateCost(result),0);
	}
	
	@Test
	public void test7() {
		List<ICell> cells = new ArrayList<ICell>();
		List<ICell> destinations = new ArrayList<ICell>();
		ICell c1= new Cell(0,1,SurfaceType.HIGHPILE, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c1.setTraversed();
		cells.add(c1);
		ICell c2= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OBSTACLE, false);
		c2.setTraversed();
		cells.add(c2);
		ICell c3= new Cell(0,2,SurfaceType.HIGHPILE, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c3.setTraversed();
		cells.add(c3);
		ICell c4= new Cell(1,0,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OBSTACLE, false);
		c4.setTraversed();
		cells.add(c4);
		ICell c5= new Cell(1,2,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c5.setTraversed();
		cells.add(c5);
		ICell c6= new Cell(1,1,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c6.setTraversed();
		cells.add(c6);
		ICell c7= new Cell(0,3,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OBSTACLE,PathType.OPEN, false);
		c7.setTraversed();
		cells.add(c7);
		ICell c8= new Cell(1,3,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OBSTACLE,PathType.OPEN, false);
		c8.setTraversed();
		cells.add(c8);
		
		destinations.add(c7);
		destinations.add(c8);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c2, destinations);
		assertEquals(c2, result.get(0));
		assertEquals(c4, result.get(1));
		assertEquals(c6, result.get(2));
		assertEquals(c5, result.get(3));
		assertEquals(c8, result.get(4));
		assertEquals(5, result.size());
		assertEquals(4.0, PathFinder.calculateCost(result),0);
	}
	
	@Test
	public void test8() {
		List<ICell> cells = new ArrayList<ICell>();
		List<ICell> destinations = new ArrayList<ICell>();
		ICell c1= new Cell(0,1,SurfaceType.HIGHPILE, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c1.setTraversed();
		cells.add(c1);
		ICell c2= new Cell(0,0,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OBSTACLE, false);
		c2.setTraversed();
		cells.add(c2);
		ICell c3= new Cell(0,2,SurfaceType.HIGHPILE, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OPEN,PathType.OPEN, false);
		c3.setTraversed();
		cells.add(c3);
		ICell c4= new Cell(1,0,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OBSTACLE, false);
		c4.setTraversed();
		cells.add(c4);
		ICell c5= new Cell(1,2,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c5.setTraversed();
		cells.add(c5);
		ICell c6= new Cell(1,1,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OPEN,PathType.OPEN, false);
		c6.setTraversed();
		cells.add(c6);
		ICell c7= new Cell(0,3,SurfaceType.BAREFLOOR, 0, PathType.OPEN, PathType.OBSTACLE, PathType.OBSTACLE,PathType.OPEN, false);
		c7.setTraversed();
		cells.add(c7);
		ICell c8= new Cell(1,3,SurfaceType.BAREFLOOR, 0, PathType.OBSTACLE, PathType.OPEN, PathType.OBSTACLE,PathType.OPEN, false);
		c8.setTraversed();
		cells.add(c8);
		
		destinations.add(c3);
		destinations.add(c8);
		PathFinder pf = new PathFinder(cells);
		List<ICell> result =  pf.findPath(c2, destinations);
		assertEquals(c2, result.get(0));
		assertEquals(c4, result.get(1));
		assertEquals(c6, result.get(2));
		assertEquals(c5, result.get(3));
		assertEquals(c8, result.get(4));
		assertEquals(5, result.size());
		assertEquals(4.0, PathFinder.calculateCost(result),0);
	}
	
	


}
