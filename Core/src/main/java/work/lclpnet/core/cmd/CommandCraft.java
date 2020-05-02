package work.lclpnet.core.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.text.TranslationTextComponent;

public class CommandCraft extends CommandBase{

	public CommandCraft() {
		super("craft");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::isPlayerPermLevel2)
				.executes(CommandCraft::openSelf);
	}

	private static int openSelf(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		ServerPlayerEntity p = ctx.getSource().asPlayer();
		if(p == null) return 1;
		
		openFor(p);
		return 0;
	}

	private static void openFor(ServerPlayerEntity p) {
		INamedContainerProvider incp = new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
			return new WorkbenchContainerOverride(p_226928_1_, p_226928_2_, IWorldPosCallable.of(p.world, p.getPosition()));
		}, new TranslationTextComponent("container.crafting"));
		
		p.openContainer(incp);
	}
	
	/**
	 * This override class is always returning true on {@link #canInteractWith(PlayerEntity)}
	 * @author Lukas
	 */
	public static class WorkbenchContainerOverride extends WorkbenchContainer {

		public WorkbenchContainerOverride(int p_i50090_1_, PlayerInventory p_i50090_2_, IWorldPosCallable p_i50090_3_) {
			super(p_i50090_1_, p_i50090_2_, p_i50090_3_);
		}
		
		@Override
		public boolean canInteractWith(PlayerEntity playerIn) {
			return true;
		}
		
	}
	
}
