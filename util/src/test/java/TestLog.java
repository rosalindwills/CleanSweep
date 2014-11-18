package sweep;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;

import com.se459.util.log.Config;
import com.se459.util.log.Log;
import com.se459.util.log.LogFactory;

import org.junit.Test;

public class TestLog {

	@Test
	public void test1() {
		Log log = LogFactory.newFileLog();
		testAppendLine(log, "234");
	}

	@Test
	public void test2() {
		Log log = LogFactory.newFileAndScreenLog();
		testAppendLine(log, "123");
	}

	@Test
	public void test3() {
		Log log = LogFactory.newFileLog();
		log.setLenOfHorizontalLine(30);
		testAppendHorizonLine(log);
	}

	@Test
	public void test4() {
		Log log = LogFactory.newFileAndScreenLog();
		log.setLenOfHorizontalLine(10);
		testAppendHorizonLine(log);
	}

	private void testAppendLine(Log log, String line) {
		log.clear();

		log.append(line);
		log.newline();
		log.append(line);

		BufferedReader br;
		try {
			File logFile = new File(Config.getInstance().defaultLogFilePath);
			br = new BufferedReader(new FileReader(logFile));
			String firstLine = br.readLine();
			String secondLine = br.readLine();
			String thirdLine = br.readLine();
			Assert.assertEquals(line, firstLine);
			Assert.assertEquals("", secondLine);
			Assert.assertEquals(line, thirdLine);
			br.close();
			log.clear();
			boolean empty = logFile.length() == 0;
			Assert.assertTrue(empty);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void testAppendHorizonLine(Log log) {
		log.clear();
		log.horizontalLine();
		BufferedReader br;
		try {
			File logFile = new File(Config.getInstance().defaultLogFilePath);
			br = new BufferedReader(new FileReader(logFile));
			String line = br.readLine();
			String expected = "";
			for (int i = 0; i < log.getLenOfHorizontalLine(); i++) {
				expected = expected + "-";
			}

			Assert.assertEquals(expected, line);
			br.close();
			log.clear();
			boolean empty = logFile.length() == 0;
			Assert.assertTrue(empty);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
