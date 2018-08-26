package jcrystal.preprocess.descriptions;

import java.io.Serializable;

public class JObjectValue implements Serializable{
	private static final long serialVersionUID = 4423925406242432825L;
	String name;
	String value;
	public JObjectValue(String name, Object value) {
		this.name = name;
		this.value = value == null ? null : value.toString();
	}
}
