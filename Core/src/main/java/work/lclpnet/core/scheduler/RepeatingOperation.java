package work.lclpnet.core.scheduler;

import javax.annotation.Nullable;

public abstract class RepeatingOperation extends SchedulerBase{

	protected long delay, period, t;
	
	/**
	 * Constructs and starts a new scheduler automatically.
	 * The scheduler will be enqueued on the main thread.
	 * 
	 * @param delay The initial delay (in ticks) of the scheduler.
	 * @param period The period (in ticks) in which the scheduler will run.
	 */
	public RepeatingOperation(long delay, long period) {
		this(delay, period, true, Schedulers.MAIN_THREAD);
	}
	
	/**
	 * @param delay The initial delay (in ticks) of the scheduler.
	 * @param period The period (in ticks) in which the scheduler will run.
	 * @param autoStart If <code>true</code> is passed, the scheduler will be started automatically.
	 * @param system The {@link SchedulerSystem} to add this scheduler to. If null, the scheduler will be scheduled on a stand-alone thread. Only if <code>autoStart = true</code>.
	 */
	public RepeatingOperation(long delay, long period, boolean autoStart, @Nullable SchedulerSystem system) {
		this.delay = delay;
		this.period = period;
		this.t = period;
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
		
		if(++t >= period) {
			t = 0;
			if(super.lifecycleListener != null) {
				if(super.lifecycleListener.onRun()) run();
			} else run();
		}
		
		return false;
	}

}
