package work.lclpnet.core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.Holder;

import com.electronwill.nightconfig.core.file.FileConfig;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class Config {

	private static FileConfig config = null;
	private static Map<String, Object> register = new HashMap<>();

	public static final String KEY_DISABLE_FARMLAND_TRAMPLING = "game.disable-farmland-trampling",
			KEY_MESSAGE_JOIN = "messages.join",
			KEY_MESSAGE_QUIT = "messages.quit",
			KEY_NO_HUNGER = "game.no-hunger",
			KEY_SILENCED_CHAT = "game.silenced-chat",
			KEY_CMD_ECHO = "game.cmd-echo";

	static {
		ITextComponent joinDefault = new StringTextComponent("Join> ").mergeStyle(TextFormatting.DARK_GRAY)
				.appendSibling(new StringTextComponent("%s").mergeStyle(TextFormatting.GRAY));
		ITextComponent quitDefault = new StringTextComponent("Quit> ").mergeStyle(TextFormatting.DARK_GRAY)
				.appendSibling(new StringTextComponent("%s").mergeStyle(TextFormatting.GRAY));

		register.put(KEY_DISABLE_FARMLAND_TRAMPLING, false);
		register.put(KEY_MESSAGE_JOIN, ITextComponent.Serializer.toJson(joinDefault));
		register.put(KEY_MESSAGE_QUIT, ITextComponent.Serializer.toJson(quitDefault));
		register.put(KEY_NO_HUNGER, false);
		register.put(KEY_SILENCED_CHAT, false);
		register.put(KEY_CMD_ECHO, true);
	}

	public static void load() {
		File configDir = new File("config");
		File configFile = new File(configDir, "core.toml");

		if(!configFile.exists()) createConfigFile(configFile);
		config = FileConfig.builder(configFile).build();
		config.load();
		config.close();

		populateConfig();
	}

	private static void populateConfig() {
		Holder<Boolean> modified = new Holder<>(false);

		register.forEach((path, defaultValue) -> {
			if(!config.contains(path)) {
				config.set(path, defaultValue);
				if(!modified.value) modified.value = true;
			}
		});

		if(modified.value) save();
	}

	private static boolean createConfigFile(File config) {
		try {
			config.getParentFile().mkdirs();
			config.createNewFile();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public static FileConfig getConfig() {
		return config;
	}

	private static void set(String path, Object val) {
		config.set(path, val);
		save();
	}

	private static <T> T get(String path) {
		if(!config.contains(path)) {
			if(!register.containsKey(path)) throw new IllegalStateException("Path not registered.");
			set(path, register.get(path));
		}
		return config.get(path);
	}

	private static void save() {
		new Thread(() -> {
			FileConfig newConfig = FileConfig.builder(config.getFile()).build();
			newConfig.putAll(config);
			newConfig.save();
			newConfig.close();
		}, "Config Saver").run(); 
	}

	public static boolean isFarmlandTramplingDisabled() {
		return get(KEY_DISABLE_FARMLAND_TRAMPLING);
	}

	public static String getJoinMessage() {
		return get(KEY_MESSAGE_JOIN);
	}

	public static String getQuitMessage() {
		return get(KEY_MESSAGE_QUIT);
	}

	public static boolean isNoHunger() {
		return get(KEY_NO_HUNGER);
	}

	public static void setNoHunger(boolean state) {
		set(KEY_NO_HUNGER, state);
	}

	public static boolean isChatSilenced() {
		return get(KEY_SILENCED_CHAT);
	}

	public static void setChatSilenced(boolean state) {
		set(KEY_SILENCED_CHAT, state);
	}

	public static boolean isCMDEcho() {
		return get(KEY_CMD_ECHO);
	}

	public static void setCMDEcho(boolean state) {
		set(KEY_CMD_ECHO, state);
	}

	public static ITextComponent getJoinMessageComponent(String name) {
		String string = getJoinMessage();
		if(string == null) return null;

		return ITextComponent.Serializer.getComponentFromJson(String.format(string, name));
	}

	public static ITextComponent getQuitMessageComponent(String name) {
		String string = getQuitMessage();
		if(string == null) return null;

		return ITextComponent.Serializer.getComponentFromJson(String.format(string, name));
	}

}
