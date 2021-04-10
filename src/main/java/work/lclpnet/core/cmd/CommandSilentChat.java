package work.lclpnet.core.cmd;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Config;
import work.lclpnet.core.Core;
import work.lclpnet.corebase.cmd.CommandBase;
import work.lclpnet.corebase.cmd.CoreCommands;
import work.lclpnet.corebase.util.Substitute;

public class CommandSilentChat extends CommandBase {

    public CommandSilentChat() {
        super("silentchat");
        addAlias("sc");
    }

    @Override
    protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
        return builder
                .requires(CoreCommands::permLevel2)
                .executes(CommandSilentChat::status)
                .then(Commands.argument("state", BoolArgumentType.bool())
                        .executes(CommandSilentChat::changeState));
    }

    private static int status(CommandContext<CommandSource> c) {
        Substitute subs = new Substitute(Config.isChatSilenced() ? "on" : "off", Config.isChatSilenced() ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED);
        c.getSource().sendFeedback(Core.TEXT.complexMessage("Silent chat is currently %s.", TextFormatting.GRAY, subs), true);
        return 0;
    }

    private static int changeState(CommandContext<CommandSource> c) {
        boolean state = c.getArgument("state", Boolean.class);
        Config.setChatSilenced(state);

        Substitute subs = new Substitute(state ? "now" : "no longer", state ? TextFormatting.DARK_GREEN : TextFormatting.DARK_RED);
        c.getSource().sendFeedback(Core.TEXT.complexMessage("The chat is %s silenced.", TextFormatting.GRAY, subs), true);
        return 0;
    }

}
