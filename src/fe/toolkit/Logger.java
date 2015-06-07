package fe.toolkit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
	
	private static Logger logger;
	
	private File log;
	
	private Logger(){}
	
	public static Logger getInstance(String path){
		if (logger == null) {
			logger = new Logger();
		}
		return logger;
	}
	
	public void log(String str) throws IOException{
		FileWriter writer = new FileWriter(this.log);
		writer.append(str);
		writer.close();
	}
	
}
