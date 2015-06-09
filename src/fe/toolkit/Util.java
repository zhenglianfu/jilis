package fe.toolkit;

import java.io.File;
import java.util.regex.Pattern;

public class Util {
	
	public static final String TAG_SPLIT  = "<img|<link|<script";
	
	public static final String CLASSPATH  = (new File("")).getAbsolutePath();
	
	public static final Pattern SRC_TAG_PATTERN = Pattern.compile(
			"<(link|script|img).*?\\s(href|src)=['\\\"](.*?)['\\\"]",
			Pattern.CASE_INSENSITIVE);

 	public static final Pattern CSS_URL_PATTERN = Pattern.compile("");
	
	public static boolean isEmpty(String s) {
		return s == null || "".equals(s);
	}
}
