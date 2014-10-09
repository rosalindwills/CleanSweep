package models;

import java.io.File;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import enums.PathType;

/** Test if SensorSimulator class can 
 * provide information about presence of obstacles correctly.

*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
public class TestDetectObstacle {
	
	private SensorSimulator sim = new SensorSimulator();
	
	public TestDetectObstacle(){
		try {
			sim.importXml("src"+ File.separator + "sampleXml"+ File.separator + "homeLayout1.xml");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	// case : <cell xs='1' ys='2' ss='2' ps='1111' ds='1' cs='0' />
	public void test1() {
		Assert.assertEquals(PathType.OPEN, sim.getPosXPathType(1, 1, 2));
		Assert.assertEquals(PathType.OPEN, sim.getNegXPathType(1, 1, 2));
		Assert.assertEquals(PathType.OPEN, sim.getPosYPathType(1, 1, 2));
		Assert.assertEquals(PathType.OPEN, sim.getNegYPathType(1, 1, 2));
	}
	
	@Test
	// case: <cell xs='3' ys='4' ss='2' ps='2121' ds='1' cs='0' />
	public void test2() {
		Assert.assertEquals(PathType.OBSTACLE, sim.getPosXPathType(1, 3, 4));
		Assert.assertEquals(PathType.OPEN, sim.getNegXPathType(1, 3, 4));
		Assert.assertEquals(PathType.OBSTACLE, sim.getPosYPathType(1, 3, 4));
		Assert.assertEquals(PathType.OPEN, sim.getNegYPathType(1, 3, 4));
	}
	
	@Test
	// case: <cell xs='0' ys='7' ss='2' ps='1212' ds='1' cs='0' />
	public void test3() {
		Assert.assertEquals(PathType.OPEN, sim.getPosXPathType(1, 0, 7));
		Assert.assertEquals(PathType.OBSTACLE, sim.getNegXPathType(1, 0, 7));
		Assert.assertEquals(PathType.OPEN, sim.getPosYPathType(1, 0, 7));
		Assert.assertEquals(PathType.OBSTACLE, sim.getNegYPathType(1, 0, 7));
	}

	
}
