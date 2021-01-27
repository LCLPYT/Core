package work.lclpnet.core.event;

import com.mojang.brigadier.ParseResults;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Util;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import work.lclpnet.core.Config;
import work.lclpnet.core.Core;
import work.lclpnet.core.event.custom.PlayerFireExtinguishEvent;
import work.lclpnet.corebase.event.custom.FoodLevelChangeEvent;
import work.lclpnet.corebase.event.custom.PlayerJoinEvent;
import work.lclpnet.corebase.event.custom.PlayerQuitEvent;
import work.lclpnet.corebase.event.custom.SignChangeEvent;
import work.lclpnet.corebase.util.ComponentHelper;
import work.lclpnet.corebase.util.MessageType;
import work.lclpnet.corebase.util.TextComponentHelper;

@EventBusSubscriber(modid = Core.MODID, bus = Bus.FORGE)
public class EventListener {

	@SubscribeEvent
	public static void onJoin(PlayerJoinEvent e) {
		PlayerEntity p = e.getPlayer();
		e.setJoinMessage(Config.getJoinMessageComponent(p.getName().getString()));
	}

	@SubscribeEvent
	public static void onQuit(PlayerQuitEvent e) {
		PlayerEntity p = e.getPlayer();
		e.setQuitMessage(Config.getQuitMessageComponent(p.getName().getString()));
	}

	@SubscribeEvent
	public static void onTrample(FarmlandTrampleEvent e) {
		if(Config.isFarmlandTramplingDisabled()) e.setCanceled(true);
	}

	@SubscribeEvent
	public static void onHungerChange(FoodLevelChangeEvent e) {
		if(Config.isNoHunger() && e.getToLevel() < e.getFromLevel()) e.setCanceled(true); 
	}
	
	@SubscribeEvent
	public static void onChat(ServerChatEvent e) {
		if(e.getPlayer().hasPermissionLevel(2)) {
			IFormattableTextComponent itc = ComponentHelper.convertCharStyleToComponentStyle(e.getMessage(), '&');
			if(TextComponentHelper.hasDeepFormatting(itc)) {
				boolean modDirectly = false;
				if(e.getComponent() instanceof TranslationTextComponent) {
					TranslationTextComponent ttc = (TranslationTextComponent) e.getComponent();
					if(ttc.formatArgs.length == 2 && ttc.formatArgs[1] instanceof ITextComponent) {
						modDirectly = true;
						ttc.formatArgs[1] = itc;
					}
				}

				if(!modDirectly) e.setComponent(itc);
			}
		}

		if(Config.isChatSilenced()) {
			e.setCanceled(true);
			e.getPlayer().sendMessage(Core.TEXT.message("The chat is silenced right now.", MessageType.ERROR), Util.DUMMY_UUID);
		}
	}

	@SubscribeEvent
	public static void onSignEdit(SignChangeEvent e) {
		PlayerEntity p = e.getPlayer();
		if(!p.hasPermissionLevel(2)) return;

		for (int i = 0; i < e.getLines().size(); i++) 
			e.setComponentLine(i, ComponentHelper.convertCharStyleToComponentStyle(e.getLine(i), '&', TextFormatting.BLACK));
	}

	@SubscribeEvent
	public static void onFireExtinguish(PlayerInteractEvent.LeftClickBlock e) {
		PlayerEntity player = e.getPlayer();

		BlockState state = player.world.getBlockState(e.getPos());
		if(!(state.getBlock() instanceof FireBlock)) return;

		PlayerFireExtinguishEvent event = new PlayerFireExtinguishEvent(player, e.getPos(), e.getFace());
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled()) e.setCanceled(true);
	}

	@SubscribeEvent
	public static void onCMDEcho(CommandEvent e) {
		if(!Config.isCMDEcho()) return;

		ParseResults<CommandSource> parseResults = e.getParseResults();
		String input = parseResults.getReader().getString();
		StringTextComponent message = new StringTextComponent("> " + (input.startsWith("/") ? input.substring(1) : input));
		parseResults.getContext().getSource().sendFeedback(message, false);
	}

}
