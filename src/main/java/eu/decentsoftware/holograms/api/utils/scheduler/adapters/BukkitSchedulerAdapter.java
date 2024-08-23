package eu.decentsoftware.holograms.api.utils.scheduler.adapters;

import eu.decentsoftware.holograms.api.utils.scheduler.SchedulerAdapter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BukkitSchedulerAdapter implements SchedulerAdapter {

    private @NonNull Plugin plugin;

    @Override
    public void runAsync(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
    }

    @Override
    public void runAsyncDelayed(Runnable runnable, long delay, TimeUnit unit) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, runnable, unit.toMillis(delay) / 50);
    }

    @Override
    public BukkitTask runAsyncRate(Runnable runnable, long delay, long period, TimeUnit unit) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, runnable, unit.toMillis(delay) / 50, unit.toMillis(period) / 50);
    }

    @Override
    public void executeAtEntity(Entity entity, Runnable runnable) {
        Bukkit.getScheduler().runTask(plugin, runnable);
    }

    @Override
    public void runAtEntityDelayed(Entity entity, Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }
}
