package log;

/** This class for outputting logging to console.
*
* @author Jerry Liu
* @version 1.0 Oct 9, 2014.
*/
class ScreenLog implements Log{
	
	private int lenOfHorizontalLine = 30;
	
	@Override
	public void append(String logline) {
		System.out.println(logline);
	}

	@Override
	public void newline() {
		System.out.println();
		
	}

	@Override
	public void horizontalLine() {
		for(int i = 0; i < this.lenOfHorizontalLine; i++){
			System.out.print("-");
		}
		System.out.println();
	}

}
