package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.holograms.objects.UpdatingHologramObject;
import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.utils.DExecutor;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import eu.decentsoftware.holograms.api.utils.event.EventFactory;
import eu.decentsoftware.holograms.api.utils.exception.LocationParseException;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.ITicked;
import eu.decentsoftware.holograms.event.HologramClickEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@Setter
public class Hologram extends UpdatingHologramObject implements ITicked {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    /*
     *	Hologram Cache
     */

    /**
     * This map contains all cached holograms. This map is used to get holograms by name.
     * <p>
     * Holograms are cached when they are loaded from files or created. They are removed
     * from the cache when they are deleted.
     * <p>
     * Holograms, that are only in this map and not in the {@link HologramManager}, are not
     * editable via commands. They are only editable via the API.
     *
     * @see #getCachedHologram(String)
     */
    private static final @NonNull Map<String, Hologram> CACHED_HOLOGRAMS;

    static {
        CACHED_HOLOGRAMS = new ConcurrentHashMap<>();
    }

    public static Hologram getCachedHologram(@NonNull String name) {
        return CACHED_HOLOGRAMS.get(name);
    }

    @NonNull
    @Contract(pure = true)
    public static Set<String> getCachedHologramNames() {
        return CACHED_HOLOGRAMS.keySet();
    }

    @NonNull
    @Contract(pure = true)
    public static Collection<Hologram> getCachedHolograms() {
        return CACHED_HOLOGRAMS.values();
    }

    /*
     *	Static Methods
     */

    @SuppressWarnings("unchecked")
    @NonNull
    public static Hologram fromFile(final @NotNull String filePath) throws LocationParseException, IllegalArgumentException {
        final FileConfig config = new FileConfig(DECENT_HOLOGRAMS.getPlugin(), "holograms/" + filePath);
        final String fileName = new File(filePath).getName();

        // Parse hologram name
        String name;
        if (fileName.toLowerCase().startsWith("hologram_") && fileName.length() > "hologram_".length()) {
            name = fileName.substring("hologram_".length(), fileName.length() - 4);
        } else {
            name = fileName.substring(0, fileName.length() - 4);
        }

        if (name.isEmpty()) {
            // This shouldn't happen when loading holograms from files.
            throw new IllegalArgumentException("Hologram name cannot be null or empty.");
        }

        if (Hologram.getCachedHologramNames().contains(name)) {
            throw new IllegalArgumentException("Hologram with name '" + name + "' already exists.");
        }

        // Get hologram location
        String locationString = config.getString("location");
        Location location = LocationUtils.asLocationE(locationString);

        boolean enabled = true;
        if (config.isBoolean("enabled")) {
            enabled = config.getBoolean("enabled");
        }

        Hologram hologram = new Hologram(name, location, config, enabled);
        if (config.isString("permission")) {
            hologram.setPermission(config.getString("permission"));
        }
        hologram.setDisplayRange(config.getInt("display-range", Settings.DEFAULT_DISPLAY_RANGE));
        hologram.setUpdateRange(config.getInt("update-range", Settings.DEFAULT_UPDATE_RANGE));
        hologram.setUpdateInterval(config.getInt("update-interval", Settings.DEFAULT_UPDATE_INTERVAL));
        hologram.addFlags(config.getStringList("flags").stream().map(EnumFlag::valueOf).toArray(EnumFlag[]::new));
        if (config.isBoolean("down-origin")) {
            hologram.setDownOrigin(config.getBoolean("down-origin", Settings.DEFAULT_DOWN_ORIGIN));
        }

        if (!config.contains("pages") && config.contains("lines")) {
            // Old Config
            HologramPage page = hologram.getPage(0);
            Set<String> keysLines = config.getConfigurationSection("lines").getKeys(false);
            for (int j = 1; j <= keysLines.size(); j++) {
                String path = "lines." + j;
                HologramLine line = HologramLine.fromFile(config.getConfigurationSection(path), page, page.getNextLineLocation());
                page.addLine(line);
            }
            config.set("lines", null);
            hologram.save();
            return hologram;
        }

        // New Config
        boolean firstPage = true;
        for (Map<?, ?> map : config.getMapList("pages")) {
            HologramPage page;
            if (firstPage) {
                page = hologram.getPage(0);
                firstPage = false;
            } else {
                page = hologram.addPage();
            }

            // Load click actions
            if (map.containsKey("actions")) {
                Map<String, List<String>> actionsMap = (Map<String, List<String>>) map.get("actions");
                for (ClickType clickType : ClickType.values()) {
                    if (actionsMap.containsKey(clickType.name())) {
                        List<String> clickTypeActions = actionsMap.get(clickType.name());
                        for (String clickTypeAction : clickTypeActions) {
                            try {
                                page.addAction(clickType, new Action(clickTypeAction));
                            } catch (Exception e) {
                                DECENT_HOLOGRAMS.getPlugin().getLogger().log(Level.WARNING, String.format(
                                        "Failed to parse action '%s' for hologram '%s' at page %s! Skipping...",
                                        clickTypeAction,
                                        hologram.getName(),
                                        page.getIndex()
                                ), e);
                            }
                        }
                    }
                }
            }

            // Load lines
            if (map.containsKey("lines")) {
                for (Map<?, ?> lineMap : (List<Map<?, ?>>) map.get("lines")) {
                    Map<String, Object> values = null;
                    try {
                        values = (Map<String, Object>) lineMap;
                    } catch (Exception ignored) {
                        // Ignore
                    }
                    if (values == null) continue;
                    HologramLine line = HologramLine.fromMap(values, page, page.getNextLineLocation());
                    page.addLine(line);
                }
            }
        }
        hologram.setFacing((float) config.getDouble("facing", 0.0f));
        return hologram;
    }

