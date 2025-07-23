package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is a manager that handles all holograms. It is responsible for
 * loading, saving, creating, deleting, and updating holograms.
 */
public class HologramManager extends Ticked {

    private final Map<String, Hologram> hologramMap = new ConcurrentHashMap<>();
    private final Map<UUID, Long> clickCooldowns = new ConcurrentHashMap<>();
    private final Set<HologramLine> temporaryLines = ConcurrentHashMap.newKeySet();

    /**
     * Map of holograms to load when their respective world loads.
     * <p>
     * There were issues with world management plugins loading worlds
     * after holograms. Due to that, holograms in these worlds were skipped
     * as we can't load holograms, that don't have their world all loaded.
     * <p>
     * Key is the name of the world, and Value is a set of file names
     * of all holograms that couldn't be loaded due to this world problem.
     *
     * @since 2.7.4
     */
    private final Map<String, Set<String>> toLoad = new ConcurrentHashMap<>();

    public HologramManager() {
        super(20L);
        this.register();

        S.async(this::reload); // Reload when the worlds are ready
    }

    @Override
    public synchronized void tick() {
        updateVisibility();
    }

    private void updateVisibility() {
        for (Hologram hologram : Hologram.getCachedHolograms()) {
            updateVisibility(hologram);
        }
    }

    public void updateVisibility(@NonNull Hologram hologram) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateVisibility(player, hologram);
        }
    }

    public void updateVisibility(@NonNull Player player) {
        for (Hologram hologram : Hologram.getCachedHolograms()) {
            updateVisibility(player, hologram);
        }
    }

    public void updateVisibility(@NonNull Player player, @NonNull Hologram hologram) {
        if (hologram.isDisabled()) {
            return;
        }

        // Determine the player's display state of this hologram.
        if (hologram.isHideState(player) || (!hologram.isDefaultVisibleState() && !hologram.isShowState(player))) {
            if (hologram.isVisible(player)) {
                hologram.hide(player);
            }
            return;
        }

        if (!hologram.isVisible(player) && hologram.canShow(player) && hologram.isInDisplayRange(player)) {
            hologram.show(player, hologram.getPlayerPage(player));
        } else if (hologram.isVisible(player) && !(hologram.canShow(player) && hologram.isInDisplayRange(player))) {
            hologram.hide(player);
        }
    }

    /**
     * Attempts to process a click on an entity. If the entity is part of a hologram,
     * that is clickable and enabled, the click will be processed.
     *
     * @param player    The player who clicked.
     * @param entityId  Entity ID of the clicked entity.
     * @return True if the click was processed, false otherwise.
     */
    public boolean onClick(final @NonNull Player player, final int entityId) {
        final UUID uid = player.getUniqueId();

        // Check if the player is on cooldown.
        if (clickCooldowns.containsKey(uid) && System.currentTimeMillis() - clickCooldowns.get(uid) < Settings.CLICK_COOLDOWN * 50L) {
            return false;
        }

        for (Hologram hologram : Hologram.getCachedHolograms()) {
            if (!hologram.isVisible(player)) {
                continue;
            }

            if (!hologram.getLocation().getWorld().equals(player.getLocation().getWorld())) {
                continue;
            }

            // Limit the distance to 5 blocks; this is to prevent
            // any possible exploits with the entity ID.
            double dx = hologram.getLocation().getX() - player.getLocation().getX();
            double dz = hologram.getLocation().getZ() - player.getLocation().getZ();
            if (dx > 5 || dx < -5 || dz > 5 || dz < -5) {
                continue;
            }

            if (hologram.onClick(player, entityId)) {
                clickCooldowns.put(uid, System.currentTimeMillis());
                return true;
            }
        }

        return false;
    }

    public void onQuit(@NonNull Player player) {
        Hologram.getCachedHolograms().forEach(hologram -> hologram.onQuit(player));
        clickCooldowns.remove(player.getUniqueId());
    }

    /**
     * Reload this manager and all the holograms.
     */
    public synchronized void reload() {
        this.destroy();
        this.loadHolograms();
        S.async(this::updateVisibility);
    }

    private void loadHolograms() {
        hologramMap.clear();
        toLoad.clear();


        int counter = 0;
        Log.info("Loading holograms... ");

        Log.info("Loaded %d holograms!", counter);
    }

    /**
     * Destroy this manager and all the holograms.
     */
    public synchronized void destroy() {
        // Destroy registered holograms
        for (Hologram hologram : getHolograms()) {
            hologram.destroy();
        }
        hologramMap.clear();

        // Destroy temporary lines
        for (HologramLine line : temporaryLines) {
            line.destroy();
        }
        temporaryLines.clear();

        clickCooldowns.clear();
    }

    /**
     * Hide all registered holograms for the given player.
     *
     * @param player Given player.
     */
    public void hideAll(@NonNull Player player) {
        for (Hologram hologram : getHolograms()) {
            hologram.hide(player);
        }
        for (HologramLine line : temporaryLines) {
            line.hide(player);
        }
    }

    /**
     * Remove hologram by name.
     *
     * @param name Name of the hologram.
     * @return The hologram or null if it wasn't found.
     */
    public Hologram removeHologram(@NonNull String name) {
        Hologram hologram = hologramMap.remove(name);
        if (hologram != null) {
            EventFactory.fireHologramUnregisterEvent(hologram);
        }

        return hologram;
    }

    /**
     * Get all registered holograms.
     *
     * @return Collection of all registered holograms.
     */
    @NonNull
    public Collection<Hologram> getHolograms() {
        return hologramMap.values();
    }

}
