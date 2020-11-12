package work.lclpnet.core.scheduler;

import java.util.ArrayList;
import java.util.List;

import work.lclpnet.corebase.CoreBase;

public class SchedulerSystem {

	protected final List<SchedulerBase> schedulers = new ArrayList<>(), 
			cancelTasks = new ArrayList<>();

	public List<SchedulerBase> getSchedulers() {
		return schedulers;
	}
	
	public void enqueue(SchedulerBase sched) {
		if(isEnqueued(sched)) throw new IllegalStateException("Task with id " + sched.getTaskId() + " is already scheduled.");
		if(sched.lifecycleListener != null) sched.lifecycleListener.onStart();
		schedulers.add(sched);
	}
	
	public void dequeue(SchedulerBase sched) {
		if(!isEnqueued(sched)) throw new IllegalStateException("Task with id " + sched.getTaskId() + " is not yet scheduled.");
		if(sched.lifecycleListener != null) sched.lifecycleListener.onEnd();
		schedulers.remove(sched);
	}
	
	public boolean isEnqueued(SchedulerBase sched) {
		return schedulers.contains(sched);
	}
	
	public boolean isRunning(SchedulerBase sched) {
		return isEnqueued(sched) && !sched.isCancelled();
	}
	
	public void tick() {
		schedulers.forEach(sched -> {
			if(sched.tick()) cancelTasks.add(sched);
		});

		cancelTasks.forEach(this::dequeue);
		cancelTasks.clear();
	}
	
	public static class AsyncSchedulerSystem extends SchedulerSystem{
		
		private Thread thread = null;
		
		@Override
		public void enqueue(SchedulerBase sched) {
			super.enqueue(sched);
			startThread();
		}

		private void startThread() {
			if(thread != null) return;
			thread = new Thread(() -> {
				while(CoreBase.active) {
					try {
						Thread.sleep(50L);
						this.tick();
					} catch (InterruptedException e) {
						return;
					}
				}
			}, "Async Scheduler Thread");
			thread.start();
		}
		
	}

}