    /*
     *	Fields
     */

    /**
     * The lock used to synchronize the saving process of this hologram.
     *
     * @implNote This lock is used to prevent multiple threads from saving
     * the same hologram at the same time. This is important because the
     * saving process is not thread-safe in SnakeYAML.
     * @since 2.7.10
     */
    protected final Lock lock = new ReentrantLock();

    /**
     * This object serves as a mutex for all visibility-related operations.
     * <p>
     * For example, when we want to hide a hologram, that's already being
     * updated on another thread, we would need to wait for the update to
     * finish before we can hide the hologram. That is because if we didn't,
     * parts of the hologram might still be visible after the hide operation,
     * due to the update process.
     *
     * @implNote This lock is used to prevent multiple threads from modifying
     * the visibility of the same hologram at the same time. This is important
     * because the visibility of a hologram is not thread-safe.
     * @since 2.7.11
     */
    protected final Object visibilityMutex = new Object();

    protected final @NonNull String name;
    protected boolean saveToFile;
    protected final @Nullable FileConfig config;
    protected final @NonNull Map<UUID, Integer> viewerPages = new ConcurrentHashMap<>();
    protected final @NonNull Set<UUID> hidePlayers = ConcurrentHashMap.newKeySet();
    protected final @NonNull Set<UUID> showPlayers = ConcurrentHashMap.newKeySet();
    protected boolean defaultVisibleState = true;
    protected final @NonNull DList<HologramPage> pages = new DList<>();
    protected boolean downOrigin = Settings.DEFAULT_DOWN_ORIGIN;
    protected boolean alwaysFacePlayer = false;
    private final @NonNull AtomicInteger tickCounter;

    /*
     *	Constructors
     */

    /**
     * Creates a new hologram with the given name and location. The hologram will be saved to a file.
     *
     * @param name     The name of the hologram.
     * @param location The location of the hologram.
     */
    public Hologram(@NonNull String name, @NonNull Location location) {
        this(name, location, true);
    }

