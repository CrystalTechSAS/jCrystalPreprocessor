package jcrystal.preprocess.main;

import jcrystal.preprocess.descriptions.JClass;
import jcrystal.preprocess.descriptions.JPackage;
import jcrystal.preprocess.utils.Resolver;

public class ClassProcesor {

	public static JClass loadClassInfo(Class<?> clase) {
		try {
			JClass ret = new JClass(clase);
			Resolver.CLASES.put(ret.name, ret);
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
}
 