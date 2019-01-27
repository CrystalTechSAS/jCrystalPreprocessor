package jcrystal.preprocess.descriptions;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jcrystal.preprocess.main.ClassProcesor;
import jcrystal.preprocess.utils.Resolver;

public class JTypeSolver{
	
	private static Map<String, JType> SIMPLE_TYPES = new TreeMap<>();
	
	public static IJType STRING = JTypeSolver.load(String.class, null);
	public static IJType DATE = JTypeSolver.load(Date.class, null);
	
	public static IJType LONG = JTypeSolver.load(long.class, null);
	public static IJType INT = JTypeSolver.load(int.class, null);
	public static IJType DOUBLE = JTypeSolver.load(double.class, null);
	public static IJType BOOLEAN = JTypeSolver.load(boolean.class, null);
	public static IJType FLOAT= JTypeSolver.load(float.class, null);
	public static IJType CHAR = JTypeSolver.load(char.class, null);
	public static IJType BYTE = JTypeSolver.load(byte.class, null);
	public static IJType SHORT = JTypeSolver.load(short.class, null);
	
	public static IJType OBJ_LONG = JTypeSolver.load(Long.class, null);
	public static IJType OBJ_INT = JTypeSolver.load(Integer.class, null);
	public static IJType OBJ_DOUBLE = JTypeSolver.load(Double.class, null);
	public static IJType OBJ_FLOAT = JTypeSolver.load(Float.class, null);
	public static IJType OBJ_BOOLEAN = JTypeSolver.load(Boolean.class, null);
	public static IJType OBJ_CHAR = JTypeSolver.load(Character.class, null);
	public static IJType OBJ_BYTE = JTypeSolver.load(Byte.class, null);
	public static IJType OBJ_SHORT = JTypeSolver.load(Short.class, null);
	
	
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
}
