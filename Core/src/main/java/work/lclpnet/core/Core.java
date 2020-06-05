package work.lclpnet.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.custom.ServerReloadedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import work.lclpnet.core.cmd.CoreCommands;
import work.lclpnet.core.event.EventListener;
import work.lclpnet.core.event.SchedulerListener;
import work.lclpnet.corebase.util.ComponentSupplier;

@Mod(Core.MODID)
public class Core {
	
	public static final String MODID = "core";
	private static final Logger LOGGER = LogManager.getLogger();
	public static final ComponentSupplier TEXT = new ComponentSupplier("Core");
	public static volatile boolean active = false;
	private static MinecraftServer server = null;

	public Core() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		IEventBus bus = MinecraftForge.EVENT_BUS;
		bus.register(this);
		bus.register(new EventListener());
		bus.register(new SchedulerListener());
	}

	private void setup(final FMLCommonSetupEvent event) { //preinit
		LOGGER.info("Core initializing...");

		Config.load();

		LOGGER.info("Core initialized.");
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent e) {
		LOGGER.info("Core starting...");
		server = e.getServer();
		CoreCommands.registerCommands(e.getCommandDispatcher());
	}

	@SubscribeEvent
	public void onServerStarted(FMLServerStartedEvent e) {
		LOGGER.info("Core started.");
		active = true;
	}
	
	@SubscribeEvent
	public void onServerStop(FMLServerStoppingEvent e) {
		LOGGER.info("Core stopping...");
		active = false;
	}
	
	@SubscribeEvent
	public void onServerStopped(FMLServerStoppedEvent e) {
		LOGGER.info("Core stopped.");
	}

	@SubscribeEvent
	public void onServerReloaded(ServerReloadedEvent e) {
		LOGGER.info("Core reloading...");

		Config.load();

		LOGGER.info("Core reloaded.");
	}
	
	public static MinecraftServer getServer() {
		return server;
	}
	
}
