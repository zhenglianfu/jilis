package fe.toolkit;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Configer {
	
	public static final String PROPERTIES_RENAME = "rename";

	public static final String PROPERTIES_MD5 = "md5";

	public static final String PROPERTIES_ROOT = "root";

	public static final String PROPERTIES_DIR = "dir";

	public static final String PROPERTIES_PUBLIC = "public";

	public static final String PROPERTIES_RESOURCE = "resource";
	
	public static final String PROPERTIES_HTML     = "html";
	
	public static final String PROPERTIES_KEEP_FILENAME = "keepfilename";
	
	private static Map<String, String> config;

	public static Map<String, String> getConfig() throws IOException {
		if (null != config) {
			return config;
		}
		config = new HashMap<String, String>();
		InputStream in = Configer.class.getClassLoader().getResourceAsStream(
				"resource" + File.separator + "config.properties");
		Properties properties = new Properties();
		properties.load(in);
		for (Object obj : properties.keySet()) {
			config.put(obj.toString(), properties.get(obj).toString());
		}
		return config;
	}

}
