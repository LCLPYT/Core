package work.lclpnet.core.cmd;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class CoreCommands {

	public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
		new CommandSilentChat().register(dispatcher);
	}
	
}
