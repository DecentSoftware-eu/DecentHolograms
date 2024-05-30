package eu.decentsoftware.holograms.api.listeners;

import eu.decentsoftware.holograms.api.DecentHolograms;
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

@SuppressWarnings("unused")
public class WorldListener implements Listener {

    private final DecentHolograms decentHolograms;

    public WorldListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        HologramManager hologramManager = decentHolograms.getHologramManager();
        World world = event.getWorld();

        S.async(() -> hologramManager.getHolograms().stream()
                .filter(Hologram::isEnabled)
                .filter(hologram -> hologram.getLocation().getWorld().equals(world))
                .forEach(hologram -> hologram.disable(DisableCause.WORLD_UNLOAD)));
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        HologramManager hologramManager = decentHolograms.getHologramManager();
        World world = event.getWorld();

        S.async(() -> {
            if (hologramManager.getToLoad().containsKey(world.getName())) {
                hologramManager.getToLoad().get(world.getName()).forEach(fileName -> {
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
}
