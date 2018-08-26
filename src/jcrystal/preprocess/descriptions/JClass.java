package jcrystal.preprocess.descriptions;

import java.io.File;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jcrystal.preprocess.convertions.AnnotationResolverHolder;
import jcrystal.preprocess.main.ClassProcesor;
import jcrystal.preprocess.utils.Resolver;

public class JClass extends JType implements JIAnnotable, JIHasModifiers, Serializable{
	private static final long serialVersionUID = 143568675432L;
	boolean isStatic;
	boolean inner;
	public int modifiers;
	public List<JType> interfaces = new ArrayList<>();
	public List<JVariable> attributes = new ArrayList<>();
	public List<JMethod> methods = new ArrayList<>();
	public List<JAnnotation> annotations= new ArrayList<>();
	public JEnum enumData;
	public JType superClass;
	public JType declaringClass;
	
	public JClass(Class<?> clase){
		super(clase);
		modifiers = clase.getModifiers();
		name = clase.getName();
		simpleName = clase.getSimpleName();
		isEnum = clase.isEnum();
		isStatic = Modifier.isStatic(clase.getModifiers());
		inner = clase.isMemberClass();
		if(clase.getSuperclass() != null)
			superClass = JType.load(clase.getSuperclass(), clase.getGenericSuperclass());
		if(clase.getDeclaringClass() != null)
			declaringClass = JType.load(clase.getDeclaringClass(), null);
		Class<?>[] ifaces = clase.getInterfaces();
		for(int e = 0; e < ifaces.length; e++)
			interfaces.add(JType.load(ifaces[e], clase.getGenericInterfaces()[e]));
		packageName = clase.getPackage().getName();
		Arrays.stream(clase.getDeclaredFields())/*.sorted((c1,c2)->c1.getName().compareTo(c2.getName()))*/.forEach(f->{
			attributes.add(new JVariable(f));
		});
		Arrays.stream(clase.getDeclaredMethods()).sorted((c1,c2)->c1.getName().compareTo(c2.getName())).forEach(m->{
			if(!m.getName().startsWith("lambda$"))
				methods.add(new JMethod(m));
		});
		if(clase.getSimpleName().startsWith("Manager"))
			Arrays.stream(clase.getDeclaredClasses()).forEach(c->{
				System.out.println("Sub clase "  + c.getName());
				ClassProcesor.loadClassInfo(c);
			});
		loadAnnotations(clase.getAnnotations());
		if(isEnum)
			enumData = new JEnum(clase);
	}
	public boolean isInner() {
		return inner;
	}
	@Override
	public int getModifiers() {
		return modifiers;
	}
	@Override
	public List<JAnnotation> getAnnotations() {
		return annotations;
	}
	
	public File getFile(File srcFile){
		return new File(srcFile, name.replace(".", "/")+".java");
	}
	public JType getSuperClass() {
		return superClass;
	}
	public JType getDeclaringClass() {
		return declaringClass;
	}
	public boolean isAnnotationPresent(Class<? extends Annotation> clase) {
		boolean v = getAnnotations().stream().anyMatch(f->f.name.equals(clase.getName()));
		return v;
	}
	public <A extends Annotation> A getAnnotation(Class<A> annotationClass) {
		A b = AnnotationResolverHolder.CUSTOM_RESOLVER.resolveAnnotation(annotationClass, this);
		return b;
	}
	public boolean isAnnotationPresentOnAncestor(String annotationName) {
		return isJAnnotationPresent(annotationName) || (getPackage()!=null && getPackage().isJAnnotationPresent(annotationName));
	}
}
