package fe.toolkit;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator 资源文件重命名
 */
public class ResourceRefactor {

	public static final String CONNECT_CHAR = "_";

	public static int FILE_INDEX = 0;

	public static final int TIME_STAMP = 0;

	public static final int MD5_STAMP = 1;

	public static final int REPLACE_FILENAME = 2;

	public static final int APPEND_AS_QUERY = 3;

	public static final int APPEND_FILENAME = 4;

	private static Map<String, ResourceRefactor> instanceMap = new HashMap<String, ResourceRefactor>();

	private int stampType;

	private int renameType;

	private ResourceRefactor() {
	}

	public static ResourceRefactor getInstance(int stampType, int renameType) {
		return ResourceRefactor.getInstance("", stampType, renameType);
	}

	// only one dirPath in the memory
	public static ResourceRefactor getInstance(String dirPath, int stampType,
			int renameType) {
		dirPath = dirPath == null ? "" : dirPath;
		ResourceRefactor instance = null;
		if (instanceMap.containsKey(dirPath)) {
			instance = instanceMap.get(dirPath);
			instance.stampType = stampType;
			instance.renameType = renameType;
		} else {
			instance = new ResourceRefactor();
			instance.stampType = stampType;
			instance.renameType = renameType;
		}
		return instance;
	}

	public boolean refactor(String filePath, Map<String, Resource> resources)
			throws IOException {
		File file = new File(filePath);
		File temp = new File(filePath + "~");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(
				temp)));
		String line = reader.readLine();
		StringBuilder sb = new StringBuilder();
		while (null != line) {
			writer.print(line);
			line = this.refactorSrc(line, resources) + "\n";
			sb.append(line);
			line = reader.readLine();
		}
		reader.close();
		writer.flush();
		writer.close();
		writer = new PrintWriter(new BufferedWriter(new FileWriter(file)));
		writer.append(sb.toString());
		writer.flush();
		writer.close();
		temp.delete();
		return true;
	}

	private String refactorSrc(String line, Map<String, Resource> resource) {
		for (String src : resource.keySet()) {
			if (line.indexOf(src) != -1) {
				line = line.replace(src, renameSrc(src, resource.get(src)));
			}
		}
		return line;
	}

	private String renameSrc(String src, Resource resource) {
		File source  = resource.getFile();
		String stamp = generateStamp(source);
		if (resource.isWebSource()) {
			src = addQuery(src, stamp);
		} else {
			switch (this.renameType) {
			case ResourceRefactor.REPLACE_FILENAME:
				src = replaceName(src, resource, stamp);
				break;
			case ResourceRefactor.APPEND_FILENAME:
				src = appendFileName(src, resource, stamp);
				break;
			case ResourceRefactor.APPEND_AS_QUERY:
			default:
				src = addQuery(src, stamp);
			}
		}
		return src;
	}
	// 末尾添加时间戳
	private String addQuery(String src, String stamp){
		if (src.indexOf("?") == -1) {
			src = src + "?" + stamp;
		} else {
			src = src + "&" + stamp;
		}
		return src;
	}
	// 文件更名追加stamp
	private String appendFileName(String src, Resource resource, String stamp){
		String name = resource.getFileName();
		File   file = resource.getFile();
		String newFileName = name.substring(0, name.lastIndexOf("."))
				+ ResourceRefactor.CONNECT_CHAR
				+ stamp
				+ name.substring(name.lastIndexOf("."));
		src = src.replace(name, newFileName);
		file.renameTo(new File(file.getParent() + File.separator
				+ newFileName));
		return src;
	}
	// 文件更名为stamp
	private String replaceName(String src, Resource resource, String stamp){
		String name = resource.getFileName();
		File   file = resource.getFile();
		String newFileName = stamp
				+ name.substring(name.lastIndexOf("."));
		src = src.replace(name, newFileName);
		// rename the resource 
		file.renameTo(new File(file.getParent() + File.separator
				+ newFileName));
		return src;
	}
	
	private String generateStamp(File file){
		String stamp = "";
		switch (this.stampType) {
		case ResourceRefactor.MD5_STAMP:
			try {
				stamp = GenericMD5.md5(file);
			} catch (NoSuchAlgorithmException | IOException e) {
				e.printStackTrace();
			}
			break;
		case ResourceRefactor.TIME_STAMP:
			stamp = String.valueOf(System.currentTimeMillis()
					+ ResourceRefactor.FILE_INDEX++);
			break;
		default:
			stamp = "";
		}
		return stamp;
	}

}
