package eu.decentsoftware.holograms.api.utils.scheduler;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.function.Consumer;

public class ConsumerTask<T> {

	private final JavaPlugin plugin;
	private final Map<String, Consumer<T>> parts = Maps.newHashMap();
	private final T object;

	private long delay, interval;
	private BukkitTask task;

	/**
	 * Create new instance of ConsumerTask with default
	 * delay of 0 ticks and default interval of 20 ticks.
	 *
	 * @param plugin JavaPlugin executing the task.
	 * @param object Object the task is executed on.
	 */
	public ConsumerTask(JavaPlugin plugin, T object) {
		this(plugin, object, 0L, 20L);
	}

	/**
	 * Create new instance of ConsumerTask.
	 *
	 * @param plugin JavaPlugin executing the task.
	 * @param object Object the task is executed on.
	 * @param delay Delay of the task in ticks.
	 * @param interval Interval of the task in ticks.
	 */
	public ConsumerTask(JavaPlugin plugin, T object, long delay, long interval) {
		this.plugin = plugin;
		this.object = object;
		this.delay = delay;
		this.interval = interval;
	}

	/**
	 * Start the task.
	 */
	public void start() {
		this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> parts.values().forEach(c -> c.accept(object)), delay, interval);
	}

	/**
	 * Restart the task.
	 */
	public void restart() {
		this.stop();
		this.start();
	}

	/**
	 * Stop the task.
	 */
	public void stop() {
		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}
	}

	/**
	 * Add a part to the task.
	 *
	 * @param name Name of the part.
	 * @param part The part.
	 */
	public void addPart(String name, Consumer<T> part) {
		parts.put(name, part);
	}

	/**
	 * Get all the parts.
	 *
	 * @return Map of all the parts.
	 */
	public Map<String, Consumer<T>> getParts() {
		return parts;
	}

	/**
	 * Remove a part.
	 *
	 * @param name Name of the part.
	 */
	public void removePart(String name) {
		parts.remove(name);
	}

	/**
	 * Set delay of this task in ticks.
	 * <p>
	 *     You must use restart() method to update the task.
	 * </p>
	 *
	 * @param delay The new delay of the task in ticks.
	 */
	public void setDelay(long delay) {
		this.delay = delay;
	}

	/**
	 * Get the delay of the task in ticks.
	 *
	 * @return the delay of the task in ticks.
	 */
	public long getDelay() {
		return delay;
	}

	/**
	 * Set interval of this task in ticks.
	 * <p>
	 *     You must use restart() method to update the task.
	 * </p>
	 *
	 * @param interval The new interval of the task.
	 */
	public void setInterval(long interval) {
		this.interval = interval;
	}

	/**
	 * Get the interval of the task in ticks.
	 *
	 * @return the interval of the task in ticks.
	 */
	public long getInterval() {
		return interval;
	}

	/**
	 * Get the object this task is executed on.
	 *
	 * @return the object this task is executed on.
	 */
	public T getObject() {
		return object;
	}

}
