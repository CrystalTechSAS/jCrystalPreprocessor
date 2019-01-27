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

public class WrapStringJType implements Serializable, IJType{
	private static final long serialVersionUID = -875202507362620017L;
	private String type;
	public WrapStringJType(String type) {
		this.type = type;
	}
	@Override
	public JClass resolve() {
		throw new NullPointerException();
	}
	@Override
	public boolean isEnum() {
		throw new NullPointerException();
	}
	@Override
	public boolean isArray() {
		throw new NullPointerException();
	}
	@Override
	public boolean isPrimitive() {
		throw new NullPointerException();
	}
	@Override
	public boolean isSubclassOf(Class<?> clase) {
		throw new NullPointerException();
	}
	@Override
	public boolean isSubclassOf(IJType clase) {
		throw new NullPointerException();
	}
	@Override
	public boolean is(Class<?>... classes) {
		throw new NullPointerException();
	}
	@Override
	public boolean is(IJType... classes) {
		throw new NullPointerException();
	}
	@Override
	public String getName() {
		return type;
	}
	@Override
	public String getSimpleName() {
		return type;
	}
	@Override
	public JPackage getPackage() {
		throw new NullPointerException();
	}
	@Override
	public String getPackageName() {
		throw new NullPointerException();
	}
	@Override
	public List<IJType> getInnerTypes() {
		throw new NullPointerException();
	}
	@Override
	public List<JAnnotation> getAnnotations() {
		throw new NullPointerException();
	}
	@Override
	public boolean isAnnotationPresent(Class<? extends Annotation> clase) {
		throw new NullPointerException();
	}
	@Override
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		throw new NullPointerException();
	}
	@Override
	public String toString() {
		return type;
	}
	@Override
	public boolean nullable() {
		throw new NullPointerException();
	}
}
