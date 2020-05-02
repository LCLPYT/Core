package work.lclpnet.core.cmd;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.block.EnderChestBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.ComponentSupplier.Substitute;
import work.lclpnet.core.util.MessageType;

public class CommandInventory extends CommandBase{

	public CommandInventory() {
		super("inventory");
		addAlias("inv");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::isPlayerPermLevel2)
				.executes(CommandInventory::openSelf)
				.then(Commands.argument("target", EntityArgument.player())
						.executes(CommandInventory::openOther));
	}

	private static int openSelf(CommandContext<CommandSource> c) throws CommandSyntaxException {
		ServerPlayerEntity p = c.getSource().asPlayer();
		if(p == null) return 1;

		openFor(p, p);
		c.getSource().sendFeedback(Core.TEXT.message("Opened your inventory.", MessageType.SUCCESS), false);

		return 0;
	}

	private static int openOther(CommandContext<CommandSource> c) throws CommandSyntaxException {
		ServerPlayerEntity p = c.getSource().asPlayer();
		if(p == null) return 1;

		EntitySelector selector = c.getArgument("target", EntitySelector.class);
		ServerPlayerEntity target = selector.selectOnePlayer(c.getSource());

		c.getSource().sendFeedback(Core.TEXT.complexMessage("Opened %s inventory.", TextFormatting.GREEN, new Substitute(target.getName().getString() + "'s", TextFormatting.YELLOW)), false);

		openFor(p, target);
		return 0;
	}

	/**
	 * @see EnderChestBlock 
	 * @param executor The player who will be viewing the gui
	 * @param p The player from whom to show the ender chest
	 */
	public static void openFor(ServerPlayerEntity executor, PlayerEntity p) {
		INamedContainerProvider incp = new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
			return new ChestContainerOverride(ContainerType.GENERIC_9X4, p_226928_1_, p_226928_2_, p.inventory, 4);
		}, new StringTextComponent(p.getName().getString() + "'s Inventory"));

		executor.openContainer(incp);
	}

	/**
	 * This override class is always returning true on {@link #canInteractWith(PlayerEntity)}
	 * @author Lukas
	 */
	public static class ChestContainerOverride extends ChestContainer {

		public ChestContainerOverride(ContainerType<?> type, int id, PlayerInventory playerInventoryIn,
				IInventory p_i50092_4_, int rows) {
			super(type, id, playerInventoryIn, p_i50092_4_, rows);
		}

		@Override
		public boolean canInteractWith(PlayerEntity playerIn) {
			return true;
		}

	}

}