    /**
     * Creates a new hologram with the given name and location.
     *
     * @param name       The name of the hologram.
     * @param location   The location of the hologram.
     * @param saveToFile Whether the hologram should be saved to a file.
     */
    public Hologram(@NonNull String name, @NonNull Location location, boolean saveToFile) {
        this(name, location, saveToFile ? new FileConfig(DECENT_HOLOGRAMS.getPlugin(), String.format("holograms/%s.yml", name)) : null);
    }

    /**
     * Creates a new hologram with the given name and location. The hologram will be saved to the given file.
     *
     * @param name     The name of the hologram.
     * @param location The location of the hologram.
     * @param config   The config of the hologram.
     */
    public Hologram(@NonNull String name, @NonNull Location location, @Nullable FileConfig config) {
        this(name, location, config, true);
    }

    /**
     * Creates a new hologram with the given name and location.
     *
     * @param name     The name of the hologram.
     * @param location The location of the hologram.
     * @param config   The config of the hologram.
     * @param enabled  Whether the hologram should be enabled.
     */
    public Hologram(@NonNull String name, @NonNull Location location, @Nullable FileConfig config, boolean enabled) {
        super(location);
        this.name = name;
        this.config = config;
        this.enabled = enabled;
        this.saveToFile = this.config != null;
        this.tickCounter = new AtomicInteger();
        this.addPage();
        this.register();

        CACHED_HOLOGRAMS.put(this.name, this);
    }

    /*
     *	Tick
     */

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public long getInterval() {
        return 1L;
    }

    @Override
    public void tick() {
        if (tickCounter.get() == getUpdateInterval()) {
            tickCounter.set(1);
            updateAll();
            return;
        }
        tickCounter.incrementAndGet();
        updateAnimationsAll();
    }

    /*
     *	General Methods
     */

    @Override
    public String toString() {
        return getClass().getName() + "{" +
                "name=" + getName() +
                ", enabled=" + isEnabled() +
                "} " + super.toString();
    }

    /**
     * This method calls {@link #destroy()} before deleting the holograms file.
     */
    @Override
    public void delete() {
        super.delete();
        if (config != null) {
            config.delete();
        }
    }

    /**
     * This method disables the hologram, removes it from the {@link HologramManager},
     * removes it from the cache and hides it from all players.
     */
    @Override
    public void destroy() {
        this.disable(DisableCause.API);
        this.viewerPages.clear();
        DECENT_HOLOGRAMS.getHologramManager().removeHologram(getName());
        CACHED_HOLOGRAMS.remove(getName());
    }

    /**
     * This method enables the hologram, calls the {@link #register()} method
     * to start the update task and shows it to all players.
     */
    @Override
    public void enable() {
        synchronized (visibilityMutex) {
            super.enable();
            this.showAll();
            this.register();
        }
    }

    /**
     * This method disables the hologram, calls the {@link #unregister()} method
     * to stop the update task and hides it from all players.
     */
    @Override
    public void disable(@NonNull DisableCause cause) {
        synchronized (visibilityMutex) {
            this.unregister();
            this.hideAll();
            super.disable(cause);
        }
    }

    @Override
    public void setFacing(float facing) {
        final float prev = this.facing;

        super.setFacing(facing);

        // Update the facing for all lines, that don't yet have a different facing set.
        // We want to keep the hologram facing working as a "default" value, but we don't want
        // it to override custom line facing.
        for (HologramPage page : this.pages) {
            page.getLines().forEach(line -> {
                if (line.getFacing() == prev) {
                    line.setFacing(facing);
                }
                page.realignLines();
            });
        }
    }

    /**
     * Set the location of this hologram. This method doesn't update the holograms location
     * for the players, you have to call {@link #realignLines()} for that.
     *
     * @param location The new location of this hologram.
     */
    @Override
    public void setLocation(@NonNull Location location) {
        super.setLocation(location);
        teleportClickableEntitiesAll();
    }

    /**
     * Get hologram size. (Number of pages)
     *
     * @return Number of pages in this hologram.
     */
    public int size() {
        return pages.size();
    }

