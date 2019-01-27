package jcrystal.preprocess.descriptions;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import jcrystal.preprocess.convertions.AnnotationResolverHolder;

public interface JIAnnotable {
	public List<JAnnotation> getAnnotations();
	public default void loadAnnotations(Annotation[] annotations) {
		Arrays.stream(annotations).forEach(a->{
			try {
				getAnnotations().add(new JAnnotation(a));
			}catch (Exception e) {
				e.printStackTrace();
				throw new NullPointerException();
			}
		});
	}
	public default boolean isJAnnotationPresent(String name) {
		return getAnnotations().stream().anyMatch(f->f.name.equals(name));
	}
	public default boolean isAnnotationPresent(Class<? extends Annotation> clase) {
		return getAnnotations().stream().anyMatch(f->f.name.equals(clase.getName()));
	}
	public default JAnnotation getJAnnotation(String name) {
		return getAnnotations().stream().filter(f->f.name.equals(name)).findFirst().orElse(null);
	}
	public default <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		return AnnotationResolverHolder.CUSTOM_RESOLVER.resolveAnnotation(annotationClass, this);
	}
	public default void ifJAnnotation(Class<? extends Annotation> clase, Consumer<JAnnotation> consumer) {
		getAnnotations().stream().filter(f->f.name.equals(clase.getName())).findFirst().ifPresent(consumer);
	}
	public default <A extends Annotation> void ifAnnotation(Class<A> clase, Consumer<A> consumer) {
		getAnnotations().stream().filter(f->f.name.equals(clase.getName())).findFirst().ifPresent(a->consumer.accept(getAnnotation(clase)));
	}
}
