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

	Log log = LogFactory.newFileLog();

	@Test
	public void test() {
		log.clear();
		
		log.append("abc");
		log.newline();
		log.append("edf");
		
		BufferedReader br;
		try {
			File logFile = new File(Config.getInstance().defaultLogFilePath);
			br = new BufferedReader(new FileReader(logFile));
			String firstLine = br.readLine();
			String secondLine = br.readLine();
			String thirdLine = br.readLine();
			Assert.assertEquals("abc", firstLine);
			Assert.assertEquals("", secondLine);
			Assert.assertEquals("edf", thirdLine);
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
