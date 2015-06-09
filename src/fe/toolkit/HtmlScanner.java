package fe.toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;


/**
 * @author Administrator
 * @version 1.0 alpha html parser made in JAVA
 */
public class HtmlScanner {
	
	private Map<String, Resource> resourceMap;
	
	private FileReader reader;
	
	private File html;
	
	private HtmlScanner() {}
	
	public static HtmlScanner getInstance(File html) throws FileNotFoundException {
		HtmlScanner htmlParser = new HtmlScanner();
		htmlParser.reader = new FileReader(html);
		htmlParser.html = html;
		return htmlParser;
	}
	
	public Map<String, Resource> parseResource(String root, String parent) throws IOException {
		root   = root   == null ? Util.CLASSPATH : root;
		parent = parent == null ? root           : parent;
		this.resourceMap = new HashMap<String, Resource>();
		BufferedReader bufferReader = new BufferedReader(this.reader);
		String line = bufferReader.readLine();
		List<ReferLine> uris = new ArrayList<ReferLine>();
		int lineNumber = 1;
		while(line != null){
			Matcher match = Util.SRC_TAG_PATTERN.matcher(line);
			while (match.find()) {
				String uri = match.group(match.groupCount());
				if (!Util.isEmpty(uri)) {
					uris.add(new ReferLine(lineNumber, uri.trim()));
				}
			}
			line = bufferReader.readLine();
			lineNumber ++;
		}
		// calculate path
		Path path = new Path();
		path.setParent(parent);
		path.setRoot(root);
		for (int i = 0; i < uris.size(); i ++) {
			String uri = uris.get(i).getLine();
			Resource resource = Resource.parseURI(path.caculatePath(uri), uri);
			if (!resource.isExisted()) {
				System.out.println(this.html.getAbsolutePath() + " line " + uris.get(i).getLineNumber() + ": " + uri + " is not existed");
			}
			this.resourceMap.put(uri, resource);
		}
		return this.resourceMap;
	}
	
	public void closeReader() throws IOException{
	 	if (null != this.reader) {
	 		this.reader.close();
	 	}
	}
	
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

}
