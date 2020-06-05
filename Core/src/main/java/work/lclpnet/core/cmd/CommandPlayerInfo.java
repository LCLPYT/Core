package work.lclpnet.core.cmd;

import java.net.InetSocketAddress;

import javax.annotation.Nullable;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.command.arguments.EntitySelector;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import work.lclpnet.core.Core;
import work.lclpnet.corebase.cmd.CommandBase;
import work.lclpnet.corebase.cmd.CoreCommands;
import work.lclpnet.corebase.util.MessageType;
import work.lclpnet.corebase.util.ObjectHelper;

public class CommandPlayerInfo extends CommandBase{

	public CommandPlayerInfo() {
		super("playerinfo");
	}

	@Override
	protected LiteralArgumentBuilder<CommandSource> transform(LiteralArgumentBuilder<CommandSource> builder) {
		return builder
				.requires(CoreCommands::permLevel2)
				.executes(this::infoSelf)
				.then(Commands.argument("target", EntityArgument.player())
						.executes(this::infoOther));
	}

	public int infoSelf(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		if(!CoreCommands.isPlayer(ctx.getSource())) {
			ctx.getSource().sendErrorMessage(Core.TEXT.message("Non-players have to specify a target! e.g. /playerinfo <name>", MessageType.ERROR));
			return 1;
		}

		info(ctx.getSource(), ctx.getSource().asPlayer());

		return 0;
	}

	public int infoOther(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
		EntitySelector selector = ctx.getArgument("target", EntitySelector.class);
		ServerPlayerEntity target = selector.selectOnePlayer(ctx.getSource());
		if(target == null) return 1;

		info(ctx.getSource(), target);

		return 0;
	}

	private void info(CommandSource source, ServerPlayerEntity target) {
		msg(source, "GENRAL", null);
		msg(source, "NAME", target.getName().getString());
		msg(source, "UUID", target.getUniqueID().toString());
		msg(source, "IP-ADRESS", ((InetSocketAddress) target.connection.getNetworkManager().getRemoteAddress()).getHostString());
		msg(source, "LANGUAGE", (String) ObjectHelper.get(target, "language"));

		msg(source, "STATUS", null);
		msg(source, "GAMEMODE", target.interactionManager.getGameType().name());
		msg(source, "HEALTH", String.format("%.2f", target.getHealth()));
		msg(source, "FOOD-LEVEL", target.getFoodStats().getFoodLevel());
		msg(source, "SATURAION-LEVEL", String.format("%.2f", target.getFoodStats().getSaturationLevel()));
		msg(source, "ALLOW-FLIGHT", target.abilities.allowFlying);
		msg(source, "EXPERIENCE", target.experience);
		msg(source, "EXP-LEVEL", target.experienceLevel);
		msg(source, "TOTAL-EXP", target.experienceTotal);
		msg(source, "WALKSPEED", target.abilities.getWalkSpeed());
		msg(source, "FLYSPEED", target.abilities.getFlySpeed());
		msg(source, "DIMENSION", target.world.getDimension().getType().getRegistryName().toString());
		msg(source, "INVULNERABLE", target.abilities.disableDamage);

		msg(source, "ATTRIBUTES", null);
		attr(source, "ATTACK-SPEED", target, SharedMonsterAttributes.ATTACK_SPEED);
		attr(source, "ARMOR", target, SharedMonsterAttributes.ARMOR);
		attr(source, "ARMOR-TOUGHNESS", target, SharedMonsterAttributes.ARMOR_TOUGHNESS);
		attr(source, "ATTACK-DAMAGE", target, SharedMonsterAttributes.ATTACK_DAMAGE);
		attr(source, "KNOCKBACK-RESISTANCE", target, SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
		attr(source, "LUCK", target, SharedMonsterAttributes.LUCK);
		attr(source, "MAX-HEALTH", target, SharedMonsterAttributes.MAX_HEALTH);
		attr(source, "MOVEMENT-SPEED", target, SharedMonsterAttributes.MOVEMENT_SPEED);
		attr(source, "FLYING-SPEED", target, SharedMonsterAttributes.FLYING_SPEED);
	}

	private void attr(CommandSource source, String category, ServerPlayerEntity target, IAttribute attr) {
		IAttributeInstance attribute = target.getAttribute(attr);
		if(attribute != null) msg(source, category, attribute.getValue());
	}

	private void msg(CommandSource source, String category, @Nullable Object value) {
		if(value == null) source.sendFeedback(new StringTextComponent(category).applyTextStyle(TextFormatting.BLUE), false);
		else {
			ITextComponent sibling = new StringTextComponent(value.toString()).applyTextStyle(TextFormatting.GREEN);
			source.sendFeedback(new StringTextComponent(category).applyTextStyle(TextFormatting.DARK_PURPLE).appendText(": ").appendSibling(sibling), false);
		}
	}

}
