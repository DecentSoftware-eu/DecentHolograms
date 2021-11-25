package eu.decentsoftware.holograms.api.utils.scheduler;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;

public class S {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(DECENT_HOLOGRAMS.getPlugin(), runnable);
    }

    public static void sync(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(DECENT_HOLOGRAMS.getPlugin(), runnable, delay);
    }

    public static void async(Runnable runnable) {
        try {
            Bukkit.getScheduler().runTaskAsynchronously(DECENT_HOLOGRAMS.getPlugin(), runnable);
        } catch (IllegalPluginAccessException e) {
            new Thread(runnable).start();
        }
    }

    public static void async(Runnable runnable, long delay) {
        try {
            Bukkit.getScheduler().runTaskLaterAsynchronously(DECENT_HOLOGRAMS.getPlugin(), runnable, delay);
        } catch (IllegalPluginAccessException e) {
            new Thread(runnable).start();
        }
    }

}
