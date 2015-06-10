package fe.toolkit;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator
 * @version 1.0 alpha Front End Developer Tool made by JAVA language
 */
public class Main {

	private boolean rename;

	private boolean md5;

	private boolean isKeepFileName;

	private Map<String, Map<String, Resource>> resourceMap;

	private String root;

	private String dir;

	private String buildDir;

	private String[] resourceTypes;

	private String[] cssTypes;

	private String[] htmlTypes;

	public Main readProperties() throws IOException {
		this.rename = Boolean.valueOf(Configer.get(Configer.PROPERTIES_RENAME));
		this.md5 = Boolean.valueOf(Configer.get(Configer.PROPERTIES_MD5));
		this.isKeepFileName = Boolean.valueOf(Configer
				.get(Configer.PROPERTIES_KEEP_FILENAME));
		this.resourceTypes = Configer.get(Configer.PROPERTIES_RESOURCE).split(
				"\\|");
		this.htmlTypes = Configer.get(Configer.PROPERTIES_HTML).split("\\|");
		this.cssTypes = Configer.get(Configer.PROPERTIES_CSS).split("\\|");
		this.buildDir = Configer.get(Configer.PROPERTIES_PUBLIC);
		// dir&root setting [classPath]
		String classPath = (new File("")).getAbsolutePath();
		this.root = Configer.get(Configer.PROPERTIES_ROOT);
		this.dir = Configer.get(Configer.PROPERTIES_DIR);
		if (Util.isEmpty(this.root)) {
			this.root = classPath;
		}
		if (Util.isEmpty(dir)) {
			this.dir = classPath;
		}
		this.copyToBulidDir();
		this.resourceMap = new HashMap<String, Map<String, Resource>>();
		return this;
	}

	public void printResource() throws IOException {
		BufferedWriter buffWriter = new BufferedWriter(new FileWriter(new File(
				this.buildDir + File.separator + "resourceRefer.json")));
		StringBuilder sb = new StringBuilder();
		sb.append("{\n");
		for (String fileName : this.resourceMap.keySet()) {
			sb.append("\t\"" + fileName.replace("\\", "/") + "\":");
			sb.append("{");
			for (String referName : this.resourceMap.get(fileName).keySet()) {
				sb.append("\n\t\t\"" + referName.replace("\\", "/") + "\":");
				sb.append(this.resourceMap.get(fileName).get(referName)
						.toString());
				sb.append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("\n\t},\n");
		}
		sb.delete(sb.length() - 2, sb.length());
		sb.append("\n}");
		buffWriter.write(sb.toString());
		buffWriter.flush();
		buffWriter.close();
		System.out.println(this.buildDir + "//"
				+ "resourceRefer.json created -> " + sb.toString());
	}

	public Map<String, Map<String, Resource>> scannerHtml() throws IOException {
		return scannerHtml(new File(this.buildDir), this.resourceMap);
	}

	public Map<String, Map<String, Resource>> scannerHtml(File root)
			throws IOException {
		if (root == null) {
			root = new File(this.buildDir);
		}
		return scannerHtml(root, new HashMap<String, Map<String, Resource>>());
	}

	public void refactorHtml() throws IOException {
		Map<String, Map<String, Resource>> referMap = this.scannerHtml();
		int stampType = this.md5 ? ResourceRefactor.MD5_STAMP
				: ResourceRefactor.TIME_STAMP;
		int renameType = 0;
		// rename/append value
		if (this.isKeepFileName) {
			renameType = ResourceRefactor.APPEND_FILENAME;
		} else if (this.rename) {
			renameType = ResourceRefactor.REPLACE_FILENAME;
		} else {
			renameType = ResourceRefactor.APPEND_AS_QUERY;
		}
		for (String filePath : referMap.keySet()) {
			ResourceRefactor refactor = ResourceRefactor.getInstance(stampType,
					renameType);
			refactor.refactor(filePath, referMap.get(filePath));
		}
	}

	private Map<String, Map<String, Resource>> scannerHtml(File root,
			Map<String, Map<String, Resource>> resourceMap) throws IOException {
		if (null == resourceMap) {
			resourceMap = new HashMap<String, Map<String, Resource>>();
		}
		File[] files = root.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			if (file.isDirectory()) {
				scannerHtml(file, resourceMap);
			} else {
				Map<String, Resource> resourceRefer = null;
				if (isHTMLFile(file)) {
					resourceRefer = scannerHtmlResource(files[i]);
				} else if (isCSSFile(file)) {
					resourceRefer = scannerCssResource(files[i]);
				} else {
					continue;
				}
				Map<String, Resource> filtedRefer = new HashMap<String, Resource>();
				for (String src : resourceRefer.keySet()) {
					Resource resource = resourceRefer.get(src);
					if (isExpectedResource(resource)) {
						filtedRefer.put(src, resource);
					}
				}
				// has resource reference
				if (filtedRefer.size() > 0) {
					resourceMap.put(file.getAbsolutePath(), filtedRefer);
				}
			}
		}
		return resourceMap;
	}

