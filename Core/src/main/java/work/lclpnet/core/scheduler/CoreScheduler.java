package work.lclpnet.core.scheduler;

public abstract class CoreScheduler {

	protected SchedulerBase task = null;
	
	public abstract void run();
	
	public ScheduledOperation runTaskLater(long delay) {
		checkNotYetScheduled();
		task = new ScheduledOperation(delay) {
			
			@Override
			public void run() {
				CoreScheduler.this.run();
			}
		};
		return (ScheduledOperation) task;
	}
	
	public RepeatingOperation runTaskTimer(long delay, long period) {
		checkNotYetScheduled();
		task = new RepeatingOperation(delay, period) {
			
			@Override
			public void run() {
				CoreScheduler.this.run();
			}
		};
		return (RepeatingOperation) task;
	}
	
	public void cancel() {
		if(task != null) task.cancel();
	}
	
    private void checkNotYetScheduled() {
        if (task != null) throw new IllegalStateException("Already scheduled as " + task.getTaskId());
    }
	
}
