package eu.decentsoftware.holograms.utils.scheduler;

import com.google.common.collect.Maps;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;

public class RunnableTask {

	private final JavaPlugin plugin;
	private final Map<String, Runnable> parts = Maps.newHashMap();

	private long delay, interval;
	private BukkitTask task;

	public RunnableTask(JavaPlugin plugin) {
		this(plugin, 0L, 20L);
	}

	public RunnableTask(JavaPlugin plugin, long delay, long interval) {
		this.plugin = plugin;
		this.delay = delay;
		this.interval = interval;
	}

	public void start() {
		this.task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () ->
			parts.values().forEach(Runnable::run)
		, delay, interval);
	}

	public void restart() {
		this.stop();
		this.start();
	}

	public void stop() {
		if (this.task != null) {
			this.task.cancel();
			this.task = null;
		}
	}

	public void addPart(String name, Runnable part) {
		parts.put(name, part);
	}

	public void removePart(String name) {
		parts.remove(name);
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public long getDelay() {
		return delay;
	}

	public void setInterval(long interval) {
		this.interval = interval;
	}

	public long getInterval() {
		return interval;
	}

}
