package jcrystal.preprocess.descriptions;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class JVariable implements JIAnnotable, Serializable{
	private static final long serialVersionUID = 2251144499897925662L;
	public JType type;
	public String name;
	public int modifiers;
	List<JAnnotation> annotations= new ArrayList<>();
	public String staticDefaultValue;
	public JVariable(int modifiers, JType type, String name) {
		this.modifiers = modifiers;
		this.type = type;
		this.name = name;
	}
	public JVariable(Parameter p) {
		name = p.getName();
		type = JType.load(p.getType(), p.getParameterizedType());
		modifiers = p.getModifiers();
		loadAnnotations(p.getAnnotations());
	}
	public JVariable(Field f) {
		name = f.getName();
		type = JType.load(f.getType(), f.getGenericType());
		modifiers = f.getModifiers();
		Arrays.stream(f.getAnnotations()).sorted((c1,c2)->c1.annotationType().getName().compareTo(c2.annotationType().getName())).forEach(a->{
			try {
				annotations.add(new JAnnotation(a));				
			}catch (Exception e) {
				throw new NullPointerException();
			}
		});
		
		if(Modifier.isStatic(modifiers)) {
			try {
				f.setAccessible(true);
				Object defaultValue = f.get(null);
				if(defaultValue != null) {
					staticDefaultValue = defaultValue.toString();
					if(getType().isPrimitive()) {
						if(f.getType() == long.class && defaultValue.equals(new Long(0)))
							staticDefaultValue = null;		
						else if(f.getType() == int.class && defaultValue.equals(new Integer(0)))
							staticDefaultValue = null;
						else if(f.getType() == double.class && defaultValue.equals(new Double(0)))
							staticDefaultValue = null;
						else if(f.getType() == boolean.class && defaultValue.equals(new Boolean(false)))
							staticDefaultValue = null;
					}else if(getType().isEnum()) {
						staticDefaultValue = f.getType().getSimpleName()+"."+f.getType().getMethod("name").invoke(defaultValue).toString();
					}else if(defaultValue.equals(new Long(0)))
						staticDefaultValue = null;		
					else if(defaultValue.equals(new Integer(0)))
						staticDefaultValue = null;
					else if(defaultValue.equals(new Double(0)))
						staticDefaultValue = null;
					else if(defaultValue.equals(new Boolean(false)))
						staticDefaultValue = null;

				}
				if(defaultValue != null && f.getType().isEnum())
					defaultValue = f.getType().getSimpleName()+"."+f.getType().getMethod("name").invoke(defaultValue);
			} catch (Exception e) {}
		}
	}
	@Override
	public List<JAnnotation> getAnnotations() {
		return annotations;
	}
	public JType getType() {
		return type;
	}
	public String getName() {
		return name;
	}
	public boolean isPublic() {
		return Modifier.isPublic(modifiers);
	}
	public boolean isStatic() {
		return Modifier.isStatic(modifiers);
	}
	public boolean isFinal() {
		return Modifier.isFinal(modifiers);
	}
	public boolean isPrivate() {
		return Modifier.isPrivate(modifiers);
	}
	public boolean isProtected() {
		return Modifier.isProtected(modifiers);
	}
	public int getModifiers() {
		return modifiers;
	}
}
