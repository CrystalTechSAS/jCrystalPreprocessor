package jcrystal.preprocess.descriptions;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class JEnum implements Serializable{
	private static final long serialVersionUID = 1342567432L;
	public final String name;
	public final List<EnumValue> valores = new ArrayList<>();
	public final Map<String, Class<?>> propiedades = new TreeMap<>();
	public JEnum(Class<?> clase) {
		this.name = clase.getSimpleName();
		try {
			for(Field f  : clase.getFields())
				if((Modifier.isFinal(f.getModifiers()) || f.getName().equals("id")) && !Modifier.isStatic(f.getModifiers()) )
					propiedades.put(f.getName(), f.getType());
			propiedades.put("rawName", String.class);
		} catch (Exception ex) {throw new NullPointerException(ex.getMessage());}
		for (Object o : clase.getEnumConstants()) {
			valores.add(new EnumValue(clase, (Enum<?>) o));
		}
	}
	public class EnumValue implements Serializable{
		private static final long serialVersionUID = 7767854074029064604L;
		public final Map<String, Object> propiedades = new TreeMap<>();
		public final String name;
		public EnumValue(Class<?> clase, Enum<?> e) {
			name = e.name();
			try {
				for(Field f  : clase.getFields())
					if((Modifier.isFinal(f.getModifiers()) || f.getName().equals("id")) && !Modifier.isStatic(f.getModifiers()) )
						propiedades.put(f.getName(), f.get(e));
				propiedades.put("rawName", e.name());					
			} catch (Exception ex) {throw new NullPointerException(ex.getMessage());}
		}
	}
}
