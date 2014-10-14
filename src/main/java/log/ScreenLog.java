package log;

/** This class for outputting logging to console.
*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
class ScreenLog implements Log{
	
	private int lenOfHorizontalLine = 30;
	
	public void append(String logline) {
		System.out.println(logline);
	}

	public void newline() {
		System.out.println();
		
	}

	public void horizontalLine() {
		for(int i = 0; i < this.lenOfHorizontalLine; i++){
			System.out.print("-");
		}
		System.out.println();
	}

	public void clear() {
		try
	    {
	        final String os = System.getProperty("os.name");

	        if (os.contains("Windows"))
	        {
	            Runtime.getRuntime().exec("cls");
	        }
	        else
	        {
	            Runtime.getRuntime().exec("clear");
	        }
	    }
	    catch (final Exception e)
	    {
	        e.printStackTrace();
	    }
	}

}
