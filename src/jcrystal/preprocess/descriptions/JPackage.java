package jcrystal.preprocess.descriptions;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public class JPackage implements JIAnnotable, Serializable{
	private static final long serialVersionUID = -5704843477884875405L;
	String name;
	List<JAnnotation> annotations= new ArrayList<>();
	public JPackage(Package paquete){
		name = paquete.getName();
		for(Annotation a : paquete.getAnnotations())
			annotations.add(new JAnnotation(a));
	}
	@Override
	public List<JAnnotation> getAnnotations() {
		return annotations;
	}
	public String getName() {
		return name;
	}
}
