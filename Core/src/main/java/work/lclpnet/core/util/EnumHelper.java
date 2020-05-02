package work.lclpnet.core.util;

public class EnumHelper {

	public static <T> T parseEnumType(String name, Class<T> clazz) {
		if(name == null || clazz == null || !clazz.isEnum()) return null;
		
		for(T t : clazz.getEnumConstants()) 
			if(t.toString().equals(name)) 
				return t;
		
		return null;
	}
	
	public static <T> T parseEnumTypeIgnoreCase(String name, Class<T> clazz) {
		if(name == null || clazz == null || !clazz.isEnum()) return null;
		
		for(T t : clazz.getEnumConstants()) 
			if(t.toString().equalsIgnoreCase(name)) 
				return t;
		
		return null;
	}
	
	public static <T> T getRandomEnumType(Class<T> clazz) {
		if(clazz == null || !clazz.isEnum()) return null;
		
		return Randoms.getRandomArrayElement(clazz.getEnumConstants());
	}
	
}
