package work.lclpnet.core.util;

import java.math.BigDecimal;

import javax.annotation.Nullable;

public class NumberHelper {

	public static double square(double d) {
		return d * d;
	}

	public static float square(float f) {
		return f * f;
	}

	public static double root(double radicand, double index) {
		if(radicand < 0D) return Double.NaN;
		else if(radicand == 0D) return 0D;
		else if(radicand == 1D) return 1D;
		
		if(index == 0D) return 1D;
		else if(index == 1D) return radicand;
		
		return Math.pow(Math.E, Math.log(radicand) / index);
	}

	public static double sqrtNewton(double radicand) {
		double t, sqrt = radicand / 2;
		
		do {
			t = sqrt;
			sqrt = (t + (radicand / t)) / 2;
		} while ((t - sqrt) != 0);
		
		return sqrt;
	}
	
	public static double rootNewton(double radicand, double index) {
		if(radicand < 0D) return Double.NaN;
		else if(radicand == 0D) return 0D;
		else if(radicand == 1D) return 1D;
		
		if(index == 0D) return 1D;
		else if(index == 1D) return radicand;
		else if(index <= 0D) return 1D / rootNewton(radicand, Math.abs(index));
		
		double rt = radicand / index, t, epsilon = 10e-11D;
		
		do {
			t = rt;
			rt = rt - ((Math.pow(rt, index) - radicand) / (index * Math.pow(rt, index - 1D)));
		} while (Math.abs(t - rt) > epsilon);
		
		return rt;
	}
	
	public static boolean isMultipleOf(int multiple, int of) {
		return multiple % of == 0;
	}

	public static boolean isMultipleOf(double multiple, double of) {
		return multiple % of == 0d;
	}

	public static boolean isMultipleOf(float multiple, float of) {
		return multiple % of == 0f;
	}

	public static double round(double d, int decimalPlaces) {
		BigDecimal bd = new BigDecimal(d); 
		bd = bd.setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP);
		return bd.doubleValue();
	}

	public static int roundUp(double d) {
		return (int) Math.ceil(d);
	}

	public static int roundUp(float f) {
		return (int) Math.ceil(f);
	}

	public static int roundDown(double d) {
		return (int) Math.floor(d);
	}

	public static int roundDown(float f) {
		return (int) Math.floor(f);
	}

	public static int getDecimalPlaces(double d) {
		String text = Double.toString(Math.abs(d));
		int integerPlaces = text.indexOf('.');
		return text.length() - integerPlaces - 1;
	}

	public static int getDecimalPlaces(float f) {
		String text = Float.toString(Math.abs(f));
		int integerPlaces = text.indexOf('.');
		return text.length() - integerPlaces - 1;
	}
	
    public static boolean isKthBitSet(int val, int k) {
        return (val & (1 << k - 1)) >> k - 1 == 1;
    }
    
    public static String getBinaryString(int val) {
    	return Integer.toBinaryString(val);
    }
    
    public static String getBinaryStringFormattedAsFirstByte(int val) {
    	return getFormattedBinaryString(val, BinaryFormat.MANUAL_OCTET_CROP_OR_PAD, 1);
    }
    
    public static String getBinaryStringFormattedAsBytes(int val) {
    	return getBinaryStringFormattedAsBytes(val, false);
    }
    
    public static String getBinaryStringFormattedAsBytes(int val, boolean spacesBetweenBytes) {
    	return getFormattedBinaryString(val, BinaryFormat.AUTO_OCTET, 0, spacesBetweenBytes);
    }
    
    public static String getFormattedBinaryString(int val, @Nullable BinaryFormat format) {
    	return getFormattedBinaryString(val, format, 1, false);
    }
    
    public static String getFormattedBinaryString(int val, @Nullable BinaryFormat format, int bytes) {
    	return getFormattedBinaryString(val, format, bytes, false);
    }
    
    public static String getFormattedBinaryString(int val, @Nullable BinaryFormat format, int bytes, boolean spacesBetweenBytes) {
    	String binaryString = getBinaryString(val);
    	
		if(format == null || format == BinaryFormat.COMPACT) return binaryString;
		else if(format == BinaryFormat.MANUAL_OCTET_CROP_OR_PAD || format == BinaryFormat.AUTO_OCTET) {
			if(format == BinaryFormat.AUTO_OCTET) bytes = (int) Math.ceil(binaryString.length() / 8D);
			
			int goalLength = bytes * 8;
			int length = binaryString.length();
			
			if(length < goalLength) {
				StringBuilder builder = new StringBuilder();
		        for (int i = 0; i < goalLength - length; i++) builder.append('0');
		        builder.append(binaryString);
		        
		        binaryString = builder.toString();
			}
			
			binaryString = binaryString.substring(binaryString.length() - goalLength, binaryString.length());
			if(spacesBetweenBytes && bytes > 1) {
				StringBuilder builder = new StringBuilder();
				for (int i = 0; i < bytes; i++) builder.append(binaryString.substring(i * 8, (i + 1) * 8) + (i == bytes - 1 ? "" : " "));
				binaryString = builder.toString();
			}
			return binaryString;
		}
		
		return null; //format not supported
    }
    
	public static float randomFloat(float min, float max) {
		return min + Randoms.mainRandom.nextFloat() * (max - min);
	}
	
	public static double randomDouble(double min, double max) {
		return min + Randoms.mainRandom.nextDouble() * (max - min);
	}
	
	public static int randomInt(int i1, int i2) {
		int min = Math.min(i1, i2);
		int max = Math.max(i1, i2);
		return min + Randoms.mainRandom.nextInt(max - min + 1);
	}
    
    public static enum BinaryFormat {
    	COMPACT,
    	AUTO_OCTET,
    	MANUAL_OCTET_CROP_OR_PAD;
    }

}
