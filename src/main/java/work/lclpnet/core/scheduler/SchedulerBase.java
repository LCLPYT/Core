package work.lclpnet.core.scheduler;

public abstract class SchedulerBase implements Runnable{

	private static int newTaskID = 0;
	
	protected boolean cancelled = false;
	protected SchedulerLifecycleListener lifecycleListener = null;
	private final int taskId = newTaskID++;
	
	public void cancel() {
		if(cancelled) return;
		
		cancelled = true;
		if(lifecycleListener != null) lifecycleListener.onCancel();
	}
	
	public boolean isCancelled() {
		return cancelled;
	}

	public void setLifecycleListener(SchedulerLifecycleListener lifecycleListener) {
		this.lifecycleListener = lifecycleListener;
	}
	
	public final SchedulerLifecycleListener getLifecycleListener() {
		return lifecycleListener;
	}
	
	public int getTaskId() {
		return taskId;
	}
	
	/**
	 * This method is executed every tick.
	 * 
	 * @return True, if the task should be cancelled.
	 */
	public abstract boolean tick();

}