    /**
     * Save this hologram to a file asynchronously.
     *
     * @implNote Always returns true. If the hologram is not persistent,
     * this method just doesn't do anything.
     */
    public void save() {
        if (!saveToFile) {
            return;
        }

        DExecutor.execute(() -> {
            try {
                lock.tryLock(250, TimeUnit.MILLISECONDS);

                config.set("location", LocationUtils.asString(getLocation(), false));
                config.set("enabled", isEnabled());
                config.set("permission", permission == null || permission.isEmpty() ? null : permission);
                config.set("flags", flags.isEmpty() ? null : flags.stream().map(EnumFlag::name).collect(Collectors.toList()));
                config.set("display-range", displayRange);
                config.set("update-range", updateRange);
                config.set("update-interval", updateInterval);
                config.set("facing", facing);
                config.set("down-origin", downOrigin);
                config.set("pages", pages.stream().map(HologramPage::serializeToMap).collect(Collectors.toList()));
                config.saveData();
                config.reload();
            } catch (InterruptedException e) {
                // Failed to acquire lock, cancel save.
            } finally {
                // Prevents deadlocks
                lock.unlock();
            }
        });
    }

    /**
     * Create a new instance of this hologram object that's identical to this one.
     *
     * @param name     Name of the clone.
     * @param location Location of the clone.
     * @param temp     True if the clone should only exist until the next reload. (Won't save to file)
     * @return Cloned instance of this line.
     */
    public Hologram clone(@NonNull String name, @NonNull Location location, boolean temp) {
        Hologram hologram = new Hologram(name, location.clone(), !temp);
        hologram.setDownOrigin(this.isDownOrigin());
        hologram.setPermission(this.getPermission());
        hologram.setFacing(this.getFacing());
        hologram.setDisplayRange(this.getDisplayRange());
        hologram.setUpdateRange(this.getUpdateRange());
        hologram.setUpdateInterval(this.getUpdateInterval());
        hologram.addFlags(this.getFlags().toArray(new EnumFlag[0]));
        hologram.setDefaultVisibleState(this.isDefaultVisibleState());
        hologram.showPlayers.addAll(this.showPlayers);
        hologram.hidePlayers.addAll(this.hidePlayers);

        for (int i = 0; i < size(); i++) {
            HologramPage page = getPage(i);
            HologramPage clonePage = page.clone(hologram, i);
            if (hologram.pages.size() > i) {
                hologram.pages.set(i, clonePage);
            } else {
                hologram.pages.add(clonePage);
            }
        }
        return hologram;
    }

    /**
     * Handle a click on this hologram.
     *
     * @param player    The player that clicked the hologram.
     * @param entityId  The id of the clicked entity.
     * @param clickType The type of the click.
     * @return True if the click was handled, false otherwise.
     */
    public boolean onClick(final @NonNull Player player, final int entityId, final @NonNull ClickType clickType) {
        HologramPage page = getPage(player);
        if (page == null || !page.hasEntity(entityId)) {
            return false;
        }

        boolean eventNotCancelled = EventFactory.handleHologramClickEvent(player, this, page, clickType, entityId);
        if (eventNotCancelled) {
            if (!hasFlag(EnumFlag.DISABLE_ACTIONS)) {
                page.executeActions(player, clickType);
            }
            return true;
        }

        return false;
    }

    /**
     * Handle the player quit event for this hologram. This method will hide the hologram
     * from the player and remove the player from the show/hide lists.
     *
     * @param player The player that quit.
     */
    public void onQuit(@NonNull Player player) {
        hide(player);
        removeShowPlayer(player);
        removeHidePlayer(player);
        viewerPages.remove(player.getUniqueId());
    }

    /*
     *	Visibility Methods
     */

    /**
     * Set default display state
     *
     * @param state state
     */
    public void setDefaultVisibleState(boolean state) {
        this.defaultVisibleState = state;
    }

