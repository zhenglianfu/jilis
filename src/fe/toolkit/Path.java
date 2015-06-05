package fe.toolkit;


public class Path {
	
	public static final String separator = "/";
	
	private String root = "";
	
	private String parent;

	public String caculatePath(String child){
		String path = this.parent + "/";
		path = path.replace("\\", "/");
		if (child != null) {
			if (child.indexOf("/") == 0) {
				path = this.root + child;
			} else if (child.indexOf(".") == 0) {
				while(child.indexOf(".") == 0){
					if (child.indexOf("../") == 0) {
						path = path.substring(0, path.lastIndexOf("/", path.length() > 0 ? path.length() - 2 : -1) + 1);
						child = child.substring(3);
						path += child;
					} else if (child.indexOf("./") == 0) {
						child = child.substring(2);
						path += child;
					}
				}
			} else if (child.indexOf("http://") == 0) {
				path = child;
			} else {
				path += child;
			}
		}
		return path; 
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}
}
