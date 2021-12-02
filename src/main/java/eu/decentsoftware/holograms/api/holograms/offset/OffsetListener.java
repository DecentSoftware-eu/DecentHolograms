package eu.decentsoftware.holograms.api.holograms.offset;

import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OffsetListener extends Ticked implements Listener {

    private final Map<UUID, Double> lastAngles = new ConcurrentHashMap<>();

    public OffsetListener() {
        super(2L);
    }

    public void destroy() {
        this.unregister();
    }

    @Override
    public void tick() {
        try {
            for (Hologram hologram : Hologram.getCachedHolograms()) {
                if (!hologram.isAlwaysFacePlayer()) continue;
                Location hl = hologram.getLocation();
                for (Player player : hologram.getViewerPlayers()) {
                    UUID uuid = player.getUniqueId();
                    Location l = player.getLocation();
                    double angle = OffsetCalculator.angleOn(l.getX(), l.getZ(), hl.getX(), hl.getY());
                    if (lastAngles.containsKey(uuid)) {
                        double lastAngle = lastAngles.get(uuid);
                        if (Math.abs(lastAngle - angle) < Math.PI / 45) {
//                            continue;
                        }
                    }
                    updateOffsets(player, hologram);
                    lastAngles.put(uuid, angle);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        lastAngles.remove(player.getUniqueId());
    }

    public void updateOffsets(Player player, Hologram hologram) {
        Location playerLoc = player.getLocation();
        Location holoLoc = hologram.getLocation();
        HologramPage page = hologram.getPage(player);
        for (HologramLine line : page.getLines()) {
            if (line.getOffsetX() == 0d && line.getOffsetZ() == 0d) {
                continue;
            }
            Location prevLocation = line.getLocation();
            OffsetCalculator.Loc2D result = OffsetCalculator.calculateOffSet(
                    new OffsetCalculator.Loc2D(playerLoc.getX(), playerLoc.getZ()),
                    new OffsetCalculator.Loc2D(-line.getOffsetX(), -line.getOffsetZ()),
                    new OffsetCalculator.Loc2D(holoLoc.getX(), holoLoc.getZ())
            );
            Location finalOffsetLoc = new Location(holoLoc.getWorld(), result.getX(), line.getLocation().getY(), result.getZ());
            line.setLocation(finalOffsetLoc);
            line.updateLocation(player);
            line.setLocation(prevLocation);
        }
    }
}