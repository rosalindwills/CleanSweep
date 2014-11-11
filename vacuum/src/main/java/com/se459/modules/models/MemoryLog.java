package com.se459.modules.models;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import com.se459.util.log.Config;

public class MemoryLog {

	private String logPathString = Config.getInstance().memoryDumpOutDirectory;
	BufferedWriter output;

	
	private int fileNum = 0;

	public MemoryLog() {
		init();
	}

	private void init() {
		
		String folderName = new SimpleDateFormat(
				"[yyyyMMdd][hh][mm][ss][aaa]").format(new Date());
		this.logPathString = Config.getInstance().memoryDumpOutDirectory + File.separator + folderName;
		File logFile = new File(logPathString);
		if (!logFile.exists()) {
			logFile.getParentFile().mkdirs();
			logFile.mkdir();
		}
	}

	public void append(BufferedImage bi) {
		
		String fileNumStr = String.format("%05d", this.fileNum);

		String fileName = this.logPathString + File.separator
				+ fileNumStr + ".png";
		
		this.fileNum ++;
	
		try {
			ImageIO.write(bi, "png", new File(fileName));
		} catch (Exception e) {
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
