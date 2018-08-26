package jcrystal.preprocess.responses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import jcrystal.clients.ClientType;
import jcrystal.clients.ResourceType;

public class OutputSection implements Serializable{
	private static final long serialVersionUID = -8733612658456678855L;
	public String id;
	public String destPath;
	public String content;
	public boolean global;
	public OutputSection(String destPath, String id, List<String> content) {
		this.destPath = destPath;
		this.id = id;
		this.content = content.stream().collect(Collectors.joining(System.lineSeparator()));
		
	}
	public OutputSection(String destPath, String id, String content) {
		this.destPath = destPath;
		this.id = id;
		this.content = content;
	}
	public OutputSection setGlobal(boolean global) {
		this.global = global;
		return this;
	}
}
