package work.lclpnet.core.cmd;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class CoreCommands {

	public static void registerCommands(CommandDispatcher<CommandSource> dispatcher) {
		new CommandSilentChat().register(dispatcher);
		new CommandChatClear().register(dispatcher);
		new CommandChest().register(dispatcher);
		new CommandInventory().register(dispatcher);
		new CommandCraft().register(dispatcher);
		new CommandCrash().register(dispatcher);
		new CommandDay().register(dispatcher);
		new CommandNight().register(dispatcher);
		new CommandDie().register(dispatcher);
		new CommandFeed().register(dispatcher);
		new CommandHeal().register(dispatcher);
		new CommandPing().register(dispatcher);
		new CommandPlayerInfo().register(dispatcher);
		new CommandRawSay().register(dispatcher);
		new CommandRename().register(dispatcher);
		new CommandSpeed().register(dispatcher);
		new CommandSudo().register(dispatcher);
		new CommandWorldSpawnTp().register(dispatcher);
		new CommandFly().register(dispatcher);
	}
	
}