    /**
     * @return Default display state
     */
    public boolean isVisibleState() {
        return defaultVisibleState;
    }

    /**
     * Set player hide state
     *
     * @param player player
     */
    public void setHidePlayer(@NonNull Player player) {
        UUID uniqueId = player.getUniqueId();
        if (!hidePlayers.contains(uniqueId)) {
            hidePlayers.add(player.getUniqueId());
        }
    }

    /**
     * Remove a player hide state
     *
     * @param player player
     */
    public void removeHidePlayer(@NonNull Player player) {
        UUID uniqueId = player.getUniqueId();
        hidePlayers.remove(uniqueId);
    }

    /**
     * Determine if the player can't see the hologram
     *
     * @param player player
     * @return state
     */
    public boolean isHideState(@NonNull Player player) {
        return hidePlayers.contains(player.getUniqueId());
    }

    /**
     * Set player show state
     *
     * @param player player
     */
    public void setShowPlayer(@NonNull Player player) {
        UUID uniqueId = player.getUniqueId();
        if (!showPlayers.contains(uniqueId)) {
            showPlayers.add(player.getUniqueId());
        }
    }

    /**
     * Remove a player show state
     *
     * @param player player
     */
    public void removeShowPlayer(@NonNull Player player) {
        UUID uniqueId = player.getUniqueId();
        showPlayers.remove(uniqueId);
    }

    /**
     * Determine if the player can see the hologram
     *
     * @param player player
     * @return state
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isShowState(@NonNull Player player) {
        return showPlayers.contains(player.getUniqueId());
    }

    /**
     * Show this hologram for given player on a given page.
     *
     * @param player    Given player.
     * @param pageIndex Given page.
     */
    public boolean show(@NonNull Player player, int pageIndex) {
        synchronized (visibilityMutex) {
            if (isDisabled() || isHideState(player) || (!isDefaultVisibleState() && !isShowState(player))) {
                return false;
            }
            HologramPage page = getPage(pageIndex);
            if (page != null && page.size() > 0 && canShow(player) && isInDisplayRange(player)) {
                if (isVisible(player)) {
                    hide(player);
                }
                if (Version.after(8)) {
                    showPageTo(player, page, pageIndex);
                } else {
                    // We need to run the task later on older versions as, if we don't, it causes issues with some holograms *randomly* becoming invisible.
                    // I *think* this is from despawning and spawning the entities (with the same ID) in the same tick.
                    S.sync(player, () -> showPageTo(player, page, pageIndex), 0L);
                }
                return true;
            }
            return false;
        }
    }

    private void showPageTo(@NonNull Player player, @NonNull HologramPage page, int pageIndex) {
        page.getLines().forEach(line -> line.show(player));
        // Add player to viewers
        viewerPages.put(player.getUniqueId(), pageIndex);
        viewers.add(player.getUniqueId());
        showClickableEntities(player);
    }

    public void showAll() {
        synchronized (visibilityMutex) {
            if (isEnabled()) {
                Bukkit.getOnlinePlayers().forEach(player -> show(player, getPlayerPage(player)));
            }
        }
    }

    public void update(@NonNull Player player) {
        synchronized (visibilityMutex) {
            if (hasFlag(EnumFlag.DISABLE_UPDATING) || !isVisible(player) || !isInUpdateRange(player) || isHideState(player)) {
                return;
            }

            HologramPage page = getPage(player);
            if (page != null) {
                page.getLines().forEach(line -> line.update(player));
            }
        }
    }

    public void updateAll() {
        synchronized (visibilityMutex) {
            if (isEnabled() && !hasFlag(EnumFlag.DISABLE_UPDATING)) {
                getViewerPlayers().forEach(this::update);
            }
        }
    }

    public void updateAnimations(@NonNull Player player) {
        synchronized (visibilityMutex) {
            if (hasFlag(EnumFlag.DISABLE_ANIMATIONS) || !isVisible(player) || !isInUpdateRange(player) || isHideState(player)) {
                return;
            }

            HologramPage page = getPage(player);
            if (page != null) {
                page.getLines().forEach(line -> line.updateAnimations(player));
            }
        }
    }

