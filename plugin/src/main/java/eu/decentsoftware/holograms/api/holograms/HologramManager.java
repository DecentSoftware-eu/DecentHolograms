package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.event.EventFactory;
import eu.decentsoftware.holograms.api.utils.exception.LocationParseException;
import eu.decentsoftware.holograms.api.utils.file.FileUtils;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.Ticked;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is a manager that handles all holograms. It is responsible for
 * loading, saving, creating, deleting, and updating holograms.
 */
public class HologramManager extends Ticked {

    private final DecentHolograms decentHolograms;
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

    public HologramManager(DecentHolograms decentHolograms) {
        super(20L);
        this.decentHolograms = decentHolograms;
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
     * Spawn a temporary line going to disappear after the given duration.
     *
     * @param location Location of the line.
     * @param content  Content of the line.
     * @param duration Duration to disappear after. (In ticks)
     * @return The Hologram Line.
     */
    public HologramLine spawnTemporaryHologramLine(@NonNull Location location, String content, long duration) {
        HologramLine line = new HologramLine(null, location, content);
        temporaryLines.add(line);
        line.show();
        S.async(() -> {
            line.destroy();
            temporaryLines.remove(line);
        }, duration);
        return line;
    }

    /**
     * Attempts to process a click on an entity. If the entity is part of a hologram,
     * that is clickable and enabled, the click will be processed.
     *
     * @param player    The player who clicked.
     * @param entityId  Entity ID of the clicked entity.
     * @param clickType Click type.
     * @return True if the click was processed, false otherwise.
     */
    public boolean onClick(final @NonNull Player player, final int entityId, final @NonNull ClickType clickType) {
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

            if (hologram.onClick(player, entityId, clickType)) {
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

        File folder = new File(decentHolograms.getDataFolder(), "holograms");
        List<File> files = FileUtils.getFilesFromTree(folder, Common.NAME_REGEX + "\\.yml", true);
        if (files.isEmpty()) {
            return;
        }

        int counter = 0;
        Log.info("Loading holograms... ");
        for (File file : files) {
            String filePath = FileUtils.getRelativePath(file, folder);
            try {
                registerHologram(Hologram.fromFile(filePath));
                counter++;
            } catch (LocationParseException e) {
                // This hologram will load when its world loads.
                String worldName = e.getWorldName();
                if (!toLoad.containsKey(worldName)) {
                    toLoad.put(worldName, new HashSet<>());
                }
                toLoad.get(worldName).add(filePath);
                counter++;
            } catch (Exception e) {
                Log.warn("Failed to load hologram from file '%s'!", e, filePath);
            }
        }
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
     * Show all registered holograms for the given player.
     *
     * @param player Given player.
     */
    public void showAll(@NonNull Player player) {
        for (Hologram hologram : getHolograms()) {
            hologram.show(player, hologram.getPlayerPage(player));
        }
        for (HologramLine line : temporaryLines) {
            line.show(player);
        }
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
     * Check whether a hologram with the given name is registered in this manager.
     *
     * @param name Name of the hologram.
     * @return Boolean whether a hologram with the given name is registered in this manager.
     */
    public boolean containsHologram(@NonNull String name) {
        return hologramMap.containsKey(name);
    }

    /**
     * Register a new hologram.
     *
     * @param hologram New hologram.
     */
    public void registerHologram(@NonNull Hologram hologram) {
        hologramMap.put(hologram.getName(), hologram);
        EventFactory.fireHologramRegisterEvent(hologram);
    }

    /**
     * Get hologram by name.
     *
     * @param name Name of the hologram.
     * @return The hologram or null if it wasn't found.
     */
    public Hologram getHologram(@NonNull String name) {
        return hologramMap.get(name);
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
     * Get the names of all registered holograms.
     *
     * @return Set of the names of all registered holograms.
     */
    public Set<String> getHologramNames() {
        return hologramMap.keySet();
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

    @NonNull
    public Map<String, Set<String>> getToLoad() {
        return toLoad;
    }

}
