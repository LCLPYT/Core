package work.lclpnet.core.cmd;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.PlayerHelper;
import work.lclpnet.core.util.Substitute;

public class CommandSpeed extends CommandBase{

	public CommandSpeed() {
		super("speed");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::isPlayerPermLevel2)
				.then(Commands.literal("set")
						.then(Commands.argument("value", FloatArgumentType.floatArg(-1F, 5F))
								.executes(this::set)))
				.then(Commands.literal("reset")
						.executes(this::reset));
	}
	
	public int set(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		float f = ctx.getArgument("value", float.class);
		ServerPlayerEntity p = ctx.getSource().asPlayer();
		if(p.abilities.isFlying) {
			if(f > 1F) f = 1F;
			PlayerHelper.setFlySpeed(p, f);
			sendUpdate(f, "flying", p);
		} else {
			PlayerHelper.setWalkSpeed(p, f);
			sendUpdate(f, "walking", p);
		}
		return 0;
	}

	private void sendUpdate(float f, String type, ServerPlayerEntity p) {
		p.sendMessage(Core.TEXT.complexMessage("Set your %s speed to %s.", TextFormatting.GREEN, new Substitute(type), new Substitute(f, TextFormatting.YELLOW)));
	}
	
	public int reset(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		final float flyDef = 0.05F, walkDef = 0.1F;
		
		ServerPlayerEntity p = ctx.getSource().asPlayer();
		if(p.abilities.isFlying) {
			PlayerHelper.setFlySpeed(p, flyDef);
			sendUpdate(flyDef, "flying", p);
		} else {
			PlayerHelper.setWalkSpeed(p, walkDef);
			sendUpdate(walkDef, "walking", p);
		}
		return 0;
	}
	
}
