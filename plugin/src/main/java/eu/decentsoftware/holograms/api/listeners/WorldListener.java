package eu.decentsoftware.holograms.api.listeners;

import eu.decentsoftware.holograms.api.holograms.DisableCause;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.exception.LocationParseException;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WorldListener implements Listener {

    private final HologramManager hologramManager;

    public WorldListener(HologramManager hologramManager) {
        this.hologramManager = hologramManager;
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        World world = event.getWorld();

        S.async(() -> hologramManager.getHolograms().stream()
                .filter(Hologram::isEnabled)
                .filter(hologram -> hologram.getLocation().getWorld().equals(world))
                .forEach(hologram -> hologram.disable(DisableCause.WORLD_UNLOAD)));
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        World world = event.getWorld();

        S.async(() -> {
            Set<String> hologramsToLoad = getHologramsToLoadByWorld(world);
            if (!hologramsToLoad.isEmpty()) {
                hologramsToLoad.forEach(fileName -> {
                    try {
                        Hologram hologram = Hologram.fromFile(fileName);
                        if (hologram.isEnabled()) {
                            hologram.showAll();
                            hologram.realignLines();
                            hologramManager.registerHologram(hologram);
                        }
                    } catch (LocationParseException ignored) {
                        Log.warn("Failed to load hologram from file: " + fileName);
                    }
                });
            }
            hologramManager.getHolograms().stream()
                    .filter(hologram -> !hologram.isEnabled())
                    .filter(hologram -> hologram.getLocation().getWorld().equals(world))
                    .filter(hologram -> hologram.getDisableCause().equals(DisableCause.WORLD_UNLOAD))
                    .forEach(Hologram::enable);
        });
    }

    private Set<String> getHologramsToLoadByWorld(World world) {
        Set<String> hologramsToLoad = new HashSet<>();
        Map<String, Set<String>> toLoad = hologramManager.getToLoad();

        Set<String> byName = toLoad.get(world.getName());
        if (byName != null) {
            hologramsToLoad.addAll(byName);
        }

        Set<String> byUid = toLoad.get(world.getUID().toString());
        if (byUid != null) {
            hologramsToLoad.addAll(byUid);
        }
        return hologramsToLoad;
    }

}
