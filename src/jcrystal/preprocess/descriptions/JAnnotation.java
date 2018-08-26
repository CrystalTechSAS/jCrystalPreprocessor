package jcrystal.preprocess.descriptions;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class JAnnotation implements Serializable{
	private static final long serialVersionUID = 1435242554L;
	public String name;
	public String simpleName;
	public Map<String, Object> values = new TreeMap<>();
	
	public JAnnotation(Class<? extends Annotation> annotation) {
		this.name = annotation.getName();
		this.simpleName = annotation.getSimpleName();
	}
	public JAnnotation(Annotation annotation){
		Class<?> clase = annotation.annotationType();
		name = clase.getName();
		simpleName = clase.getSimpleName();
		Method ms[] = clase.getDeclaredMethods();
		Arrays.sort(ms, (c1,c2)->c1.getName().compareTo(c2.getName()));
		for(Method m : ms)
			try {
				Object val = m.invoke(annotation);
				if(val != null) {
					if(m.getReturnType().isArray()) {
						if(m.getReturnType().getComponentType() == String.class)
							values.put(m.getName(), val);
						else if(m.getReturnType().getComponentType() == Class.class)
							values.put(m.getName(), Arrays.stream((Class<?>[])val).map(f->f.getName()).toArray(f->new String[f]));
						else if(m.getReturnType().getComponentType().isAnnotation())
							values.put(m.getName(), Arrays.stream((Annotation[])val).map(JAnnotation::new).toArray(f->new JAnnotation[f]));
					}
					else if(m.getReturnType().isEnum())
						values.put(m.getName(), m.getReturnType().getMethod("name").invoke(val));
					else if(m.getReturnType().isAnnotation())
						values.put(m.getName(), new JAnnotation((Annotation)val));
					else if(m.getReturnType() == Class.class)
						values.put(m.getName(), ((Class<?>)val).getName());
					else
						values.put(m.getName(), val.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new NullPointerException(); 
			}
	}
	public String getName() {
		return name;
	}
	public String getSimpleName() {
		return simpleName;
	}
	@Override
	public String toString() {
		if(values.isEmpty())
			return "@@"+name+"()";
		return "@@"+name+"("+values.entrySet().stream().map(f->f.getKey()+"="+f.getValue()).collect(Collectors.joining(", "))+")";
	}
}
