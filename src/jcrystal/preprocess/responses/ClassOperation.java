package jcrystal.preprocess.responses;

import java.io.Serializable;
import java.util.List;

public class ClassOperation implements Serializable{
	private static final long serialVersionUID = 4231280420761550053L;
	public ClassOperationType type;
	public String path;
	public String content;
	public List<String> contentList;
	public ClassOperation(ClassOperationType type, String path, String content) {
		this.type = type;
		this.path = path;
		this.content = content;
	}
	public ClassOperation(ClassOperationType type, String path, List<String> content) {
		this.type = type;
		this.path = path;
		this.contentList = content;
	}
	
}
