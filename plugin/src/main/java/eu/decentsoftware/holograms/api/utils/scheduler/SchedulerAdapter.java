package eu.decentsoftware.holograms.api.utils.scheduler;

import org.bukkit.entity.Entity;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public interface SchedulerAdapter {

    /**
     * Schedules the specified task to be executed asynchronously immediately.
     *
     * @param runnable The task to execute.
     */
    void runAsync(Runnable runnable);

    /**
     * Schedules the specified task to be executed asynchronously after the time delay has passed.
     *
     * @param runnable The task to execute.
     * @param delay    The time delay to pass before the task should be executed.
     * @param unit     The time unit for the initial delay and period.
     */
    void runAsyncDelayed(Runnable runnable, long delay, TimeUnit unit);

    /**
     * Schedules the specified task to be executed asynchronously after the delay has passed,
     * and then periodically executed with the specified period.
     *
     * @param runnable The task to execute.
     * @param delay    The time delay to pass before the task should be executed.
     * @param period   The time between task executions after the first execution of the task.
     * @param unit     The time unit for the initial delay and period.
     * @return The BukkitTask that represents the scheduled task.
     */
    BukkitTask runAsyncRate(Runnable runnable, long delay, long period, TimeUnit unit);

    /**
     * Schedules a task. If the task failed to schedule because the scheduler is retired (entity removed),
     * then returns {@code false}. Otherwise, either the run callback will be invoked after the specified delay,
     * or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity,
     * remove other entities, load chunks, load worlds, modify ticket levels, etc.
     *
     * <p>
     * It is guaranteed that the task and retired callback are invoked on the region which owns the entity.
     * </p>
     *
     * @param entity   The entity relative to which the scheduler is obtained.
     * @param runnable The task to execute.
     */
    void executeAtEntity(Entity entity, Runnable runnable);

    /**
     * Schedules a task with the given delay. If the task failed to schedule because the scheduler is retired (entity removed),
     * then returns {@code false}. Otherwise, either the run callback will be invoked after the specified delay,
     * or the retired callback will be invoked if the scheduler is retired.
     * Note that the retired callback is invoked in critical code, so it should not attempt to remove the entity,
     * remove other entities, load chunks, load worlds, modify ticket levels, etc.
     *
     * <p>
     * It is guaranteed that the task and retired callback are invoked on the region which owns the entity.
     * </p>
     *
     * @param entity   The entity relative to which the scheduler is obtained.
     * @param runnable The task to execute.
     * @param delay    The time delay to pass before the task should be executed, in ticks.
     */
    void runAtEntityDelayed(Entity entity, Runnable runnable, long delay);
}
