package work.lclpnet.core.event;

import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.event.TickEvent.ServerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import work.lclpnet.core.Core;
import work.lclpnet.core.scheduler.Schedulers;

@EventBusSubscriber(bus = Bus.FORGE, modid = Core.MODID)
public class SchedulerListener {

	@SubscribeEvent
	public static void onServerTick(ServerTickEvent e) {
		if(e.phase != Phase.START) return;

		Schedulers.MAIN_THREAD.tick();
	}
	
}
