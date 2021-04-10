package work.lclpnet.core.cmd;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands.EnvironmentType;

public class CoreCommands {

    public static void registerCommands(CommandDispatcher<CommandSource> dispatcher, EnvironmentType type) {
        new CommandSilentChat().register(dispatcher);
    }

}
