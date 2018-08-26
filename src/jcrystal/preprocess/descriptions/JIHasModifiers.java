package jcrystal.preprocess.descriptions;

import java.lang.reflect.Modifier;

public interface JIHasModifiers {
	public int getModifiers();
	public default boolean isPublic() {
		return Modifier.isPublic(getModifiers());
	}
	public default boolean isStatic() {
		return Modifier.isStatic(getModifiers());
	}
}
