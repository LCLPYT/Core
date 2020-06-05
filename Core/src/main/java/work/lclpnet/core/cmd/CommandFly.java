package work.lclpnet.core.cmd;

import java.util.List;

import com.mojang.brigadier.arguments.BoolArgumentType;
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
import work.lclpnet.corebase.cmd.CommandBase;
import work.lclpnet.corebase.cmd.CoreCommands;
import work.lclpnet.corebase.util.MessageType;
import work.lclpnet.corebase.util.Substitute;

public class CommandFly extends CommandBase{

	public CommandFly() {
		super("fly");
	}
	
	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel2)
				.executes(this::flySelf)
				.then(Commands.argument("target", EntityArgument.players())
						.executes(this::flyOther)
						.then(Commands.argument("state", BoolArgumentType.bool())
								.executes(this::flyOtherState)));
	}
	
	public int flySelf(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		if(!CoreCommands.isPlayer(ctx.getSource())) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("Non-players must supply a target. e.g. /fly <player name>", MessageType.ERROR));
			return 1;
		}
		
		ctx.getSource().sendFeedback(
				toggleFly(ctx.getSource().asPlayer()) 
				? Core.TEXT.message("Flying enabled.", MessageType.SUCCESS) 
				: Core.TEXT.message("Flying disabled.", MessageType.ERROR), false);
		return 0;
	}
	
	public int flyOther(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		List<ServerPlayerEntity> targets = (List<ServerPlayerEntity>) EntityArgument.getPlayers(ctx, "target");
		if(targets.isEmpty()) return 1;
		
		Boolean state = null;
		boolean sameState = true;
		for(ServerPlayerEntity p : targets) {
			if(state == null) state = p.abilities.allowFlying;
			else if(state.booleanValue() != p.abilities.allowFlying) {
				sameState = false;
				break;
			}
		}
		
		if(!sameState) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("Error, the players you have selected do not have the same value for 'allowFlying'. If you want to apply a state to all players, consider passing an argument such as: /fly <players> [true|false].", MessageType.ERROR));
			return 1;
		}
		
		targets.forEach(this::toggleFly);
		ctx.getSource().sendFeedback(
				!state 
				? Core.TEXT.complexMessage("Flying enabled for %s.", TextFormatting.GREEN, new Substitute(EntitySelector.joinNames(targets).getString(), TextFormatting.YELLOW)) 
				: Core.TEXT.complexMessage("Flying disabled for %s.", TextFormatting.RED, new Substitute(EntitySelector.joinNames(targets).getString(), TextFormatting.YELLOW)), false);
		return 0;
	}
	
	public int flyOtherState(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		List<ServerPlayerEntity> targets = (List<ServerPlayerEntity>) EntityArgument.getPlayers(ctx, "target");
		if(targets.isEmpty()) return 1;
		
		boolean state = ctx.getArgument("state", boolean.class);
		
		targets.forEach(s -> setFly(s, state));
		ctx.getSource().sendFeedback(
				state 
				? Core.TEXT.complexMessage("Flying enabled for %s.", TextFormatting.GREEN, new Substitute(EntitySelector.joinNames(targets).getString(), TextFormatting.YELLOW)) 
				: Core.TEXT.complexMessage("Flying disabled for %s.", TextFormatting.RED, new Substitute(EntitySelector.joinNames(targets).getString(), TextFormatting.YELLOW)), false);
		return 0;
	}
	
	private boolean toggleFly(ServerPlayerEntity player) {
		setFly(player, !player.abilities.allowFlying);
		return player.abilities.allowFlying;
	}
	
	private void setFly(ServerPlayerEntity player, boolean allow) {
		player.abilities.allowFlying = allow;
		if(!allow && player.abilities.isFlying) player.abilities.isFlying = false;
		player.sendPlayerAbilities();
	}
	
}
