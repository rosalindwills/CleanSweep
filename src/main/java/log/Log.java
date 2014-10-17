package log;


/** This Log interface for logging.
*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
public interface Log {
	
	/** Append a log line.
	 *  @param logline		the string you want to record
	 */
	public void append(String logline);
	
	/** Append a empty new line.
	 */
	public void newline();
	
	/** Append a horizontal line.
	 */
	public void horizontalLine();
	
	/**
	 * Delete previous log
	 */
	public void clear();
	
	
}