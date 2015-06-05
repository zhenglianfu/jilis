package fe.toolkit;

import java.io.File;
import java.lang.reflect.Field;

public class Resource {

	private String originalPath;

	private String fileName;

	private File file;

	private String absPath;

	private String fileType;
	
	private boolean isWebSource;

	public Resource() {
	}

	public String getFileName() {
		return fileName;
	}

	public File getFile() {
		return file;
	}

	public String getAbsPath() {
		return absPath;
	}

	public String getFileType() {
		return fileType;
	}

	public String getOriginalPath() {
		return originalPath;
	}
	
	public boolean isWebSource() {
		return isWebSource;
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Field[] fields = this.getClass().getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			Field field = fields[i];
			sb.append("\"" + field.getName() + "\"");
			sb.append(":");
			try {
				Object obj = field.get(this);
				sb.append("\"" + obj.toString().replace("\\", "/") + "\"");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}

	public static Resource parseURI(String absUri, String origin) {
		Resource resource = new Resource();
		resource.originalPath = origin;
		absUri = absUri.substring(0, absUri.indexOf("?") == -1 ? absUri.length() : absUri.indexOf("?"));
		resource.absPath = absUri;
		resource.file = new File(absUri);
		int slashIndex = absUri.lastIndexOf("/") == -1 ? 0 : absUri
				.lastIndexOf("/");
		resource.fileType    = absUri.substring(absUri.lastIndexOf(".") + 1);
		resource.fileName    = absUri.substring(slashIndex + 1);
		resource.isWebSource = absUri.toLowerCase().indexOf("http://") == 0;
		return resource;
	}
	
	public static Resource parseURI(String absUri){
		return parseURI(absUri, absUri);
	}

}
