package work.lclpnet.core.cmd;

import java.util.function.Predicate;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;

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
	
	public static boolean isPlayer(CommandSource cs) {
		return cs.getEntity() != null && cs.getEntity() instanceof PlayerEntity;
	}
	
	public static boolean isEntity(CommandSource cs) {
		return cs.getEntity() != null;
	}
	
	public static boolean isPlayerPermLevel2(CommandSource cs) {
		return permLevel2(cs) && isPlayer(cs);
	}
	
	@SafeVarargs
	public static boolean requires(CommandSource cs, Predicate<CommandSource>... predicates) {
		for(Predicate<CommandSource> p : predicates) 
			if(!p.test(cs)) return false;
		return true;
	}
	
}
