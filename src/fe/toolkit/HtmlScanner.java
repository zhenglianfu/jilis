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
			if (match.find()) {
				String piece = match.group();
				String[] pieces = piece.split(Util.TAG_SPLIT);
				for (int i = 0; i < pieces.length; i++) {
					Matcher pieceMatch = Util.SRC_PATTERN.matcher(pieces[i]);
					if (pieceMatch.find()) {
						String attr  = pieceMatch.group();
						String[] items = attr.split("=");
						uris.add(new ReferLine(lineNumber, trimQuoteRound(join(slice(items, 1), "="))));
					}
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
	
	private String trimQuoteRound(String input){
		input = input.trim();
		if (input.charAt(0) == '\'' || input.charAt(0) == '"') {
			input = input.substring(1);
		}
		if (input.charAt(input.length() - 1) == '\'' || input.charAt(input.length() - 1) == '"') {
			input = input.substring(0, input.length() - 1);
		}
		return input;
	}
	
	private String[] slice(String[] arr, int start){
		return slice(arr, start, arr.length);
	}
	
	private String[] slice(String[] arr, int start, int end){
		String[] temp = new String[Math.abs(end - start)];
		int i = 0;
		for (; start < end; start++) {
			temp[i++] = arr[start];
		}
		return temp;
	}
	
	private String join(String[] arr, String separator){
		String s = "";
		separator = separator == null ? "" : separator;
		for (int i = 0; i < arr.length; i++) {
			s += arr[i] + separator;
		}
		return s.substring(0, s.length() - separator.length());
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