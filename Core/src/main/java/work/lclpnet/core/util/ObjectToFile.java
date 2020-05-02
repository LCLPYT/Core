package work.lclpnet.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class ObjectToFile {

	public static void write(Serializable obj, File file) {
		if(!file.exists()) 
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		FileOutputStream fos = null;
		ObjectOutputStream oos = null;

		try {
			fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(fos != null) fos.close();
				if(oos != null) oos.close();
			} catch (IOException e) {
				System.out.println("Error closing OutputStreams");
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> T read(File file) {
		T t = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(file);
			ois = new ObjectInputStream(fis);
			t = (T) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fis != null) fis.close();
				if(ois != null) ois.close();
			} catch (IOException e) {
				System.out.println("Error closing InputStreams");
				e.printStackTrace();
			}
		}
		return t;
	}

	public static <T> void writeJson(T t, File f) {
		if(t.getClass().isAnonymousClass()) throw new UnsupportedOperationException("Anonymous classes can't be serialized.");

		if(!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				System.out.println("Error creating json file (" + f.getAbsolutePath() + ").");
				e.printStackTrace();
			}
		}
		
		Gson gson = new Gson();

		PrintStream writer = null;
		try {
			writer = new PrintStream(f);
			gson.toJson(t, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T> T readJson(File f, Class<T> type) {
		if(f == null || !f.exists() || type == null) return null;

		T t = null;

		try {
			JsonReader reader = new JsonReader(new FileReader(f));
			t = new Gson().fromJson(reader, type);

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return t;
	}

}
