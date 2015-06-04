package fe.toolkit;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * @author Administrator
 * @version 1.0 alpha Front End Developer Tool made by JAVA language
 */
public class Main {

	private Map<String, String> config = new HashMap<String, String>();
	
	private boolean rename;
	
	private boolean md5;
	
	public static void main(String[] path) throws IOException {
		Main process = new Main();
		InputStream in = Main.class.getClassLoader().getResourceAsStream("resource/config.properties");
		Properties properties = new Properties();
		properties.load(in);
		for (Object obj : properties.keySet()) {
			process.config.put(obj.toString(), properties.get(obj).toString());
		}
		String dirPath = "";
		if (path != null) {
			
		} else {
			
		}
	}
}
