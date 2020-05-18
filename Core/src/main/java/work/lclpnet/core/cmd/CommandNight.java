package work.lclpnet.core.cmd;

import javax.annotation.Nullable;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.DimensionArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.MessageType;
import work.lclpnet.core.util.Substitute;

public class CommandNight extends CommandBase{

	public CommandNight() {
		super("night");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel2)
				.executes(CommandNight::night)
				.then(Commands.argument("world", DimensionArgument.getDimension())
						.executes(CommandNight::nightWorld));
	}
	
	private static int night(CommandContext<CommandSource> ctx) {
		if(!CoreCommands.isEntity(ctx.getSource())) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("Non-Entities have to specify a worldname! e.g. /night minecraft:overworld", MessageType.ERROR));
			return 1;
		}
		
		Entity en = ctx.getSource().getEntity();

		setTime(en.world, en);
		
		return 0;
	}

	private static int nightWorld(CommandContext<CommandSource> ctx) {
		DimensionType dim = DimensionArgument.getDimensionArgument(ctx, "world");
		if(dim == null) return 1;
		
		World w = Core.getServer().getWorld(dim);
		if(w == null) return 1;
		
		setTime(w, null);
		
		ctx.getSource().sendFeedback(Core.TEXT.message("Set the time in world " + w.getWorldInfo().getWorldName() + " to night.", MessageType.SUCCESS), false);
		return 0;
	}
	
	private static void setTime(World world, @Nullable Entity en) {
		world.setDayTime(16000L);
		
		final ITextComponent msg = Core.TEXT.complexMessage("%s has set the time to night.", TextFormatting.GREEN, 
				new Substitute(en != null ? en.getDisplayName().getString() : "Console", TextFormatting.YELLOW));
		
		world.getPlayers().forEach(p -> {
			p.playSound(SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1F, 0F);
			p.sendMessage(msg);
		});
	}

}
