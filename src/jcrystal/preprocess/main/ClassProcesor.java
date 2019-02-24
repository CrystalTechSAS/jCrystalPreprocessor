package jcrystal.preprocess.main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jcrystal.preprocess.descriptions.JClass;
import jcrystal.preprocess.descriptions.JPackage;
import jcrystal.preprocess.descriptions.JType;
import jcrystal.preprocess.descriptions.JTypeSolver;
import jcrystal.preprocess.utils.Resolver;
import jcrystal.reflection.annotations.Post;

public class ClassProcesor {

	public static JClass loadClassInfo(Class<?> clase) {
		try {
			JClass ret = new JClass(clase);
			Resolver.CLASES.put(ret.name, ret);
			if(clase.getSimpleName().startsWith("Manager"))
				Arrays.stream(clase.getDeclaredClasses()).forEach(c->{
					ClassProcesor.loadClassInfo(c);
				});
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static void loadPackageInfo(Package paquete) {
		JPackage ret = new JPackage(paquete);
		Resolver.PACKAGES.put(ret.getName(), ret);
	}
	
	
	public static void loadExtras() {
		Set<String> procesados = new TreeSet<>();
		while(true){
			List<JType> tiposPost = JTypeSolver.SIMPLE_TYPES.values().stream().filter(f->
				!procesados.contains(f.getName()) && f.isAnnotationPresent(Post.class) 
			).collect(Collectors.toList());
			if(tiposPost.isEmpty())
				break;
			for(JType t : tiposPost) {
				try {
					System.out.println("    Load extra "  + t.getName());
					procesados.add(t.getName());
					ClassProcesor.loadClassInfo(Class.forName(t.getName()));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			System.out.println();
		}
	}
}
 