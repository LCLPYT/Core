package work.lclpnet.core.scheduler;

import javax.annotation.Nullable;

public abstract class InstantOperation extends SchedulerBase{

	/**
	 * Constructs and starts a new scheduler automatically.
	 * The scheduler will be enqueued on the main thread.
	 */
	public InstantOperation() {
		this(true, Schedulers.MAIN_THREAD);
	}
	
	/**
	 * @param autoStart If <code>true</code> is passed, the scheduler will be started automatically.
	 * @param system The {@link SchedulerSystem} to add this scheduler to. If null, the scheduler will be scheduled on a stand-alone thread. Only if <code>autoStart = true</code>.
	 */
	public InstantOperation(boolean autoStart, @Nullable SchedulerSystem system) {
		if(autoStart) {
			if(system != null) system.enqueue(this);
			else Schedulers.scheduleAsync(this);
		}
	}
	
	@Override
	public boolean tick() {
		if(!super.isCancelled()) {
			if(super.lifecycleListener != null) {
				if(super.lifecycleListener.onRun()) run();
			} else run();
		}
		return true;
	}

}
