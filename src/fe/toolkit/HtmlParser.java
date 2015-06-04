package fe.toolkit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


/**
 * @author Administrator
 * @version 1.0 alpha html parser made in JAVA
 */
public class HtmlParser {
	
	private FileReader reader;
	
	private HtmlParser(){}
	
	public static HtmlParser getInstance(File html) throws FileNotFoundException{
		return getInstance(new FileReader(html));
	}
	
	public static HtmlParser getInstance(FileReader html){
		HtmlParser htmlParser = new HtmlParser();
		htmlParser.reader = html;
		return htmlParser;
	}
	
	public Map<String, String> parseResource(){
		
	}
	
	public void closeReader() throws IOException{
	 	if (null != this.reader) {
	 		this.reader.close();
	 	}
	}

}
