package work.lclpnet.core.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.text.TextFormatting;

public class ColorHelper {

	public static List<TextFormatting> getChatColorColors() {
		List<TextFormatting> colors = new ArrayList<>();
		colors.add(TextFormatting.AQUA);
		colors.add(TextFormatting.BLACK);
		colors.add(TextFormatting.BLUE);
		colors.add(TextFormatting.DARK_AQUA);
		colors.add(TextFormatting.DARK_BLUE);
		colors.add(TextFormatting.DARK_GRAY);
		colors.add(TextFormatting.DARK_GREEN);
		colors.add(TextFormatting.DARK_PURPLE);
		colors.add(TextFormatting.DARK_RED);
		colors.add(TextFormatting.GOLD);
		colors.add(TextFormatting.GRAY);
		colors.add(TextFormatting.GREEN);
		colors.add(TextFormatting.LIGHT_PURPLE);
		colors.add(TextFormatting.RED);
		colors.add(TextFormatting.WHITE);
		colors.add(TextFormatting.YELLOW);
		return colors;
	}

	public static List<TextFormatting> getChatColorFormats() {
		List<TextFormatting> colors = new ArrayList<>();
		colors.add(TextFormatting.BOLD);
		colors.add(TextFormatting.ITALIC);
		colors.add(TextFormatting.OBFUSCATED);
		colors.add(TextFormatting.RESET);
		colors.add(TextFormatting.STRIKETHROUGH);
		colors.add(TextFormatting.UNDERLINE);
		return colors;
	}

	public static List<TextFormatting> getChatColorLightColors() {
		List<TextFormatting> colors = new ArrayList<>();
		colors.add(TextFormatting.AQUA);
		colors.add(TextFormatting.BLUE);
		colors.add(TextFormatting.GOLD);
		colors.add(TextFormatting.GRAY);
		colors.add(TextFormatting.GREEN);
		colors.add(TextFormatting.LIGHT_PURPLE);
		colors.add(TextFormatting.RED);
		colors.add(TextFormatting.WHITE);
		colors.add(TextFormatting.YELLOW);
		return colors;
	}

	public static List<TextFormatting> getChatColorRainbowColors() {
		List<TextFormatting> colors = new ArrayList<>();
		colors.add(TextFormatting.RED);
		colors.add(TextFormatting.GOLD);
		colors.add(TextFormatting.YELLOW);
		colors.add(TextFormatting.GREEN);
		colors.add(TextFormatting.AQUA);
		colors.add(TextFormatting.DARK_BLUE);
		colors.add(TextFormatting.DARK_PURPLE);
		return colors;
	}

}
