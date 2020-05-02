package work.lclpnet.core.event;

import java.util.ArrayList;
import java.util.List;

import com.mojang.brigadier.ParseResults;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.bukkitlike.FoodLevelChangeEvent;
import net.minecraftforge.event.bukkitlike.PlayerJoinEvent;
import net.minecraftforge.event.bukkitlike.PlayerQuitEvent;
import net.minecraftforge.event.bukkitlike.SignChangeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.FarmlandTrampleEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import work.lclpnet.core.Config;
import work.lclpnet.core.Core;
import work.lclpnet.core.event.custom.PlayerFireExtinguishEvent;
import work.lclpnet.core.util.ComponentHelper;
import work.lclpnet.core.util.MessageType;

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
			ITextComponent itc = ComponentHelper.convertCharStyleToComponentStyle(e.getMessage(), '&');
			if(!e.getMessage().equals(itc.getFormattedText())) e.setComponent(itc);
		}
		
		if(Config.isChatSilenced()) {
			e.setCanceled(true);
			e.getPlayer().sendMessage(Core.TEXT.message("The chat is silenced right now.", MessageType.ERROR));
		}
	}
	
	@SubscribeEvent
	public static void onSignEdit(SignChangeEvent e) {
		PlayerEntity p = e.getPlayer();
		if(!p.hasPermissionLevel(2)) return;
		
		for (int i = 0; i < e.getLines().length; i++) 
			e.setLine(i, ComponentHelper.convertCharStyleToComponentStyle(e.getLine(i).getUnformattedComponentText(), '&', TextFormatting.BLACK));
	}
	
	private static List<PlayerEntity> breakingBlocks = new ArrayList<>();
	
	public static boolean isBreakingBlock(PlayerEntity player) {
		return breakingBlocks.contains(player);
	}
	
	private static void toggleBreaking(PlayerEntity player) {
		if(isBreakingBlock(player)) breakingBlocks.remove(player);
		else breakingBlocks.add(player);
	}
	
	@SubscribeEvent
	public static void onFireExtinguish(PlayerInteractEvent.LeftClickBlock e) {
		PlayerEntity player = e.getPlayer();
		toggleBreaking(player);
		
		if(!isBreakingBlock(player)) return;
		
		BlockPos neighbour = e.getPos().add(e.getFace().getDirectionVec());
		BlockState state = player.world.getBlockState(neighbour);
		if(!(state.getBlock() instanceof FireBlock)) return;

		PlayerFireExtinguishEvent event = new PlayerFireExtinguishEvent(player, neighbour, e.getPos(), e.getFace());
		MinecraftForge.EVENT_BUS.post(event);
		if(event.isCanceled()) e.setCanceled(true);
	}
	
	@SubscribeEvent
	public static void onFire(PlayerFireExtinguishEvent e) {
		e.setCanceled(true);
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
