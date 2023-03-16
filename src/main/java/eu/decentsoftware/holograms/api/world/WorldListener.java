package eu.decentsoftware.holograms.api.world;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.DisableCause;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.utils.exception.LocationParseException;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {

    private static final DecentHolograms DH = DecentHologramsAPI.get();

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        HologramManager hm = DH.getHologramManager();
        World world = event.getWorld();

        S.async(() -> hm.getHolograms().stream()
                .filter(Hologram::isEnabled)
                .filter(hologram -> hologram.getLocation().getWorld().equals(world))
                .forEach(hologram -> hologram.disable(DisableCause.WORLD_UNLOAD)));
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        HologramManager hm = DH.getHologramManager();
        World world = event.getWorld();

        S.async(() -> {
            if (hm.getToLoad().containsKey(world.getName())) {
                hm.getToLoad().get(world.getName()).forEach(fileName -> {
                    try {
                        Hologram hologram = Hologram.fromFile(fileName);
                        if (hologram != null && hologram.isEnabled()) {
                            hologram.showAll();
                            hologram.realignLines();
                            hm.registerHologram(hologram);
                        }
                    } catch (LocationParseException ignored) {
                        // Failed to load the hologram.
                    }
                });
            }
            hm.getHolograms().stream()
                    .filter(hologram -> !hologram.isEnabled())
                    .filter(hologram -> hologram.getLocation().getWorld().equals(world))
                    .filter(hologram -> hologram.getDisableCause().equals(DisableCause.WORLD_UNLOAD))
                    .forEach(Hologram::enable);
        });
    }
}
