package work.lclpnet.core.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@SuppressWarnings({ "unchecked" })
public class Mojang {

	/**
	 * Retrieves the current UUID linked to a username.
	 *
	 * @param username the username
	 *
	 * @return the UUID as a {@link java.lang.String String}
	 */
	public static String getUUIDOfUsername(String username) {
		return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username).get("id");
	}

	/**
	 * Retrieves the UUID linked to a username at a certain moment in time.
	 *
	 * @param username  the username
	 * @param timestamp the Java Timestamp that represents the time
	 *
	 * @return the UUID as a {@link java.lang.String String}
	 */
	public static String getUUIDOfUsername(String username, String timestamp) {
		return (String) getJSONObject("https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + timestamp).get("id");
	}

	/**
	 * Retrieves all the username a certain UUID has had in the past, including the current one.
	 *
	 * @param uuid the UUID
	 *
	 * @return a map with the username as key value and the Timestamp as a {@link java.lang.Long Long}
	 */
	public static Map<String, Long> getNameHistoryOfPlayer(String uuid) {
		JSONArray         arr     = getJSONArray("https://api.mojang.com/user/profiles/" + uuid + "/names");
		Map<String, Long> history = new HashMap<>();
		arr.forEach(o -> {
			JSONObject obj = (JSONObject) o;
			history.put((String) obj.get("name"), obj.get("changedToAt") == null ? 0 : Long.parseLong(obj.get("changedToAt").toString()));
		});
		return history;
	}

	/**
	 * Returns the {@link org.shanerx.mojang.PlayerProfile PlayerProfile} object which holds and represents the metadata for a certain account.
	 *
	 * @param uuid the UUID of the player
	 *
	 * @return the {@link org.shanerx.mojang.PlayerProfile PlayerProfile} object}
	 */
	public static PlayerProfile getPlayerProfile(String uuid) {
		JSONObject obj = getJSONObject("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
		String name = (String) obj.get("name");
		Set<PlayerProfile.Property> properties = (Set<PlayerProfile.Property>) ((JSONArray) obj.get("properties")).stream().map(o -> {
			PlayerProfile.Property p;
			JSONObject prop = (JSONObject) o;

			String propName = (String) prop.get("name");
			String propValue = (String) prop.get("value");
			if (propName.equals("textures")) {
				JSONObject tex;
				try {
					tex = (JSONObject) new JSONParser().parse(new String(Base64.getDecoder().decode(propValue.getBytes())));
				} catch (ParseException e2) {
					/* Don't blame me, I just follow the pattern from #getJSONObject */
					throw new RuntimeException(e2);
				}
				PlayerProfile.TexturesProperty q = new PlayerProfile.TexturesProperty();
				q.timestamp = (Long) tex.get("timestamp");
				q.profileId = (String) tex.get("profileId");
				q.profileName = (String) tex.get("profileName");
				q.signatureRequired = Boolean.parseBoolean((String) tex.get("signatureRequired"));
				q.textures = ((Stream<Entry<Object, Object>>) ((JSONObject) tex.get("textures")).entrySet().stream()).collect(Collectors.toMap(
						e -> (String) e.getKey(),
						e -> {
							try {
								return new URL((String) ((JSONObject) e.getValue()).get("url"));
							} catch (MalformedURLException e1) {
								/* I want lambdas with exceptions in Java, *please* */
								throw new RuntimeException("Wrapper for checked exception for lambda", e1);
							}
						}));
				p = q;
			} else
				p = new PlayerProfile.Property();
			p.name = propName;
			p.signature = (String) prop.get("signature");
			p.value = propValue;
			return p;
		}).collect(Collectors.toSet());
		return new PlayerProfile(uuid, name, properties);
	}

	/**
	 * <p>Returns a list of blacklisted hostnames, belonging to servers that were blocked due to Mojang's EULA infringement.
	 * <p><strong>N.B.:</strong> These may not be in human friendly form as they were hashed. You may want to use third-party services to obtain an (unofficial) list.
	 *
	 * @return a {@link java.util.List List} of all the blocked hostnames
	 */
	public static List<String> getServerBlacklist() {
		try {
			return Arrays.asList(getResponse("https://sessionserver.mojang.com/blockedservers").split("\n"));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * This enum represents the possible Mojang API servers availability statuses.
	 */
	public static enum ServiceStatus {

		RED,
		YELLOW,
		GREEN,
		UNKNOWN
	}

	/**
	 * This enum represents the various portions of the Mojang API.
	 */
	public static enum ServiceType {

		MINECRAFT_NET,
		SESSION_MINECRAFT_NET,
		ACCOUNT_MOJANG_COM,
		AUTHSERVER_MOJANG_COM,
		SESSIONSERVER_MOJANG_COM,
		API_MOJANG_COM,
		TEXTURES_MINECRAFT_NET,
		MOJANG_COM;

		/**
		 * <p>This method overrides {@code java.lang.Object.toString()} and returns the address of the mojang api portion a certain enum constant represents.
		 * <p><strong>Example:</strong>
		 * {@code org.shanerx.mojang.Mojang.ServiceType.MINECRAFT_NET.toString()} will return {@literal minecraft.net}
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return name().toLowerCase().replace("_", ".");
		}
	}

	/**
	 * This enum represents the skin types "Alex" and "Steve".
	 */
	public static enum SkinType {
		/**
		 * Steve
		 */
		DEFAULT,
		/**
		 * Alex
		 */
		SLIM;

		/**
		 * Returns the query parameter version for these skin types in order to send HTTP requests to the API.
		 *
		 * @return the string
		 */
		@Override
		public String toString() {
			return this == DEFAULT ? "" : "slim";
		}
	}

	private static JSONObject getJSONObject(String url) {
		JSONObject obj;

		try {
			obj = (JSONObject) new JSONParser().parse(getResponse(url));
			String err = (String) (obj.get("error"));
			
			if (err != null) {
				switch (err) {
				case "IllegalArgumentException":
					throw new IllegalArgumentException((String) obj.get("errorMessage"));
				default:
					throw new RuntimeException(err);
				}
			}
		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}

		return obj;
	}

	private static JSONArray getJSONArray(String url) {
		JSONArray arr;

		try {
			arr = (JSONArray) new JSONParser().parse(getResponse(url));

		} catch (IOException | ParseException e) {
			throw new RuntimeException(e);
		}

		return arr;
	}

	private static String getResponse(String url) throws IOException {
		return IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
	}

}
