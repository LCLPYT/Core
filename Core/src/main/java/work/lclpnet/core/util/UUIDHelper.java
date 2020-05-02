package work.lclpnet.core.util;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UUIDHelper {

    private static final Pattern DASHLESS_PATTERN = Pattern.compile("^([A-Fa-f0-9]{8})([A-Fa-f0-9]{4})([A-Fa-f0-9]{4})([A-Fa-f0-9]{4})([A-Fa-f0-9]{12})$");

    /**
     * Add dashes to a UUID.
     *
     * <p>If dashes already exist, the same UUID will be returned.</p>
     *
     * @param uuid the UUID
     * @return a UUID with dashes
     * @throws IllegalArgumentException thrown if the given input is not actually an UUID
     */
    public static String addDashes(String uuid) {
        uuid = uuid.replace("-", "");
        Matcher matcher = DASHLESS_PATTERN.matcher(uuid);
        if(!matcher.matches()) throw new IllegalArgumentException("Invalid UUID format");
        return matcher.replaceAll("$1-$2-$3-$4-$5");
    }
    
    public static String removeDashes(String uuid) {
    	return uuid.replaceAll("-", "");
    }
	
	public static String getUuid(String name) {
		return Mojang.getUUIDOfUsername(name);
	}

	public static String getName(String uuid) {
		Map<String, Long> history = Mojang.getNameHistoryOfPlayer(removeDashes(uuid));
		
		Entry<String, Long> latest = null;
		for(Entry<String, Long> e : history.entrySet()) {
			if(latest == null) latest = e;
			if(e.getValue() > latest.getValue()) latest = e;
		}
		
		return latest.getKey();
	}
	
}
