package work.lclpnet.core.util;

import java.util.Random;

public class Randoms {

	public static final Random mainRandom = new Random();
	
	public static Random getNewRandom() {
		return new Random();
	}
	
	/**
	 * Example: if <code>percent = 0.75F</code>, this method will return <code>true</code> with a 75% chance.
	 * 
	 * @param percent the chance, that <code>true</code> will be returned
	 * @return true, with the given chance
	 */
	public static boolean randomOperation(float percent) {
		return mainRandom.nextFloat() <= percent;
	}
	
	@SafeVarargs
	public static <T> T getRandomArrayElement(T... array) {
		if(array == null || array.length <= 0) return null;
		
		return array[mainRandom.nextInt(array.length)];
	}
	
}
