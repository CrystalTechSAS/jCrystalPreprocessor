package jcrystal.preprocess.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OutputSection implements Serializable{
	private static final long serialVersionUID = -8733612658456678855L;
	public String id;
	public String destPath;
	public List<String> content;
	public boolean global;
	public OutputSection(String destPath, String id, List<String> content) {
		this.destPath = destPath;
		this.id = id;
		this.content = new ArrayList<>(content);
		
	}
	public OutputSection(String destPath, String id, String content) {
		this.destPath = destPath;
		this.id = id;
		this.content = Arrays.asList(content.split(System.lineSeparator()));
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
