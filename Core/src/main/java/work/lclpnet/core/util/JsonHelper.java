package work.lclpnet.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;

public class JsonHelper {

	public static JsonObject getJsonObject(JsonObject obj, String key) {
		if(obj == null || key == null || !obj.has(key)) return null;
		
		JsonElement elem = obj.get(key);
		if(elem == null || !elem.isJsonObject()) return null;
		
		return elem.getAsJsonObject();
	}
	
	public static JsonObject getJsonObject(JsonObject obj, String key, JsonObject def) {
		JsonObject jObj = getJsonObject(obj, key);
		return jObj != null ? jObj : def;
	}
	
	public static JsonArray getJsonArray(JsonObject obj, String key) {
		if(obj == null || key == null || !obj.has(key)) return null;
		
		JsonElement elem = obj.get(key);
		if(elem == null || !elem.isJsonArray()) return null;
		
		return elem.getAsJsonArray();
	}
	
	public static JsonArray getJsonArray(JsonObject obj, String key, JsonArray def) {
		JsonArray v = getJsonArray(obj, key);
		return v != null ? v : def;
	}
	
	public static JsonNull getJsonNull(JsonObject obj, String key) {
		if(obj == null || key == null || !obj.has(key)) return null;
		
		JsonElement elem = obj.get(key);
		if(elem == null || !elem.isJsonNull()) return null;
		
		return elem.getAsJsonNull();
	}
	
	public static JsonNull getJsonNull(JsonObject obj, String key, JsonNull def) {
		JsonNull v = getJsonNull(obj, key);
		return v != null ? v : def;
	}
	
	public static JsonPrimitive getJsonPrimitive(JsonObject obj, String key) {
		if(obj == null || key == null || !obj.has(key)) return null;
		
		JsonElement elem = obj.get(key);
		if(elem == null || !elem.isJsonPrimitive()) return null;
		
		return elem.getAsJsonPrimitive();
	}
	
	public static JsonPrimitive getJsonPrimitive(JsonObject obj, String key, JsonPrimitive def) {
		JsonPrimitive v = getJsonPrimitive(obj, key);
		return v != null ? v : def;
	}
	
	public static String getString(JsonObject obj, String key) {
		JsonPrimitive primitive = getJsonPrimitive(obj, key);
		if(primitive == null || !primitive.isString()) return null;
		
		return primitive.getAsString();
	}
	
	public static String getString(JsonObject obj, String key, String def) {
		String v = getString(obj, key);
		return v != null ? v : def;
	}
	
	public static Boolean getBoolean(JsonObject obj, String key) {
		JsonPrimitive primitive = getJsonPrimitive(obj, key);
		if(primitive == null || !primitive.isBoolean()) return null;
		
		return primitive.getAsBoolean();
	}
	
	public static Boolean getBoolean(JsonObject obj, String key, Boolean def) {
		Boolean v = getBoolean(obj, key);
		return v != null ? v : def;
	}
	
	public static Number getNumber(JsonObject obj, String key) {
		JsonPrimitive primitive = getJsonPrimitive(obj, key);
		if(primitive == null || !primitive.isNumber()) return null;
		
		return primitive.getAsNumber();
	}
	
	public static Number getNumber(JsonObject obj, String key, Number def) {
		Number v = getNumber(obj, key);
		return v != null ? v : def;
	}
	
	public static Integer getInteger(JsonObject obj, String key) {
		Number n = getNumber(obj, key);
		return n != null ? new Integer(n.intValue()) : null;
	}
	
	public static Integer getInteger(JsonObject obj, String key, Integer def) {
		Integer v = getInteger(obj, key);
		return v != null ? v : def;
	}
	
	public static Double getDouble(JsonObject obj, String key) {
		Number n = getNumber(obj, key);
		return n != null ? new Double(n.doubleValue()) : null;
	}
	
	public static Double getDouble(JsonObject obj, String key, Double def) {
		Double v = getDouble(obj, key);
		return v != null ? v : def;
	}
	
	public static Float getFloat(JsonObject obj, String key) {
		Number n = getNumber(obj, key);
		return n != null ? new Float(n.floatValue()) : null;
	}
	
	public static Float getFloat(JsonObject obj, String key, Float def) {
		Float v = getFloat(obj, key);
		return v != null ? v : def;
	}
	
	public static Short getShort(JsonObject obj, String key) {
		Number n = getNumber(obj, key);
		return n != null ? new Short(n.shortValue()) : null;
	}
	
	public static Short getShort(JsonObject obj, String key, Short def) {
		Short v = getShort(obj, key);
		return v != null ? v : def;
	}
	
	public static Byte getByte(JsonObject obj, String key) {
		Number n = getNumber(obj, key);
		return n != null ? new Byte(n.byteValue()) : null;
	}
	
	public static Byte getByte(JsonObject obj, String key, Byte def) {
		Byte v = getByte(obj, key);
		return v != null ? v : def;
	}
	
	public static Long getLong(JsonObject obj, String key) {
		Number n = getNumber(obj, key);
		return n != null ? new Long(n.longValue()) : null;
	}
	
	public static Long getLong(JsonObject obj, String key, Long def) {
		Long v = getLong(obj, key);
		return v != null ? v : def;
	}
	
	public static JsonObject readJson(File f) {
		if(f == null || !f.exists()) return null;
		
		JsonObject json = null;

		try {
			FileInputStream fis = new FileInputStream(f);
			InputStreamReader isr = new InputStreamReader(fis);
			JsonReader jr = new JsonReader(isr);

			json = new Gson().fromJson(jr, JsonObject.class);

			jr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return json;
	}
	
	public static void writeJson(JsonObject json, File f) {
		writeJson(json, f, false);
	}
	
	public static void writeJson(JsonObject json, File f, boolean pretty) {
		if(json == null || f == null) return;
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		if(pretty) gsonBuilder.setPrettyPrinting();
		
		Gson gson = gsonBuilder.create();
		
		try (FileWriter fw = new FileWriter(f)) {

			gson.toJson(json, fw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
