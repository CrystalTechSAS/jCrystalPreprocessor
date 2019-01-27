package jcrystal.preprocess.descriptions;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import jcrystal.preprocess.utils.Resolver;
import jcrystal.reflection.annotations.Post;

public class JType implements JIAnnotable, Serializable, IJType{
	private static final long serialVersionUID = -875202507362620017L;

	public String name;
	public String simpleName;
	public String packageName;
	public boolean isArray;
	public boolean isEnum;
	boolean primitive;
	boolean nullable;
	public List<IJType> innerTypes = new ArrayList<>();
	private Class<?> serverType;
	protected JType(Class<?> f, Type genericType) {
		this(f);
		if(!isArray && !isEnum && genericType != null) {
			if(genericType instanceof  ParameterizedType) {
				for(Type tipo : ((ParameterizedType) genericType).getActualTypeArguments()) {
					if(tipo instanceof ParameterizedType) {
						ParameterizedType pType = (ParameterizedType)tipo;
						innerTypes.add(JTypeSolver.load((Class<?>)pType.getRawType(), pType));
					}
					else {
						innerTypes.add(JTypeSolver.load((Class<?>)tipo, null));
					}
				}	
			}
		}
	}
	JType() {}
	public JType(String name) {
		this.name = name;
		this.nullable = true;
		if(name.indexOf('.') >= 0) {
			this.simpleName = name.substring(name.lastIndexOf(".") + 1);
			this.packageName = name.substring(0, name.lastIndexOf("."));
		}else {
			this.simpleName = name;
			this.packageName = null;
		}
	}
	protected JType(Class<?> f) {
		if(f.isAnnotationPresent(Post.class))
			JTypeSolver.EXTRA_CLASES.add(f);
		name = f.getName();
		simpleName = f.getSimpleName();
		if(f.getPackage() != null)
			packageName = f.getPackage().getName();
		isArray = f.isArray();
		isEnum = f.isEnum();
		primitive = f.isPrimitive();
		nullable = !f.isPrimitive();
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
	public List<IJType> getInnerTypes() {
		return innerTypes;
	}
	@Override
	public JClass resolve() {
		JClass ret = Resolver.loadClass(name);
		if(ret == null)
			throw new NullPointerException("Clase no encontrada: " + name);
		return ret;
	}
	@Override
	public final boolean isEnum() {
		return isEnum;
	}
	@Override
	public final boolean isArray() {
		return isArray;
	}
	@Override
	public final boolean isPrimitive() {
		return primitive;
	}
	@Override
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
	@Override
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
	@Override
	public final boolean isSubclassOf(Class<?> clase) {
		if(serverType != null)
			return clase.isAssignableFrom(serverType);
		return !isPrimitive() && (is(clase) || Resolver.subclassOf(this, clase));
	}
	@Override
	public final boolean isSubclassOf(IJType clase) {
		return !isPrimitive() && (is(clase) || Resolver.subclassOf(this, clase));
	}
	@Override
	public final boolean is(Class<?> ... classes) {
		for(Class<?> c : classes)if(c.getName().equals(name))return true;
		return false;
	}
	@Override
	public final boolean is(IJType ... classes) {
		for(IJType c : classes)if(c.getName().equals(name))return true;
		return false;
	}
	@Override
	public final String getName() {
		return name;
	}
	@Override
	public final String getSimpleName() {
		return simpleName;
	}
	@Override
	public JPackage getPackage() {
		return Resolver.PACKAGES.get(packageName);
	}
	@Override
	public final String getPackageName() {
		return packageName;
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
	@Override
	public String toString() {
		if(getInnerTypes().isEmpty())
			return getName();
		else if(isArray())
			return getInnerTypes().get(0)+"[]";
		else
			return getName()+":<" + getInnerTypes().stream().map(f->f.toString()).collect(Collectors.joining(", ")) + ">";
	}
	@Override
	public boolean nullable() {
		return nullable;
	}
}
