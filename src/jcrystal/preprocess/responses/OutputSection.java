package jcrystal.preprocess.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutputSection extends ClassOperation implements Serializable{
	private static final long serialVersionUID = -8733612658456678855L;
	public String id;
	public boolean global;
	public OutputSection(String destPath, String id, List<String> content) {
		super(ClassOperationType.ADD_SECTION, destPath, new ArrayList<>(content));
		this.id = id;
	}
	public OutputSection(String destPath, String id, String content) {
		super(ClassOperationType.ADD_SECTION, destPath, Arrays.asList(content.split(System.lineSeparator())));
		this.id = id;
	}
	public OutputSection setGlobal(boolean global) {
		this.global = global;
		return this;
	}
	public static boolean isStartTag(String line){
		line = line.trim();
		return line.startsWith("/* ") && line.endsWith(" */") && !line.equals("/* END */");
	}
	public static boolean isEndTag(String line){
		line = line.trim();
		return line.equals("/* END */");
	}
	public static String getTagName(String line) {
		line = line.trim();
		return line.substring(2, line.length() - 2).trim();
	}
	    
}
