package com.se459.util.log;

/** This class for outputting logging to console.
*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
public class ScreenLog implements Log {
	
	private int lenOfHorizontalLine = 30;
	
	public int getLenOfHorizontalLine() {
		return lenOfHorizontalLine;
	}

	public void setLenOfHorizontalLine(int lenOfHorizontalLine) {
		this.lenOfHorizontalLine = lenOfHorizontalLine;
	}

	public void append(String logline) {
		System.out.println(logline);
	}

	public void newline() {
		System.out.println();
	}

	public void horizontalLine() {
		for(int i = 0; i < this.lenOfHorizontalLine; i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	public void clear() {
		try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows")) {
	            Runtime.getRuntime().exec("cls");
	        } else {
	            Runtime.getRuntime().exec("clear");
	        }
	    } catch (final Exception e) {
	    	throw new RuntimeException(e);
	    }
	}
	
	

}
