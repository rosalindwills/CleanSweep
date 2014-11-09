package com.se459.util.log;

public class LogFactory {
	
	private LogFactory() {
	}
	
	/** Returns a logging object, which can be used to output logging to console.
	 * 
	 * @return logging object
	 */
	public static Log newScreenLog(){
		return new ScreenLog();
	}
	
	/** Returns a logging object, which can be used to record logging into a file.
	 *  Log file will be created at /log/log
	 * 
	 * @return logging object
	 */
	public static Log newFileLog(){
		return new FileLog();
	}
	
	/** Returns a logging object, which can be used to record logging into a file.
	 *  You can specify the path of the log file(e.g. /a/b/log1.abc)
	 *  @param filePath   the path of the log file.
	 * 
	 * @return logging object
	 */
	public static Log newFileLog(String filePath){
		return new FileLog(filePath);
	}

}
