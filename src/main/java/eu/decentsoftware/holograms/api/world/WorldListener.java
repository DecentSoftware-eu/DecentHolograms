package eu.decentsoftware.holograms.api.world;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.holograms.DisableCause;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.api.utils.exception.LocationParseException;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {

    private static final DecentHolograms DH = DecentHologramsAPI.get();

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        DH.getHologramManager().getHolograms().stream()
                .filter(Hologram::isEnabled)
                .filter(hologram -> hologram.getLocation().getWorld().getName().equals(event.getWorld().getName()))
                .forEach(hologram -> hologram.disable(DisableCause.WORLD_UNLOAD));
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        HologramManager hm = DH.getHologramManager();
        String worldName = event.getWorld().getName();
        if (hm.getToLoad().containsKey(worldName)) {
            hm.getToLoad().get(worldName).forEach((fileName) -> {
                try {
                    Hologram hologram = Hologram.fromFile(fileName);
                    if (hologram != null && hologram.isEnabled()) {
                        hologram.showAll();
                        hologram.realignLines();
                        hm.registerHologram(hologram);
                    }
                } catch (LocationParseException ignored) {
                }
            });
        }
        hm.getHolograms().stream()
                .filter(hologram -> !hologram.isEnabled())
                .filter(hologram -> hologram.getLocation().getWorld().getName().equals(worldName))
                .filter(hologram -> hologram.getDisableCause().equals(DisableCause.WORLD_UNLOAD))
                .forEach(Hologram::enable);
    }
}
