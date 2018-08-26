package jcrystal.preprocess.utils;

import java.lang.annotation.Annotation;
import java.util.TreeMap;

import jcrystal.preprocess.descriptions.JClass;
import jcrystal.preprocess.descriptions.JPackage;
import jcrystal.preprocess.descriptions.JType;

public class Resolver {
	public static TreeMap<String, JClass> CLASES = new TreeMap<>();
	public static TreeMap<String, JPackage> PACKAGES = new TreeMap<>();
	public static JClass loadClass(String name) {
		return CLASES.get(name);
	}
	public static JPackage loadPackage(String name) {
		JPackage ret = PACKAGES.get(name);; 
		if(ret == null)
			PACKAGES.put(name, ret = new JPackage(Package.getPackage(name)));
		return ret;
		
	}
	public static boolean subclassOf(JType jtype, Class<?> clase) {
		if(CLASES.containsKey(jtype.name))
			return subclassOf(CLASES.get(jtype.name), clase);
		try {
			Class<?> c = Class.forName(jtype.name);
			return clase.isAssignableFrom(c);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static boolean subclassOf(JType jtype, JType clase) {
		if(CLASES.containsKey(jtype.name))
			return subclassOf(CLASES.get(jtype.name), clase);
		return false;
	}
	public static boolean subclassOf(JClass jtype, JType clase) {
		if(jtype.superClass != null && jtype.superClass.is(clase))
			return true;
		return jtype.interfaces.stream().anyMatch(f->f.isSubclassOf(clase));
	}
	public static boolean subclassOf(JClass jclase, Class<?> clase) {
		if(clase.getName().equals(jclase.name))
			return true;
		if(subclassOf(jclase.superClass, clase))
			return true;
		return jclase.interfaces.stream().anyMatch(f->subclassOf(f, clase));
	}
	public static boolean isAnnotationPresent(JType jtype, Class<? extends Annotation> annotation) {
		if(CLASES.containsKey(jtype.name))
			return CLASES.get(jtype.name).isAnnotationPresent(annotation);
		try {
			Class<?> c = Class.forName(jtype.name);
			return c.isAnnotationPresent(annotation);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
}
