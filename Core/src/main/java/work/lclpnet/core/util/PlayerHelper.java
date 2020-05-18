package work.lclpnet.core.util;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerList;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.PlayerProfile.TexturesProperty;

public class PlayerHelper {

	public static GameProfile retrieveGameProfile(String name, boolean forceDownloadNew) {
		PlayerList pl = Core.getServer().getPlayerList();
		PlayerEntity p = pl.getPlayerByUsername(name);
		if(!forceDownloadNew && p != null) return p.getGameProfile();
		else
			try {
				return GameProfileBuilder.fetch(UUID.fromString(UUIDHelper.addDashes(UUIDHelper.getUuid(name))), forceDownloadNew);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
	}
	
	public static JsonObject getProfile(PlayerEntity p) {
		GameProfile profile = p.getGameProfile();
		
		JsonObject json = new JsonObject();
		json.addProperty("id", UUIDHelper.removeDashes(profile.getId().toString()));
		json.addProperty("name", profile.getName());
		
		JsonArray properties = new JsonArray();
		for(Property prop : profile.getProperties().values()) {
			JsonObject property = new JsonObject();
			property.addProperty("name", prop.getName());
			property.addProperty("value", prop.getValue());
			if(prop.getSignature() != null && !prop.getSignature().isEmpty()) property.addProperty("signature", prop.getSignature());
			properties.add(property);
		}
		json.add("properties", properties);
		
		return json;
	}
	
	public static JsonObject retrieveProfile(String uuid) {
		String url = "https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDHelper.removeDashes(uuid);
		try {
			String jsonText = IOUtils.toString(new URL(url), StandardCharsets.UTF_8);
			System.out.println(jsonText);
			if(jsonText == null || jsonText.isEmpty()) return null;
			
			System.out.println(jsonText);
			
			JsonObject json = new Gson().fromJson(jsonText, JsonObject.class);
			return json;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JsonObject getProfileTextureProperty(PlayerEntity p) {
		GameProfile prof = p.getGameProfile();
		Collection<Property> collection = prof.getProperties().get("textures");

		Property textures = null;

		for(Property pr : collection) {
			if(pr.getName().equals("textures")) {
				textures = pr;
				break;
			}
		}

		if(textures == null) return null;
		
		String decoded = new String(Base64.getDecoder().decode(textures.getValue().getBytes()));
		return new Gson().fromJson(decoded, JsonObject.class);
	}
	
	public static JsonObject retrieveProfileTextureProperty(String uuid) {
		JsonObject profile = retrieveProfile(uuid);
		JsonArray properties = profile.getAsJsonArray("properties");

		JsonObject textures = null;

		for(JsonElement e : properties) {
			if(!e.isJsonObject()) continue;
			JsonObject obj = (JsonObject) e;
			if(obj.has("name") && obj.get("name").getAsString().equals("textures")) {
				textures = obj;
				break;
			}
		}

		if(textures == null) return null;
		
		String decoded = new String(Base64.getDecoder().decode(textures.get("value").getAsString().getBytes()));
		return new Gson().fromJson(decoded, JsonObject.class);
	}
	
	public static BufferedImage getSkin(PlayerEntity p) {
		JsonObject json = getProfileTextureProperty(p);
		if(!json.has("textures")) return null;
		
		JsonObject tex = json.getAsJsonObject("textures");
		if(!tex.has("SKIN")) return null;
		
		JsonObject skin = tex.getAsJsonObject("SKIN");
		if(!skin.has("url")) return null;
		
		String url = skin.get("url").getAsString();
		
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e2) {
			return null;
		}
	}
	
	public static BufferedImage getCapeSkin(PlayerEntity p) {
		JsonObject json = getProfileTextureProperty(p);
		if(!json.has("textures")) return null;
		
		JsonObject tex = json.getAsJsonObject("textures");
		if(!tex.has("CAPE")) return null;
		
		JsonObject skin = tex.getAsJsonObject("CAPE");
		if(!skin.has("url")) return null;
		
		String url = skin.get("url").getAsString();
		
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e2) {
			return null;
		}
	}
	
	public static BufferedImage downloadSkin(PlayerEntity p) {
		return downloadSkin(p.getUniqueID().toString());
	}
	
	public static BufferedImage downloadSkinByPlayerName(String name) {
		return downloadSkin(Mojang.getUUIDOfUsername(name));
	}
	
	public static BufferedImage downloadSkin(String uuid) {
		PlayerProfile profile = Mojang.getPlayerProfile(UUIDHelper.removeDashes(uuid));
		if(!profile.getTextures().isPresent()) return null;
		
		TexturesProperty textures = profile.getTextures().get();
		if(!textures.getSkin().isPresent()) return null;
		
		try {
			return ImageIO.read(textures.getSkin().get());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static BufferedImage downloadCapeSkin(PlayerEntity p) {
		return downloadCapeSkin(p.getUniqueID().toString());
	}
	
	public static BufferedImage downloadCapeSkinByPlayerName(String name) {
		return downloadCapeSkin(Mojang.getUUIDOfUsername(name));
	}
	
	public static BufferedImage downloadCapeSkin(String uuid) {
		PlayerProfile profile = Mojang.getPlayerProfile(UUIDHelper.removeDashes(uuid));
		if(!profile.getTextures().isPresent()) return null;
		
		TexturesProperty textures = profile.getTextures().get();
		if(!textures.getSkin().isPresent()) return null;
		
		try {
			return ImageIO.read(textures.getCape().get());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void setFlySpeed(ServerPlayerEntity p, float value) {
		setSpeed(p, value, "flySpeed");
	}
	
	public static void setWalkSpeed(ServerPlayerEntity p, float value) {
		setSpeed(p, value, "walkSpeed");
	}
	
	private static void setSpeed(ServerPlayerEntity p, float value, String field) {
		ObjectHelper.set(p.abilities, field, value);
		p.sendPlayerAbilities();
	}
	
}
