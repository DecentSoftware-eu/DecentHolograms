package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class HologramLineDisplayHandler extends Ticked {

    private final Queue<LineDisplayEntry> queue = new ConcurrentLinkedQueue<>();

    public HologramLineDisplayHandler() {
        super(1L);
        this.register();
    }

    public void queue(HologramLine line, Player player) {
        if (queue.stream().anyMatch(entry -> entry.getLine() == line && player.getUniqueId().equals(entry.getPlayerId()))) {
            return;
        }

        queue.add(new LineDisplayEntry(line, player.getUniqueId()));
    }

    public void removeFromQueue(HologramLine line, Player player) {
        queue.removeIf(entry -> entry.getLine() == line && player.getUniqueId().equals(entry.getPlayerId()));
    }

    @Override
    public void tick() {
        final Set<UUID> playersUpdated = new LinkedHashSet<>(); // We only want to send the player one item per tick
        final Set<LineDisplayEntry> reQueue = new LinkedHashSet<>();

        while (!queue.isEmpty()) {
            LineDisplayEntry poll = queue.poll();

            if (poll == null) {
                continue;
            }

            Player player = Bukkit.getPlayer(poll.getPlayerId());

            if (player == null) {
                continue;
            }

            if (playersUpdated.contains(player.getUniqueId())) {
                reQueue.add(poll);
                continue;
            }

            HologramLine line = poll.getLine();

            if (!line.isVisible(player)) {
                continue;
            }

            if (canDisplay(player, line)) {
                line.showItem(player);
                playersUpdated.add(player.getUniqueId());
            } else {
                reQueue.add(poll);
            }
        }

        queue.addAll(reQueue);
    }

    private boolean canDisplay(Player player, HologramLine line) {
        return DecentHologramsAPI.get().getPlayerListener().getTicksSinceLogin(player) >= Settings.DEFAULT_MINIMUM_SESSION_TICKS_ITEM_LINE
                && line.isInItemDisplayRange(player);
    }

    @Getter
    private static class LineDisplayEntry {

        private final HologramLine line;
        private final UUID playerId;

        public LineDisplayEntry(HologramLine line, UUID playerId) {
            this.line = line;
            this.playerId = playerId;
        }
    }
}
