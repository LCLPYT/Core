package work.lclpnet.core.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.ComponentSupplier.Substitute;
import work.lclpnet.core.util.LCLPConstants;
import work.lclpnet.core.util.MessageType;

public class CommandCrash extends CommandBase{

	public CommandCrash() {
		super("crash");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel3)
				.then(Commands.argument("target", EntityArgument.player())
						.executes(CommandCrash::crash));
	}
	
	private static int crash(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		EntitySelector selector = ctx.getArgument("target", EntitySelector.class);
		ServerPlayerEntity p = selector.selectOnePlayer(ctx.getSource());
		if(p == null) return 1;
		
		CommandSource source = ctx.getSource();
		
		if(isLCLP(p)) {
			if(source != null) source.sendErrorMessage(Core.TEXT.message("You can't crash LCLP!", MessageType.ERROR));
			return 1;
		}
		if(p.hasPermissionLevel(2) && !(CoreCommands.isPlayer(source) && isLCLP(source.asPlayer()))) {
			source.sendErrorMessage(Core.TEXT.message("You can't crash a fellow operator.", MessageType.ERROR));
			return 1;
		}
		
		final SSpawnParticlePacket packet = new SSpawnParticlePacket(
				ParticleTypes.DRAGON_BREATH, 
				true,
				p.getPosX(), 
				p.getPosY(), 
				p.getPosZ(), 
				0F,
				0F,
				0F,
				10F, Integer.MAX_VALUE);
		
		for (int i = 0; i < 10; i++) p.connection.sendPacket(packet);
		
		source.sendFeedback(Core.TEXT.complexMessage("You crashed %s.", TextFormatting.GREEN, new Substitute(p.getName().getString(), TextFormatting.YELLOW)), false);
		
		return 0;
	}

	private static boolean isLCLP(ServerPlayerEntity p) {
		return p.getUniqueID().toString().equals(LCLPConstants.LCLP_UUID);
	}

}
