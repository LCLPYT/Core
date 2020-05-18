package work.lclpnet.core.cmd;

import java.util.List;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.MessageType;
import work.lclpnet.core.util.Substitute;

public class CommandChatClear extends CommandBase{

	public CommandChatClear() {
		super("chatclear");
		addAlias("cc");
	}
	
	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel2)
				.executes(CommandChatClear::clearAll)
				.then(Commands.argument("target", EntityArgument.players())
						.executes(CommandChatClear::clearSingle));
	}

	private static int clearAll(CommandContext<CommandSource> c) {
		final ITextComponent msg = Core.TEXT.message("Chat has been cleared.", MessageType.SUCCESS);
		Core.getServer().getPlayerList().getPlayers().forEach(p -> clearFor(p, msg));
		return 0;
	}

	private static int clearSingle(CommandContext<CommandSource> c) throws CommandSyntaxException {
		EntitySelector selector = c.getArgument("target", EntitySelector.class);
		List<ServerPlayerEntity> targets = selector.selectPlayers(c.getSource());

		if(targets == null || targets.isEmpty()) {
			c.getSource().sendErrorMessage(Core.TEXT.message("No players were found.", MessageType.ERROR));
			return 1;
		}

		c.getSource().sendFeedback(Core.TEXT.complexMessage("Cleared %s chat.", 
				TextFormatting.GREEN, 
				new Substitute(EntitySelector.joinNames(targets).appendText("'s").getString(), TextFormatting.YELLOW)), false);

		final ITextComponent msg = Core.TEXT.message("Your chat has been cleared.", MessageType.SUCCESS);
		targets.forEach(p -> clearFor(p, msg));
		return 0;
	}

	private static void clearFor(PlayerEntity p, ITextComponent msg) {
		for (int i = 0; i < 99; i++) 
			p.sendMessage(new StringTextComponent(""));
		p.sendMessage(msg);
	}

}
