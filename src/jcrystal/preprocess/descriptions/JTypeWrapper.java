package jcrystal.preprocess.descriptions;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Collectors;

public class JTypeWrapper implements JIAnnotable, Serializable, IJType{
	private static final long serialVersionUID = -875202507362620017L;

	private IJType wrappedType;
	private String name;
	private String simpleName;
	protected JTypeWrapper(IJType type) {
		this.wrappedType = type;
		name = type.getName();
		simpleName = type.getSimpleName();
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setSimpleName(String simpleName) {
		this.simpleName = simpleName;
	}
	@Override
	public JClass resolve() {
		return wrappedType.resolve();
	}
	@Override
	public final boolean isEnum() {
		return wrappedType.isEnum();
	}
	@Override
	public final boolean isArray() {
		return wrappedType.isArray();
	}
	@Override
	public final boolean isPrimitive() {
		return wrappedType.isPrimitive();
	}
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> clase) {
		return wrappedType.isAnnotationPresent(clase);
	}
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return wrappedType.getAnnotation(annotationClass); 
	}
	@Override
	public final boolean isSubclassOf(Class<?> clase) {
		return wrappedType.isSubclassOf(clase);
	}
	@Override
	public final boolean isSubclassOf(IJType clase) {
		return wrappedType.isSubclassOf(clase);
	}
	@Override
	public final boolean is(Class<?> ... classes) {
		return wrappedType.is(classes);
	}
	@Override
	public final boolean is(IJType ... classes) {
		return wrappedType.is(classes);
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
		return wrappedType.getPackage();
	}
	@Override
	public final String getPackageName() {
		return wrappedType.getPackageName();
	}
	@Override
	public List<JAnnotation> getAnnotations() {
		return wrappedType.getAnnotations();
	}
	@Override
	public List<IJType> getInnerTypes() {
		return wrappedType.getInnerTypes();
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
		return wrappedType.nullable();
	}
}
