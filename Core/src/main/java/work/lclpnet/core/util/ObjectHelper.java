package work.lclpnet.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ObjectHelper {

	/**
	 * Returns the value of a field from the specified object.
	 * The only difference to {@link #getThrowing(Object, String)} is that this method returns null instead of throwing an exception.
	 * 
	 * @param <T> The type of the object to return.
	 * @param obj The object whose field should be returned.
	 * @param fieldName The name of the field to modify.
	 * @return The value of the field (can be null). Also returns null if there was an error.
	 */
	@Nullable
	public static <T> T get(@Nonnull Object obj, @Nonnull String fieldName) {
		Objects.requireNonNull(obj);
		Objects.requireNonNull(fieldName);

		try {
			return getThrowing(obj, fieldName);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Returns the value of a field from the specified object.
	 * The only difference to {@link #get(Object, String)} is that this method throws an exception instead of just returning null.
	 * 
	 * @param obj The object whose field should be returned.
	 * @param fieldName The name of the field to modify.
	 * @return The value of the field (can be null).
	 * @throws ReflectiveOperationException If an error occurs.
	 */
	@Nullable
	@SuppressWarnings("unchecked")
	public static <T> T getThrowing(@Nonnull Object obj, @Nonnull String fieldName) throws ReflectiveOperationException{
		Objects.requireNonNull(obj);
		Objects.requireNonNull(fieldName);

		Field field = getField(obj, fieldName);
		if(field == null) throw new NoSuchFieldError(fieldName);
		
		boolean accessible = field.isAccessible();
		if(!accessible) field.setAccessible(true);
		
		T value = (T) field.get(obj);
		
		if(!accessible) field.setAccessible(false);
		
		return value;
	}

	/**
	 * Sets the value of a field from the specified object.
	 * The only difference to {@link #setThrowing(Object, String, Object)} is that this method returns true or false instead of throwing an exception.
	 * 
	 * @param obj The object whose field should be modified.
	 * @param fieldName The name of the field to modify.
	 * @param value The value to set the field to.
	 * @return true, if the value was set successfully. False on error.
	 */
	public static boolean set(@Nonnull Object obj, @Nonnull String fieldName, @Nullable Object value) {
		Objects.requireNonNull(obj);
		Objects.requireNonNull(fieldName);

		try {
			setThrowing(obj, fieldName, value);
			return true;
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Sets the value of a field from the specified object.
	 * The only difference to {@link #set(Object, String, Object)} is that this method throws an exception instead of returning true or false.
	 * 
	 * @param obj The object whose field should be modified.
	 * @param fieldName The name of the field to modify.
	 * @param value The value to set the field to.
	 * @throws ReflectiveOperationException If there was an error modifying the field.
	 */
	public static void setThrowing(@Nonnull Object obj, @Nonnull String fieldName, @Nullable Object value) throws ReflectiveOperationException{
		Objects.requireNonNull(obj);
		Objects.requireNonNull(fieldName);

		Field field = getField(obj, fieldName);
		if(field == null) throw new NoSuchFieldError(fieldName);
		
		boolean accessible = field.isAccessible();
		if(!accessible) field.setAccessible(true);
		
		field.set(obj, value);
		
		if(!accessible) field.setAccessible(false);
	}
	
	/**
	 * Gets a field with given name recursively.
	 * Normally a declared field from a super class is not found when trying to get it.
	 * This method recursively loops through the super classes of the objects and tries to find it.
	 * 
	 * @param obj The object whose field should be returned.
	 * @param fieldName The name of the field.
	 * @return The field of the object. If not found it will return null.
	 * @see Field
	 */
	@Nullable
	public static Field getField(@Nonnull Object obj, @Nonnull String fieldName) {
		Field field = null;
		try {
			field = obj.getClass().getDeclaredField(fieldName);			
		} catch (NoSuchFieldException e) {
			try {
				Class<?> superClass = obj.getClass();
				while(!(superClass = superClass.getSuperclass()).equals(Object.class)) 
					field = superClass.getDeclaredField(fieldName);
			} catch (NoSuchFieldException ex) {}
		}
		return field;
	}
	
	public static Method getCurrentMethod(Class<?>... parameterTypes) {
		final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
		if(ste.length < 3) return null;
		
		StackTraceElement current = ste[2];
		try {
			Class<?> cl = Class.forName(current.getClassName());
			return cl.getDeclaredMethod(current.getMethodName(), parameterTypes);
		} catch (ReflectiveOperationException e) {
			e.printStackTrace();
			return null;
		}
	}
	
}
