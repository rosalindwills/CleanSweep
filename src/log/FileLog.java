package log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/** This class for recording logging into a file.
 * 
*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
class FileLog implements Log{
	
	private String logPathString = "log" + File.separator + "log";
	BufferedWriter output;
	
	private int lenOfHorizontalLine = 30;

	/** Default constructor. Log file will be created at /log/log */
	FileLog(){
		init();
	}
	
	/** Log file will be created at a given path */
	FileLog(String logPathString){
		this.logPathString = logPathString;
		init();
	}
	
	private void init(){
		File logFile = new File(logPathString);
		if (!logFile.exists()) {
			try {
				logFile.getParentFile().mkdirs(); 
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@Override
	public void append(String logline) {
		try {
			output = new BufferedWriter(new FileWriter(logPathString, true));
			output.append(logline);
			output.newLine();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void newline() {
		try {
			output = new BufferedWriter(new FileWriter(logPathString, true));
			output.newLine();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void horizontalLine() {
		try {
			output = new BufferedWriter(new FileWriter(logPathString, true));
			for(int i = 0; i < this.lenOfHorizontalLine; i++){
				output.append("-");
			}
			output.newLine();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
