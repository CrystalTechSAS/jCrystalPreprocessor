package jcrystal.preprocess.main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import jcrystal.preprocess.descriptions.JClass;
import jcrystal.preprocess.descriptions.JPackage;
import jcrystal.preprocess.descriptions.JTypeSolver;
import jcrystal.preprocess.utils.Resolver;
import jcrystal.reflection.annotations.Post;

public class ClassProcesor {

	public static JClass loadClassInfo(Class<?> clase) {
		if(clase.isAnnotationPresent(Post.class))
			EXTRA_CLASES.add(clase);
		
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
 