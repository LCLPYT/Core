package work.lclpnet.core.cmd;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.ITextComponent;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.ComponentHelper;

public class CommandRawSay extends CommandBase {

	public CommandRawSay() {
		super("rawsay");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel2)
				.then(Commands.argument("message", StringArgumentType.greedyString())
						.executes(this::rawSay));
	}
	
	public int rawSay(CommandContext<CommandSource> ctx) {
		String msg = ctx.getArgument("message", String.class);
		ITextComponent itc = ComponentHelper.convertCharStyleToComponentStyle(msg, '&');
		if(itc == null) return 1;
		
		Core.getServer().getPlayerList().sendMessage(itc);
		return 0;
	}

}
