package work.lclpnet.core.scheduler;

import javax.annotation.Nullable;

public abstract class ScheduledOperation extends SchedulerBase{

	protected long delay, t = 0;
	
	/**
	 * Constructs and starts a new scheduler automatically.
	 * The scheduler will be enqueued on the main thread.
	 * 
	 * @param delay The initial delay (in ticks) of the scheduler.
	 */
	public ScheduledOperation(long delay) {
		this(delay, true, Schedulers.MAIN_THREAD);
	}
	
	/**
	 * @param delay The initial delay (in ticks) of the scheduler.
	 * @param autoStart If <code>true</code> is passed, the scheduler will be started automatically.
	 * @param system The {@link SchedulerSystem} to add this scheduler to. If null, the scheduler will be scheduled on a stand-alone thread. Only if <code>autoStart = true</code>.
	 */
	public ScheduledOperation(long delay, boolean autoStart, @Nullable SchedulerSystem system) {
		this.delay = delay;
		if(autoStart) {
			if(system != null) system.enqueue(this);
			else Schedulers.scheduleAsync(this);
		}
	}
	
	@Override
	public boolean tick() {
		if(super.isCancelled()) return true;
		
		if(delay > 0) {
			delay--;
			return false;
		}
		
		if(super.lifecycleListener != null) {
			if(super.lifecycleListener.onRun()) run();
		} else run();
		
		return true;
	}
	
}
