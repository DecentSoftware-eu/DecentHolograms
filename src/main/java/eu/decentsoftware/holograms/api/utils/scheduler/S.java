package eu.decentsoftware.holograms.api.utils.scheduler;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.DExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class S {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    public static void sync(Entity entity, Runnable runnable, long delay) {
        DECENT_HOLOGRAMS.getScheduler().runAtEntityDelayed(entity, runnable, delay);
    }


    public static void async(Runnable runnable) {
        try {
            DECENT_HOLOGRAMS.getScheduler().runAsync(runnable);
        } catch (IllegalPluginAccessException e) {
            DExecutor.execute(runnable);
        }
    }

    public static void async(Runnable runnable, long delay) {
        try {
            DECENT_HOLOGRAMS.getScheduler().runAsyncDelayed(runnable, delay * 50, TimeUnit.MILLISECONDS);
        } catch (IllegalPluginAccessException e) {
            DExecutor.execute(runnable);
        }
    }

    public static BukkitTask asyncTask(Runnable runnable, long interval, long delay) {
        return DECENT_HOLOGRAMS.getScheduler().runAsyncRate(runnable, 0, interval * 50, TimeUnit.MILLISECONDS);
    }

}
