package jcrystal.preprocess.descriptions;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jcrystal.preprocess.main.ClassProcesor;
import jcrystal.preprocess.utils.Resolver;
import jcrystal.reflection.annotations.Post;

public class JType implements Comparable<JType>, JIAnnotable, Serializable{
	private static Map<String, JType> SIMPLE_TYPES = new TreeMap<>();
	public static JType load(Class<?> clase, Type genericType) {
		if(genericType == null || genericType instanceof  Class<?> || (genericType instanceof  ParameterizedType && ((ParameterizedType) genericType).getActualTypeArguments().length == 0)) {
			JType ret = SIMPLE_TYPES.get(clase.getName()); 
			if(ret == null)
				SIMPLE_TYPES.put(clase.getName(), ret = new JType(clase));
			return ret;
		}else{
			JType ret = SIMPLE_TYPES.get(genericType.getTypeName()); 
			if(ret == null)
				SIMPLE_TYPES.put(genericType.getTypeName(), ret = new JType(clase, genericType));
			return ret;
			
		}
	}
	public static Set<Class<?>> EXTRA_CLASES = new TreeSet<>((c1,c2)->c1.getName().compareTo(c2.getName()));
	public static void loadExtras() {
		while(!EXTRA_CLASES.isEmpty()) {
			List<Class<?>> clases = EXTRA_CLASES.stream().filter(f->!Resolver.CLASES.containsKey(f.getName())).collect(Collectors.toList());
			EXTRA_CLASES.clear();
			clases.forEach(f->{
				System.out.println("Load extra " +f);
				ClassProcesor.loadClassInfo(f);
			});
		}
	}
	private static final long serialVersionUID = -875202507362620017L;

	public String name;
	public String simpleName;
	public String packageName;
	public boolean isArray;
	public boolean isEnum;
	boolean primitive;
	public List<JType> innerTypes = new ArrayList<>();
	private Class<?> serverType;
	private JType(Class<?> f, Type genericType) {
		this(f);
		if(!isArray && !isEnum && genericType != null) {
			if(genericType instanceof  ParameterizedType) {
				for(Type tipo : ((ParameterizedType) genericType).getActualTypeArguments()) {
					if(tipo instanceof ParameterizedType) {
						ParameterizedType pType = (ParameterizedType)tipo;
						innerTypes.add(load((Class<?>)pType.getRawType(), pType));
					}
					else {
						innerTypes.add(load((Class<?>)tipo, null));
					}
				}	
			}
		}
	}
	public JType(String name) {
		this.name = name;
		this.simpleName = name.substring(name.lastIndexOf(".") + 1);
		this.packageName = name.substring(0, name.lastIndexOf("."));
	}
	protected JType(Class<?> f) {
		if(f.isAnnotationPresent(Post.class))
			EXTRA_CLASES.add(f);
		name = f.getName();
		simpleName = f.getSimpleName();
		if(f.getPackage() != null)
			packageName = f.getPackage().getName();
		isArray = f.isArray();
		isEnum = f.isEnum();
		primitive = f.isPrimitive();
		if(!name.startsWith("java.")) {
			if(isArray)
				innerTypes.add(new JType(f.getComponentType(), (Type)null));
		}
		CodeSource src = f.getProtectionDomain().getCodeSource();
		if(!isArray) {
			if (src != null) {
				if(src.getLocation().toString().endsWith("appengine-api.jar") || src.getLocation().toString().endsWith("json-20160212.jar") || src.getLocation().toString().endsWith("appengine-gcs-client-0.7.jar"))
					serverType = f;
			}else {
				serverType = f;
			}
		}
	}
	public JClass resolve() {
		JClass ret = Resolver.loadClass(name);
		if(ret == null)
			throw new NullPointerException();
		return ret;
	}
	public final boolean isEnum() {
		return isEnum;
	}
	public final boolean isArray() {
		return isArray;
	}
	public final boolean isPrimitive() {
		return primitive;
	}
	/**
	 * Este método se sobreescribe porque los tipos no cuentan con anotaciones, las anotaciones en las en las JClass
	 */
	public boolean isAnnotationPresent(Class<? extends Annotation> clase) {
		if(serverType != null)
			return serverType.isAnnotationPresent(clase);
		JClass c = Resolver.loadClass(name);
		if(c != null)
			return c.isAnnotationPresent(clase);
		if(!primitive)
			try {
				return Class.forName(name).isAnnotationPresent(clase);
			} catch (ClassNotFoundException e) {
				System.err.println(name);
			}
		return false;
	}
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		if(serverType != null)
			return serverType.getAnnotation(annotationClass);
		JClass c = Resolver.loadClass(name);
		if(c != null)
			return c.getAnnotation(annotationClass);
		if(!primitive)
			try {
				return Class.forName(name).getAnnotation(annotationClass);
			} catch (ClassNotFoundException e) {
				System.err.println(name);
			}
		return null; 
	}
	public final boolean isSubclassOf(Class<?> clase) {
		if(serverType != null)
			return clase.isAssignableFrom(serverType);
		return !isPrimitive() && (is(clase) || Resolver.subclassOf(this, clase));
	}
	public final boolean isSubclassOf(JType clase) {
		return !isPrimitive() && (is(clase) || Resolver.subclassOf(this, clase));
	}
	public final boolean is(Class<?> ... classes) {
		for(Class<?> c : classes)if(c.getName().equals(name))return true;
		return false;
	}
	public final boolean is(JType ... classes) {
		for(JType c : classes)if(c.getName().equals(name))return true;
		return false;
	}
	
	@Override
	public int compareTo(JType o) {
		return name.compareTo(o.name);
	}
	public final String getName() {
		return name;
	}
	public final String getSimpleName() {
		return simpleName;
	}
	public JPackage getPackage() {
		return Resolver.PACKAGES.get(packageName);
	}
	public final String getPackageName() {
		return packageName;
	}
	public String toFullString() {
		if(innerTypes.isEmpty())
			return name;
		else if(isArray)
			return innerTypes.get(0).toFullString()+"[]";
		else
			return name+"<" + innerTypes.stream().map(f->f.toFullString()).collect(Collectors.joining(", ")) + ">";
	}
	public JType getObjectType(){
        switch (simpleName){
            case "int": return load(Integer.class, null);
            case "long": return load(Long.class, null);
            case "double": return load(Double.class, null);
            case "float": return load(Float.class, null);
            case "boolean": return load(Boolean.class, null);
            case "char": return load(Character.class, null);
            case "byte": return load(Byte.class, null);
            case "short": return load(Short.class, null);
        }
        return this;
    }
	public boolean isPrimitiveObjectType(){
        return is(Integer.class, Long.class, Double.class, Float.class, Boolean.class, Character.class, Byte.class, Short.class);
	}
	@Override
	public String toString() {
		return toFullString();
	}
	@Override
	public List<JAnnotation> getAnnotations() {
		if(serverType != null)
			return Collections.EMPTY_LIST;
		JClass clase = Resolver.loadClass(getName());
		if(clase == null)
			return Collections.EMPTY_LIST;
		return clase.annotations;
	}
	public JType createListType() {
		JType ret = new JType(List.class);
		ret.innerTypes.add(this);
		return ret;
	}
}
