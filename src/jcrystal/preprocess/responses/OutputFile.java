package jcrystal.preprocess.responses;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import jcrystal.clients.ClientType;
import jcrystal.clients.ResourceType;

public class OutputFile implements Serializable{
	private static final long serialVersionUID = -2642574448549194694L;

	public String clientId;
	public ClientType type;
	public ResourceType resourceType;
	
	public String destPath;
	public String content;
	public List<OutputSection> sections = new ArrayList<>(); 
	public OutputFile(String clientId, ClientType type, String destPath, List<String> content) {
		this.clientId = clientId;
		this.destPath = destPath;
		this.type = type;
		this.content = content.stream().collect(Collectors.joining(System.lineSeparator()));
	}
	public OutputFile(String clientId, ClientType type, String destPath, String content) {
		this.clientId = clientId;
		this.destPath = destPath;
		this.type = type;
		this.content = content;
	}
	public OutputFile setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
		return this;
	}
}
