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

    private final Map<UUID, Float> lastYaws = new ConcurrentHashMap<>();
    private final Map<UUID, Float> lastYawsTemp = new ConcurrentHashMap<>();

    public OffsetListener() {
        super(2L);
    }

    public void destroy() {
        this.unregister();
        this.lastYawsTemp.clear();
        this.lastYaws.clear();
    }

    @Override
    public void tick() {
        for (Hologram hologram : Hologram.getCachedHolograms()) {
            if (!hologram.isEnabled() || !hologram.isAlwaysFacePlayer()) {
                continue;
            }

            for (Player player : hologram.getViewerPlayers()) {
                UUID uuid = player.getUniqueId();
                Location l = player.getLocation();
                float yaw = l.getYaw();

                // If the angle difference is too low, just skip the update.
                if (lastYaws.containsKey(uuid)) {
                    float lastYaw = lastYaws.get(uuid);
                    if (Math.abs(yaw - lastYaw) < 1.0f) {
                        continue;
                    }
                }

                updateOffsets(player, hologram);
                lastYawsTemp.put(uuid, yaw);
            }
        }
        lastYaws.putAll(lastYawsTemp);
        lastYawsTemp.clear();
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        lastYaws.remove(player.getUniqueId());
    }

    public void updateOffsets(Player player, Hologram hologram) {
        Location playerLoc = player.getLocation();
        Location holoLoc = hologram.getLocation();

        HologramPage page = hologram.getPage(player);
        if (page == null || !page.isAlwaysFacePlayer() || !page.hasOffsets()) {
            return;
        }

        for (HologramLine line : page.getLines()) {
            if (line.getOffsetX() == 0d && line.getOffsetZ() == 0d) {
                continue;
            }

            Location prevLocation = line.getLocation();
            OffsetCalculator.Loc2D result = OffsetCalculator.calculateOffSet(
                    new OffsetCalculator.Loc2D(playerLoc.getX(), playerLoc.getZ()),
                    new OffsetCalculator.Loc2D(line.getOffsetX(), line.getOffsetZ()),
                    new OffsetCalculator.Loc2D(holoLoc.getX(), holoLoc.getZ())
            );
            Location finalOffsetLoc = new Location(holoLoc.getWorld(), result.getX(), line.getLocation().getY(), result.getZ());
            finalOffsetLoc.setYaw(prevLocation.getYaw());

            line.setLocation(finalOffsetLoc);
            line.updateLocation(false, player);
            line.setLocation(prevLocation);
        }
    }
}