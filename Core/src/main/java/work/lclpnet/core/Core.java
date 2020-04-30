package work.lclpnet.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.custom.ServerReloadedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.versions.forge.ForgeVersion;
import work.lclpnet.core.cmd.CoreCommands;
import work.lclpnet.core.event.EventListener;
import work.lclpnet.core.util.ComponentSupplier;

@Mod(Core.MODID)
public class Core {
	public static final String MODID = "core";
	private static final Logger LOGGER = LogManager.getLogger();
	public static ComponentSupplier TEXT = new ComponentSupplier("Core");

	public Core() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.register(new EventListener());
	}

	private void setup(final FMLCommonSetupEvent event) { //preinit
		LOGGER.info("Core initializing...");

		Config.load();

		LOGGER.info("Core initialized.");
	}

	@SubscribeEvent
	public void onServerStarting(FMLServerStartingEvent e) {
		LOGGER.info("Core starting...");
		CoreCommands.registerCommands(e.getCommandDispatcher());
		
		System.out.println(ForgeVersion.MODIFIED_VERSION);
	}

	@SubscribeEvent
	public void onServerStarted(FMLServerStartedEvent e) {
		LOGGER.info("Core started.");
	}

	@SubscribeEvent
	public void onServerReloaded(ServerReloadedEvent e) {
		LOGGER.info("Core reloading...");

		Config.load();

		LOGGER.info("Core reloaded.");
	}

}
