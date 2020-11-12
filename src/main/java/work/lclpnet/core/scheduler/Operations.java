package work.lclpnet.core.scheduler;

import java.util.concurrent.TimeUnit;

public class Operations {

	public static ScheduledOperation scheduled(final Runnable run, long delay) {
		if(run == null) return null;
		
		return new ScheduledOperation(delay) {
			
			@Override
			public void run() {
				run.run();
			}
		};
	}
	
	public static RepeatingOperation repeating(final Runnable run, long period) {
		return repeating(run, 0, period);
	}
	
	public static RepeatingOperation repeating(final Runnable run, long delay, long period) {
		if(run == null) return null;
		
		return new RepeatingOperation(delay, period) {
			
			@Override
			public void run() {
				run.run();
			}
		};
	}
	
	public static InstantOperation instant(final Runnable run) {
		if(run == null) return null;
		
		return new InstantOperation() {
			
			@Override
			public void run() {
				run.run();
			}
		};
	}
	
	public static void sync(Runnable run) {
		if(run != null) new InstantOperation() {
			
			@Override
			public void run() {
				run.run();
			}
		};
	}
	
	public static void async(Runnable run) {
		if(run != null) new Thread(run).start();
	}
	
	/**
	 * Measures the time an operation takes.
	 * 
	 * @param r The operation to run.
	 * @return The passed time the operation took to execute. In nanoseconds.
	 */
	public static long measureOperationTime(Runnable r) {
		return measureOperationTime(r, TimeUnit.NANOSECONDS);
	}
	
	public static long measureOperationTime(Runnable r, TimeUnit unit) {
		if(r == null || unit == null) return -1L;
		
		long start = System.nanoTime();
		r.run();
		long passed = System.nanoTime() - start;
		
		return unit.convert(passed, TimeUnit.NANOSECONDS);
	}
	
}
