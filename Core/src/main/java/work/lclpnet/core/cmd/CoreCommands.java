package work.lclpnet.core.cmd;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class CoreCommands {

	public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher.register(CommandSilentChat.command());
	}

	public static boolean permLevel1(CommandSource cs) {
		return cs.hasPermissionLevel(1);
	}
	
	public static boolean permLevel2(CommandSource cs) {
		return cs.hasPermissionLevel(2);
	}
	
	public static boolean permLevel3(CommandSource cs) {
		return cs.hasPermissionLevel(3);
	}
	
	public static boolean permLevel4(CommandSource cs) {
		return cs.hasPermissionLevel(4);
	}
	
}
