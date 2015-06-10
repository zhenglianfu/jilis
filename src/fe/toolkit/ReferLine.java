package fe.toolkit;

class ReferLine{
	private int lineNumber;
	
	private String line;
	
	public ReferLine(){}
	
	public ReferLine(int lineNumber, String line){
		this.line = line;
		this.lineNumber = lineNumber;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}
}