    public void updateAnimationsAll() {
        synchronized (visibilityMutex) {
            if (isEnabled() && !hasFlag(EnumFlag.DISABLE_ANIMATIONS)) {
                getViewerPlayers().forEach(this::updateAnimations);
            }
        }
    }

    public void hide(@NonNull Player player) {
        synchronized (visibilityMutex) {
            if (isVisible(player)) {
                HologramPage page = getPage(player);
                if (page != null) {
                    page.getLines().forEach(line -> line.hide(player));
                    hideClickableEntities(player);
                }
                viewers.remove(player.getUniqueId());
            }
        }
    }

    public void hideAll() {
        synchronized (visibilityMutex) {
            if (isEnabled()) {
                getViewerPlayers().forEach(this::hide);
            }
        }
    }

    public void showClickableEntities(@NonNull Player player) {
        HologramPage page = getPage(player);
        if (page == null || !(page.isClickable() || HologramClickEvent.isRegistered())) {
            return;
        }

        // Spawn clickable entities
        NMS nms = NMS.getInstance();
        int amount = (int) (page.getHeight() / 2) + 1;
        Location location = getLocation().clone();
        location.setY((int) (location.getY() - (isDownOrigin() ? 0 : page.getHeight())) + 0.5);
        for (int i = 0; i < amount; i++) {
            int id = page.getClickableEntityId(i);
            nms.showFakeEntityArmorStand(player, location, id, true, false, true);
            location.add(0, 1.8, 0);
        }
    }

    public void showClickableEntitiesAll() {
        if (isEnabled()) {
            getViewerPlayers().forEach(this::showClickableEntities);
        }
    }

    public void hideClickableEntities(@NonNull Player player) {
        HologramPage page = getPage(player);
        if (page == null) {
            return;
        }

        // Despawn clickable entities
        NMS nms = NMS.getInstance();
        page.getClickableEntityIds().forEach(id -> nms.hideFakeEntities(player, id));
    }

    public void hideClickableEntitiesAll() {
        if (isEnabled()) {
            getViewerPlayers().forEach(this::hideClickableEntities);
        }
    }

    public void teleportClickableEntities(@NonNull Player player) {
        HologramPage page = getPage(player);
        if (page == null || !(page.isClickable() || HologramClickEvent.isRegistered())) {
            return;
        }

        // Spawn clickable entities
        NMS nms = NMS.getInstance();
        int amount = (int) (page.getHeight() / 2) + 1;
        Location location = getLocation().clone();
        location.setY((int) (location.getY() - (isDownOrigin() ? 0 : page.getHeight())) + 0.5);
        for (int i = 0; i < amount; i++) {
            int id = page.getClickableEntityId(i);
            nms.teleportFakeEntity(player, location, id);
            location.add(0, 1.8, 0);
        }
    }

    public void teleportClickableEntitiesAll() {
        if (isEnabled()) {
            getViewerPlayers().forEach(this::teleportClickableEntities);
        }
    }


    /**
     * Check whether the given player is in display range of this hologram object.
     *
     * @param player Given player.
     * @return Boolean whether the given player is in display range of this hologram object.
     */
    public boolean isInDisplayRange(@NonNull Player player) {
        /*
         * Some forks (e.g. Pufferfish) throw an exception, when we try to get
         * the world of a location, which is not loaded. We catch this exception
         * and return false, because the player is not in range.
         */
        try {
            if (player.getWorld().equals(location.getWorld())) {
                return player.getLocation().distanceSquared(location) <= displayRange * displayRange;
            }
        } catch (Exception ignored) {
            // Ignored
        }
        return false;
    }

