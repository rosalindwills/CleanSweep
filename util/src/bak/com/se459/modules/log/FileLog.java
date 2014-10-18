package com.se459.modules.log;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/** This class for recording logging into a file.
 * 
*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
class FileLog implements Log {
	
	private String logPathString = "src"+ File.separator + "main"+ File.separator + "log" + File.separator +"log";
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

	public void newline() {
		try {
			output = new BufferedWriter(new FileWriter(logPathString, true));
			output.newLine();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

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

	public void clear() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(logPathString));
			writer.print("");
			writer.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
			
	}

}
