package fe.toolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator 资源文件重命名
 */
public class ResourceRefactor {
	
	public static final int TIME_STAMP = 0;

	public static final int MD5_STAMP = 1;

	public static final int RENAME_NAME = 2;

	public static final int APPEND_NAME = 3;
	
	private static Map<String, ResourceRefactor> instanceMap = new HashMap<String, ResourceRefactor>();
	
	private int stampType;
	
	private int renameType; 
	
	private String dirPath;     // 执行目录
	
	private String docmentRoot; // 根目录
	
	private Map<String, Refactor> mapper;   // 资源映射表
	
	private ResourceRefactor(){
		mapper = new HashMap<String, Refactor>();
	}
	
	public static ResourceRefactor getInstance(int stampType, int renameType){
		return ResourceRefactor.getInstance("", stampType, renameType);
	}
	// only one dirPath in the memory 
	public static ResourceRefactor getInstance(String dirPath, int stampType, int renameType){
		dirPath = dirPath == null ? "" : dirPath;
		if (instanceMap.containsKey(dirPath)) {
			return instanceMap.get(dirPath);
		} else {
			ResourceRefactor instance = new ResourceRefactor();
			instance.dirPath = dirPath;
			instance.stampType = stampType;
			instance.renameType = renameType;
			return instance;
		}
	}
	
	public String refactor(String dir, String path){
		
		return null;
	}
	
	public String refactor(String path){
		return this.refactor(this.dirPath, path);
	}
	
	public class Refactor{
		
	}
	
}