    /**
     * Check whether the given player is in update range of this hologram object.
     *
     * @param player Given player.
     * @return Boolean whether the given player is in update range of this hologram object.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInUpdateRange(@NonNull Player player) {
        /*
         * Some forks (e.g. Pufferfish) throw an exception, when we try to get
         * the world of a location, which is not loaded. We catch this exception
         * and return false, because the player is not in range.
         */
        try {
            if (player.getWorld().equals(location.getWorld())) {
                return player.getLocation().distanceSquared(location) <= updateRange * updateRange;
            }
        } catch (Exception ignored) {
            // Ignored
        }
        return false;
    }

    public void setDownOrigin(boolean downOrigin) {
        this.downOrigin = downOrigin;
        this.hideClickableEntitiesAll();
        this.showClickableEntitiesAll();
    }

    /*
     *	Viewer Methods
     */

    public int getPlayerPage(@NonNull Player player) {
        return viewerPages.getOrDefault(player.getUniqueId(), 0);
    }

    public Set<Player> getViewerPlayers(int pageIndex) {
        Set<Player> players = new HashSet<>();
        viewerPages.forEach((uuid, integer) -> {
            if (integer == pageIndex) {
                players.add(Bukkit.getPlayer(uuid));
            }
        });
        return players;
    }

    /*
     *	Pages Methods
     */

    /**
     * Re-Align the lines in this hologram putting them to the right place.
     * <p>
     * This method is good to use after teleporting the hologram.
     * </p>
     */
    public void realignLines() {
        for (HologramPage page : pages) {
            page.realignLines();
        }
    }

    public HologramPage addPage() {
        HologramPage page = new HologramPage(this, pages.size());
        pages.add(page);
        return page;
    }

    public HologramPage insertPage(int index) {
        if (index < 0 || index > size()) return null;
        HologramPage page = new HologramPage(this, index);
        pages.add(index, page);

        // Add 1 to indexes of all the other pages.
        pages.stream().skip(index).forEach(p -> p.setIndex(p.getIndex() + 1));
        // Add 1 to all page indexes of current viewers, so they still see the same page.
        viewerPages.replaceAll((uuid, integer) -> {
            if (integer > index) {
                return integer + 1;
            }
            return integer;
        });
        return page;
    }

    public HologramPage getPage(int index) {
        if (index < 0 || index >= size()) return null;
        return pages.get(index);
    }

    public HologramPage getPage(@NonNull Player player) {
        if (isVisible(player)) {
            return getPage(getPlayerPage(player));
        }
        return null;
    }

    public HologramPage removePage(int index) {
        if (index < 0 || index >= size()) {
            return null;
        }

        HologramPage page = pages.remove(index);
        page.getLines().forEach(HologramLine::hide);

        // Update indexes of all the other pages.
        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setIndex(i);
        }

        // Update all page indexes of current viewers, so they still see the same page.
        if (pages.isNotEmpty()) {
            for (Map.Entry<UUID, Integer> entry : viewerPages.entrySet()) {
                UUID uuid = entry.getKey();
                int currentPage = viewerPages.get(uuid);
                if (currentPage == index) {
                    show(Bukkit.getPlayer(uuid), 0);
                } else if (currentPage > index) {
                    viewerPages.put(uuid, currentPage - 1);
                }
            }
        }
        return page;
    }

    public boolean swapPages(int index1, int index2) {
        if (index1 == index2 || index1 < 0 || index1 >= size() || index2 < 0 || index2 >= size()) {
            return false;
        }
        // Swap them in the list
        Collections.swap(pages, index1, index2);

        // Swap indexes of affected pages
        HologramPage page1 = getPage(index1);
        HologramPage page2 = getPage(index2);
        int i = page1.getIndex();
        page1.setIndex(page2.getIndex());
        page2.setIndex(i);

        // Swap viewers
        Set<Player> viewers1 = getViewerPlayers(index1);
        Set<Player> viewers2 = getViewerPlayers(index2);
        viewers1.forEach(player -> show(player, index2));
        viewers2.forEach(player -> show(player, index1));
        return true;
    }

}
