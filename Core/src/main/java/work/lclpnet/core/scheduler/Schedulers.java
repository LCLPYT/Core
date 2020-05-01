package work.lclpnet.core.scheduler;

import work.lclpnet.core.Core;
import work.lclpnet.core.scheduler.SchedulerSystem.AsyncSchedulerSystem;

public class Schedulers {

	public static final SchedulerSystem MAIN_THREAD = new SchedulerSystem(),
			ALT_THREAD = new AsyncSchedulerSystem();
	
	public static void scheduleSync(SchedulerBase sched) {
		if(sched != null) MAIN_THREAD.enqueue(sched);
	}
	
	public static void scheduleOnAlternativeThread(SchedulerBase sched) {
		if(sched != null) ALT_THREAD.enqueue(sched);
	}
	
	public static void scheduleAsync(SchedulerBase sched) {
		if(sched == null) return;
		
		new Thread(() -> {
			while(Core.active) {
				try {
					Thread.sleep(50L);
					if(sched.tick()) return;
				} catch (InterruptedException e) {
					return;
				}
			}
		}).start();
	}
	
}
