package work.lclpnet.core.util;

public class CharacterHelper {

	public static String getJavaUnicodeCode(char c) {
		return "\\u" + Integer.toHexString(c | 0x10000).substring(1);
	}
	
	public static String getUnicodeCode(char c) {
		return "U+" + Integer.toHexString(c | 0x10000).substring(1);
	}
	
}
