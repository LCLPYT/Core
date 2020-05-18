package work.lclpnet.core.cmd;

import java.util.List;

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

public class CommandHeal extends CommandBase{

	public CommandHeal() {
		super("heal");
	}
	
	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::isPlayerPermLevel2)
				.executes(this::healSelf)
				.then(Commands.argument("target", EntityArgument.players())
						.executes(this::healOther));
	}
	
	public int healSelf(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		if(!CoreCommands.isPlayer(ctx.getSource())) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("Non-players have to supply a target. e.g. /heal <name>", MessageType.ERROR));
			return 1;
		}
		
		heal(ctx.getSource().asPlayer());
		
		return 0;
	}
	
	public int healOther(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		EntitySelector selector = ctx.getArgument("target", EntitySelector.class);
		List<ServerPlayerEntity> targets = selector.selectPlayers(ctx.getSource());
		
		if(targets == null || targets.isEmpty()) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("No players were found.", MessageType.ERROR));
			return 1;
		}
		
		targets.forEach(this::heal);
		
		ctx.getSource().sendFeedback(Core.TEXT.complexMessage("Healed %s.", TextFormatting.GREEN, new Substitute(EntitySelector.joinNames(targets).getString(), TextFormatting.YELLOW)), false);
		
		return 0;
	}
	
	private void heal(ServerPlayerEntity p) {
		p.setHealth(p.getMaxHealth());
		p.getFoodStats().setFoodLevel(20);
		p.sendMessage(Core.TEXT.message("You have been healed.", MessageType.SUCCESS));
	}
	
}