	private boolean isExpectedResource(Resource resource) {
		for (int i = 0; i < this.resourceTypes.length; i++) {
			if (this.resourceTypes[i].equals(resource.getFileType())) {
				return true;
			}
		}
		return false;
	}

	private Map<String, Resource> scannerHtmlResource(File html)
			throws IOException {
		if (this.resourceMap.containsKey(html.getAbsolutePath())) {
			return this.resourceMap.get(html.getAbsolutePath());
		}
		HtmlScanner scanner = HtmlScanner.getInstance(html);
		return scanner.parseResource(this.buildDir, html.getParent());
	}

	private Map<String, Resource> scannerCssResource(File css)
			throws IOException {
		if (this.resourceMap.containsKey(css.getAbsolutePath())) {
			return this.resourceMap.get(css.getAbsolutePath());
		}
		CssScanner scanner = CssScanner.getInstance(css);
		return scanner.parseResource(this.buildDir, css.getParent());
	}

	private void copyToBulidDir() throws IOException {
		File root = new File(this.root);
		File build = new File(this.buildDir);
		if (!root.exists()) {
			System.out.println("directory " + this.root
					+ " is not exist on your hardware");
			return;
		}
		if (!build.exists()) {
			build.mkdirs();
		}
		File files[] = root.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			File targetFile = new File(build.getAbsolutePath() + File.separator
					+ file.getName());
			if (file.getAbsolutePath().equals(build.getAbsolutePath())) {
				continue;
			}
			if (file.isDirectory()) {
				copyDir(file, targetFile);
			} else {
				copyFile(file, targetFile);
			}
		}
		System.out.println("copy to '" + this.buildDir + "' is done");
	}

	private boolean copyDir(File source, File target) throws IOException {
		if (!target.exists()) {
			target.mkdirs();
		}
		File files[] = source.listFiles();
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			File targetFile = new File(target.getAbsolutePath()
					+ File.separator + file.getName());
			if (file.isDirectory()) {
				copyDir(file, targetFile);
			} else {
				copyFile(file, targetFile);
			}
		}
		return true;
	}

	private boolean copyFile(File source, File target) throws IOException {
		if (target.exists()) {
			target.delete();
			target.createNewFile();
		}
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				source));
		BufferedOutputStream out = new BufferedOutputStream(
				new FileOutputStream(target));
		byte[] readBuffer = new byte[128];
		int len = in.read(readBuffer);
		while (len != -1) {
			out.write(readBuffer, 0, len);
			len = in.read(readBuffer);
		}
		in.close();
		out.flush();
		out.close();
		return true;
	}

	private boolean isHTMLFile(File file) {
		String fileName = file.getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (this.htmlTypes.length > 0) {
			for (int i = 0; i < this.htmlTypes.length; i++) {
				if (this.htmlTypes[i].equalsIgnoreCase(fileType)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isCSSFile(File file) {
		String fileName = file.getName();
		String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
		if (this.cssTypes.length > 0) {
			for (int i = 0; i < this.cssTypes.length; i++) {
				if (this.cssTypes[i].equalsIgnoreCase(fileType)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isRename() {
		return rename;
	}

	public boolean isMd5() {
		return md5;
	}

	public String[] getResourceTypes() {
		return resourceTypes;
	}

	public String[] getHtmlTypes() {
		return htmlTypes;
	}

	public boolean isKeepFileName() {
		return isKeepFileName;
	}

}
