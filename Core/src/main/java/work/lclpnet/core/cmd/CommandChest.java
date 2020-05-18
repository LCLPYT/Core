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
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.inventory.container.ChestContainer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.core.util.MessageType;
import work.lclpnet.core.util.Substitute;

public class CommandChest extends CommandBase{

	public CommandChest() {
		super("chest");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::isPlayerPermLevel2)
				.executes(CommandChest::openSelf)
				.then(Commands.argument("target", EntityArgument.player())
						.executes(CommandChest::openOther));
	}
	
	private static int openSelf(CommandContext<CommandSource> c) throws CommandSyntaxException {
		ServerPlayerEntity p = c.getSource().asPlayer();
		if(p == null) return 1;

		openFor(p, p);
		c.getSource().sendFeedback(Core.TEXT.message("Opened your ender chest.", MessageType.SUCCESS), false);

		return 0;
	}

	private static int openOther(CommandContext<CommandSource> c) throws CommandSyntaxException {
		ServerPlayerEntity p = c.getSource().asPlayer();
		if(p == null) return 1;
		
		EntitySelector selector = c.getArgument("target", EntitySelector.class);
		ServerPlayerEntity target = selector.selectOnePlayer(c.getSource());

		c.getSource().sendFeedback(Core.TEXT.complexMessage("Opened %s ender chest.", TextFormatting.GREEN, new Substitute(target.getName().getString() + "'s", TextFormatting.YELLOW)), false);

		openFor(p, target);
		return 0;
	}

	/**
	 * @see EnderChestBlock 
	 * @param executor The player who will be viewing the gui
	 * @param p The player from whom to show the ender chest
	 */
	public static void openFor(ServerPlayerEntity executor, PlayerEntity p) {
		EnderChestInventory inv = p.getInventoryEnderChest();

		INamedContainerProvider incp = new SimpleNamedContainerProvider((p_226928_1_, p_226928_2_, p_226928_3_) -> {
            return ChestContainer.createGeneric9X3(p_226928_1_, p_226928_2_, inv);
         }, new StringTextComponent(p.getName().getString() + "'s Enderchest"));
		
		executor.openContainer(incp);
	}
	
}
