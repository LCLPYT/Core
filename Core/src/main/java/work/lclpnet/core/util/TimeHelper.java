package work.lclpnet.core.util;

public class TimeHelper {

	public static String convertUntilYearsIncorrect(long millis) {
		long years = getRawYearsIncorrect(millis);
		
		if(years > 0) {
			return String.format("%sy %sm %sd %sh %smin %ssec", years,
					getRawMonthsIncorrect(millis) % 12,
					getRawDays(millis) % 31,
					getRawHours(millis) % 24,
					getRawMinutes(millis) % 60,
					getRawSeconds(millis) % 60);
		} else return convertUntilMonthsIncorrect(millis);
	}

	public static String convertUntilMonthsIncorrect(long millis) {
		long months = getRawMonthsIncorrect(millis);
		
		if(months > 0) {
			return String.format("%sm %sd %sh %smin %ssec", months,
					getRawDays(millis) % 31,
					getRawHours(millis) % 24,
					getRawMinutes(millis) % 60,
					getRawSeconds(millis) % 60);
		} else return convertUntilWeeks(millis);
	}

	public static String convertUntilWeeks(long millis) {
		long weeks = getRawWeeks(millis);
		
		if(weeks > 0) {
			return String.format("%sw %sd %sh %smin %ssec", weeks,
					getRawDays(millis) % 7,
					getRawHours(millis) % 24,
					getRawMinutes(millis) % 60,
					getRawSeconds(millis) % 60);
		} else return convertUntilDays(millis);
	}

	public static String convertUntilDays(long millis) {
		long days = getRawDays(millis);
		
		if(days > 0) {
			return String.format("%sd %sh %smin %ssec", days,
					getRawHours(millis) % 24,
					getRawMinutes(millis) % 60,
					getRawSeconds(millis) % 60);
		} else return convertUntilHours(millis);
	}

	public static String convertUntilHours(long millis) {
		long hours = getRawHours(millis);
		
		if(hours > 0) {
			return String.format("%sh %smin %ssec", hours,
					getRawMinutes(millis) % 60,
					getRawSeconds(millis) % 60);
		} else return convertUntilMinutes(millis);
	}

	public static String convertUntilMinutes(long millis) {
		long minutes = getRawMinutes(millis);
		
		if(minutes > 0) {
			return String.format("%smin %ssec", minutes,
					getRawSeconds(millis) % 60);
		} else return convertUntilSeconds(millis);
	}
	
	public static String convertUntilMinutesWithMillis(long millis) {
		return convertUntilMinutesWithMillis(millis, 3);
	}
	
	public static String convertUntilMinutesWithMillis(long millis, int digits) {
		long minutes = getRawMinutes(millis);
		
		if(minutes > 0) {
			return String.format("%smin %ssec", minutes,
					String.format(String.format("%%.%sf", digits), getRawSeconds(millis) % 60 + (millis % 1000 / 1000D)));
		} else return convertUntilSecondsWithMillis(millis);
	}

	public static String convertUntilSeconds(long millis) {
		long seconds = getRawSeconds(millis);
		
		if(seconds > 0) return String.format("%ssec", seconds);
		else return convertUntilSecondsWithMillis(millis);
	}
	
	public static String convertUntilSecondsWithMillis(long millis) {
		return convertUntilSecondsWithMillis(millis, 3);
	}
	
	public static String convertUntilSecondsWithMillis(long millis, int digits) {
		if(millis <= 0) return "0ms";
		
		long seconds = getRawSeconds(millis);
		if(seconds > 0) return String.format("%ssec", String.format(String.format("%%.%sf", digits), seconds + (millis % 1000 / 1000d)));
		else return String.format("%sms", millis);
	}
	
	public static long getRawSeconds(long millis) {
		return millis / 1000;
	}

	public static long getRawMinutes(long millis) {
		return millis / 60000;
	}

	public static long getRawHours(long millis) {
		return millis / 3600000;
	}

	public static long getRawDays(long millis) {
		return millis / 86400000;
	}

	public static long getRawWeeks(long millis) {
		return millis / 604800000;
	}

	/**
	 * This method will assume that one month = 31 days,
	 * therefore it is named 'incorrect'.<br>
	 * <br>
	 * <b>THIS IS HARDCODED</b>
	 */
	public static long getRawMonthsIncorrect(long millis) {
		return millis / 2678400000L;
	}

	/**
	 * This method will assume that one month = 31 days,
	 * therefore it is named 'incorrect'.<br>
	 * <br>
	 * <b>THIS IS HARDCODED</b>
	 */
	public static long getRawYearsIncorrect(long millis) {
		return millis / 32140800000L;
	}
	
}
