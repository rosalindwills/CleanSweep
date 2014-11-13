package com.se459.util.log;

import java.io.File;

public class Config {

	public double chargeCapacity = 50;
	public double dirtCapacity = 50;
	public long delay = 50;

	public String floorPlanFilePath = "sensor" + File.separator + "src" + File.separator
			+ "main" + File.separator + "resources" + File.separator
			+ "homeLayout1.xml";
	public String defaultLogFilePath = "log" + File.separator + "log";
	public String memoryDumpOutDirectory = "log" + File.separator + "memory";
	public boolean debugMode = true;
	
	private static final Config instance;
	 
    static {
        try {
            instance = new Config();
        } catch (Exception e) {
            throw new RuntimeException("An error occurred!", e);
        }
    }
 
    public static Config getInstance() {
        return instance;
    }
 
    private Config() {
    }
}
