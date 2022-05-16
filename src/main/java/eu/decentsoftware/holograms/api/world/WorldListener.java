package eu.decentsoftware.holograms.api.world;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {
    
    private final DecentHolograms decentHolograms;
    
    public WorldListener(DecentHolograms decentHolograms) {
        this.decentHolograms = decentHolograms;
    }
    
    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        decentHolograms.getHologramManager().getHolograms().stream()
            .filter(Hologram::isEnabled)
            .filter(hologram -> hologram.getLocation().getWorld().getName().equals(event.getWorld().getName()))
            .forEach(Hologram::disable);
    }
    
    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        decentHolograms.getHologramManager().getHolograms().stream()
            .filter(hologram -> !hologram.isEnabled())
            .filter(hologram -> hologram.getLocation().getWorld().getName().equals(event.getWorld().getName()))
            .forEach(Hologram::enable);
    }
}
