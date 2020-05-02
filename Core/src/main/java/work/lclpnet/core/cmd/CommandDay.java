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
import work.lclpnet.core.util.ComponentSupplier.Substitute;
import work.lclpnet.core.util.MessageType;

public class CommandDay extends CommandBase{

	public CommandDay() {
		super("day");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel2)
				.executes(CommandDay::day)
				.then(Commands.argument("world", DimensionArgument.getDimension())
						.executes(CommandDay::dayWorld));
	}
	
	private static int day(CommandContext<CommandSource> ctx) {
		if(!CoreCommands.isEntity(ctx.getSource())) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("Non-Entities have to specify a worldname! e.g. /day minecraft:overworld", MessageType.ERROR));
			return 1;
		}
		
		Entity en = ctx.getSource().getEntity();

		setTime(en.world, en);
		
		return 0;
	}

	private static int dayWorld(CommandContext<CommandSource> ctx) {
		DimensionType dim = DimensionArgument.getDimensionArgument(ctx, "world");
		if(dim == null) return 1;
		
		World w = Core.getServer().getWorld(dim);
		if(w == null) return 1;
		
		setTime(w, null);
		
		ctx.getSource().sendFeedback(Core.TEXT.message("Set the time in world " + dim + " to day.", MessageType.SUCCESS), false);
		return 0;
	}
	
	private static void setTime(World world, @Nullable Entity en) {
		world.setDayTime(6000L);
		
		final ITextComponent msg = Core.TEXT.complexMessage("%s has set the time to day.", TextFormatting.GREEN, 
				new Substitute(en != null ? en.getDisplayName().getString() : "Console", TextFormatting.YELLOW));
		
		world.getPlayers().forEach(p -> {
			p.playSound(SoundEvents.BLOCK_ANVIL_LAND, SoundCategory.BLOCKS, 1F, 0F);
			p.sendMessage(msg);
		});
	}

}
