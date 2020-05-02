package work.lclpnet.core.util;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.apache.commons.io.FileUtils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;

public class FileHelper {

	public static enum ByteUnit {
		BYTE(1D),
		KILOBYTE(1e+3D),
		MEGABYTE(1e+6D),
		GIGABYTE(1e+9D),
		TERRABYTE(1e+12D),
		PETABYTE(1e+15D),
		EXABYTE(1e+18D),
		ZETTABYTE(1e+21D),
		YOTTABYTE(1e+24D);
		
		private double multiplier;
		
		private ByteUnit(double potency) {
			this.multiplier = potency;
		}
		
		public double getMultiplier() {
			return multiplier;
		}
		
		public long getBytes(long amount) {
			return (long) (amount * multiplier);
		}
	}
	
	public static long getBytes(ByteUnit u, long amount) {
		if(u == null) return 0L;
		return u.getBytes(amount);
	}

	public static boolean forceDelete(File f) {
		if(f == null || !f.exists()) return false;
		
		try {
			FileUtils.forceDelete(f);
		} catch (IOException e) {
			System.out.println("Error deleting file.");
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

	public static long getFileBytesAmount(File f) {
		if(f == null || !f.exists()) return 0L;
		return f.length();
	}

	public static double getFileSize(long bytes, ByteUnit u) {
		return (double) (bytes / u.getMultiplier());
	}

	public static double getFileSize(File f, ByteUnit u) {
		return getFileSize(getFileBytesAmount(f), u);
	}

	public static String getFileSizeString(long bytes, ByteUnit u, int decimalPlaces) {
		double unitSize = getFileSize(bytes, u);
		String value = "" + unitSize;

		if(unitSize % 1D == 0D) value = "" + NumberHelper.roundDown(unitSize);
		else {
			double pow = Math.pow(0.1D, decimalPlaces);
			if(unitSize < pow && unitSize > 0D) {
				String text = Double.toString(unitSize);
				int farIdx = -1;
				for (int i = text.indexOf('.') + 1; i < text.length(); i++) {
					char c = text.charAt(i);
					if(c != '0') {
						farIdx = i;
						break;
					}
				}
				if(farIdx != -1) {
					String temp = text.substring(0, farIdx + 1);
					double fixed = unitSize;
					try {
						Double.parseDouble(temp);
					} catch (NumberFormatException e) {
						//the 'temp' string is malformed OR the double contains an E notation (e.g. 1.0E) OR an unknown cause
					}
					DecimalFormat format = new DecimalFormat();
					value = format.format(fixed);
				}
			}
			else {
				DecimalFormat format = new DecimalFormat();
				format.setMaximumFractionDigits(decimalPlaces);
				value = format.format(unitSize);
			}
		}

		switch (u) {
		case BYTE:      return value + " B";
		case KILOBYTE:  return value + " KB";
		case MEGABYTE:  return value + " MB";
		case GIGABYTE:  return value + " GB";
		case TERRABYTE: return value + " TB";
		case PETABYTE:  return value + " PB";
		case EXABYTE:   return value + " EB";
		case ZETTABYTE: return value + " ZB";
		case YOTTABYTE: return value + " YB";
		default:
			return value;
		}
	}

	public static String getFileSizeString(File f, ByteUnit u, int decimalPlaces) {
		return getFileSizeString(getFileBytesAmount(f), u, decimalPlaces);
	}

	public static HashCode getHashCode(File f, HashFunction function) {
		if(f == null || !f.exists() || function == null) return null;
		
		try {
			ByteSource byteSource = Files.asByteSource(f);
			return byteSource.hash(function);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getHash(File f, HashFunction function) {
		HashCode code = getHashCode(f, function);
		if(code == null) return null;
		
		return code.toString();
	}
	
	public static String getSha256(File f) {
		return getHash(f, Hashing.sha256());
	}
	
}
