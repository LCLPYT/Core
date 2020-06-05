package work.lclpnet.core.cmd;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.AirItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.corebase.cmd.CommandBase;
import work.lclpnet.corebase.cmd.CoreCommands;
import work.lclpnet.corebase.util.ComponentHelper;
import work.lclpnet.corebase.util.MessageType;
import work.lclpnet.corebase.util.Substitute;

public class CommandRename extends CommandBase{

	public CommandRename() {
		super("rename");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel2)
				.then(Commands.argument("target", EntityArgument.players())
						.then(Commands.argument("displayName", StringArgumentType.greedyString())
								.executes(this::rename)));
	}

	public int rename(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		EntitySelector selector = ctx.getArgument("target", EntitySelector.class);
		List<ServerPlayerEntity> players = selector.selectPlayers(ctx.getSource());

		String display = ctx.getArgument("displayName", String.class);
		ITextComponent itc = ComponentHelper.convertCharStyleToComponentStyle("&r" + display, '&');

		List<ServerPlayerEntity> renamed = new ArrayList<>();
		players.forEach(p -> rename(p, itc, renamed));

		if(renamed.isEmpty()) {
			ctx.getSource().sendFeedback(Core.TEXT.message("No items were renamed, because none of the targets had an item in their main hand.", MessageType.ERROR), false);
		} else {
			ctx.getSource().sendFeedback(Core.TEXT.complexMessage("Renamed %s mainhand item to '%s'", TextFormatting.GREEN, 
					new Substitute(EntitySelector.joinNames(renamed).getString(), TextFormatting.YELLOW),
					new Substitute(itc, TextFormatting.WHITE)), false);
		}

		return 0;
	}

	private void rename(ServerPlayerEntity p, ITextComponent itc, List<ServerPlayerEntity> renamed) {
		ItemStack is = p.getHeldItemMainhand();
		if(is == null || is.getItem() instanceof AirItem) return;
		
		is.setDisplayName(itc);
		renamed.add(p);
	}

}
