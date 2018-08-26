package jcrystal.preprocess.main;

import java.io.File;

public class MainPreprocessor {

	public static void loadFile(String name, File source) {
		if(name.endsWith("package-info")) {
			System.out.println("loadFile " + name);
			Package p = Package.getPackage(name.replace(".package-info", ""));
			ClassProcesor.loadPackageInfo(p);
		}else {
			try {
				ClassProcesor.loadClassInfo(Class.forName(name));
			}catch (Exception e) {
				e.printStackTrace();
			}			
		}
	}
}
