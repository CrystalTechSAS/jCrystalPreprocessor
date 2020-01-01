package jcrystal.preprocess.responses;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jcrystal.configs.clients.ClientType;
import jcrystal.configs.clients.ResourceType;

public class OutputFile implements Serializable{
	private static final long serialVersionUID = -2642574448549194694L;

	public String clientId;
	public ClientType type;
	public ResourceType resourceType;
	
	public String destPath;
	public List<String> content;
	public OutputFile(String clientId, ClientType type, String destPath, List<String> content) {
		this.clientId = clientId;
		this.destPath = destPath;
		this.type = type;
		this.content = new ArrayList<>(content);
	}
	public OutputFile(String clientId, ClientType type, String destPath, String content) {
		this.clientId = clientId;
		this.destPath = destPath;
		this.type = type;
		this.content = new ArrayList<>(Arrays.asList(content.split(System.lineSeparator())));
	}
	public OutputFile setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
		return this;
	}
}
