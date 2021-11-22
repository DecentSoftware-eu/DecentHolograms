package eu.decentsoftware.holograms.api.utils.scheduler;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import org.bukkit.Bukkit;

public class S {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    public static void sync(Runnable runnable) {
        Bukkit.getScheduler().runTask(DECENT_HOLOGRAMS.getPlugin(), runnable);
    }

    public static void async(Runnable runnable) {
        Bukkit.getScheduler().runTaskAsynchronously(DECENT_HOLOGRAMS.getPlugin(), runnable);
    }

    public static void async(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLaterAsynchronously(DECENT_HOLOGRAMS.getPlugin(), runnable, delay);
    }

}
