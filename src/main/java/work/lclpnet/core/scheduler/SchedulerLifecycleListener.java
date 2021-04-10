package work.lclpnet.core.scheduler;

public interface SchedulerLifecycleListener {

    /**
     * Called, when the scheduler gets enqueued.
     */
    default void onStart() {
    }

    /**
     * Called, when the scheduler get dequeued.
     */
    default void onEnd() {
    }

    /**
     * Called, when the scheduler gets cancelled.
     */
    default void onCancel() {
    }

    /**
     * Called, when the scheduler is about to run.
     *
     * @return When true is returned, the scheduler will not execute the next run.
     */
    default boolean onRun() {
        return false;
    }

}
