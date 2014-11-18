package com.se459.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileAndScreenLog implements Log{

	private String logPathString = Config.getInstance().defaultLogFilePath;
	BufferedWriter output;
	
	private int lenOfHorizontalLine = 30;
	
	public int getLenOfHorizontalLine() {
		return lenOfHorizontalLine;
	}

	public void setLenOfHorizontalLine(int lenOfHorizontalLine) {
		this.lenOfHorizontalLine = lenOfHorizontalLine;
	}


	/** Default constructor. Log file will be created at /log/log */
	FileAndScreenLog(){
		init();
	}
	
	/** Log file will be created at a given path */
	FileAndScreenLog(String logPathString){
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
				throw new RuntimeException(e);
			}
		}
	}
	
	public void append(String logline) {
		System.out.println(logline);
		try {
			output = new BufferedWriter(new FileWriter(logPathString, true));
			output.append(logline);
			output.newLine();
			output.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void newline() {
		System.out.println();
		try {
			output = new BufferedWriter(new FileWriter(logPathString, true));
			output.newLine();
			output.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void horizontalLine() {
		for(int i = 0; i < this.lenOfHorizontalLine; i++) {
			System.out.print("-");
		}
		System.out.println();
		try {
			output = new BufferedWriter(new FileWriter(logPathString, true));
			for(int i = 0; i < this.lenOfHorizontalLine; i++){
				output.append("-");
			}
			output.newLine();
			output.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void clear() {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new File(logPathString));
			writer.print("");
			writer.close();	
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
			
	}

}
