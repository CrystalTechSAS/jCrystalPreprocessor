package jcrystal.preprocess.convertions;

import java.lang.annotation.Annotation;

import jcrystal.preprocess.descriptions.JAnnotation;
import jcrystal.preprocess.descriptions.JIAnnotable;

public interface IAnnotationResolver {
	public <A extends Annotation> A resolveAnnotation(Class<A> annotationClass, JIAnnotable element);
}
