package fe.toolkit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author Administrator CSS file scanner url("") url('') url()
 *         '@import("dir/a.css")' '@import('dir/a.css')' '@import(dir/a.css)'
 */
public class CssScanner {
	private File file;

	private String absPath;
	
	private Map<String, Resource> resourceMap;
	
	public static CssScanner getInstance(String path){
		return getInstance(new File(path));
	}
	
	public static CssScanner getInstance(File file){
		CssScanner scanner = new CssScanner();
		scanner.file = file;
		scanner.absPath = file.getAbsolutePath();
		scanner.resourceMap = new HashMap<String, Resource>();
		return scanner;
	}
	
	public Map<String, Resource> parseResource(String root, String parent) throws IOException{
		BufferedReader reader = new BufferedReader(new FileReader(this.file));
		String line = reader.readLine();
		List<ReferLine> uris = new ArrayList<ReferLine>();
		int lineNumber = 1;
		while(line != null){
			Matcher match = Util.CSS_URL_PATTERN.matcher(line);
			while (match.find()) {
				String uri = match.group(match.groupCount());
				if (!Util.isEmpty(uri)) {
					uris.add(new ReferLine(lineNumber, uri.trim()));
				}
			}
			line = reader.readLine();
			lineNumber += 1;
		}
		reader.close();
		// set resourceMap
		Path path = new Path();
		path.setParent(parent);
		for (ReferLine refer : uris) {
			String uri = refer.getLine();
			Resource resource = Resource.parseURI(path.caculatePath(uri), refer.getLine());
			this.resourceMap.put(uri, resource);
			if (!resource.isExisted()) {
				System.out.println(this.absPath + ", line: " + refer.getLineNumber() + ": " + uri + " is not existd");
			}
		}
		return this.resourceMap;
	}

}
