package work.lclpnet.core.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.ComponentSupplier.Substitute;
import work.lclpnet.core.util.MessageType;

public class CommandPing extends CommandBase{

	public CommandPing() {
		super("ping");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::isPlayer)
				.executes(this::pingSelf)
				.then(Commands.argument("target", EntityArgument.player())
						.executes(this::pingOther));
	}
	
	public int pingSelf(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		if(!CoreCommands.isPlayer(ctx.getSource())) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("Non-players have to specify a target! e.g. /ping <name>", MessageType.ERROR));
			return 1;
		}
		ctx.getSource().sendFeedback(Core.TEXT.complexMessage("Your ping: %s", TextFormatting.GREEN, new Substitute(ctx.getSource().asPlayer().ping + "ms", TextFormatting.YELLOW)), false);
		return 0;
	}
	
	public int pingOther(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		EntitySelector selector = ctx.getArgument("target", EntitySelector.class);
		ServerPlayerEntity target = selector.selectOnePlayer(ctx.getSource());
		if(target == null) return 1;
		
		ctx.getSource().sendFeedback(Core.TEXT.complexMessage("%s's Ping: %s", TextFormatting.GREEN, new Substitute(target.getName().getString()), new Substitute(target.ping + "ms", TextFormatting.YELLOW)), false);
		
		return 0;
	}
	
}
