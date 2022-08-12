package eu.decentsoftware.holograms.api.holograms;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.actions.Action;
import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.holograms.objects.UpdatingHologramObject;
import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.utils.collection.DList;
import eu.decentsoftware.holograms.api.utils.config.Configuration;
import eu.decentsoftware.holograms.api.utils.event.EventFactory;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.api.utils.tick.ITicked;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Setter
public class Hologram extends UpdatingHologramObject implements ITicked {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    /*
     *	Hologram Cache
     */

    private static final Map<String, Hologram> CACHED_HOLOGRAMS;

    static {
        CACHED_HOLOGRAMS = new ConcurrentHashMap<>();
    }

    public static Hologram getCachedHologram(String name) {
        return CACHED_HOLOGRAMS.get(name);
    }

    public static Set<String> getCachedHologramNames() {
        return CACHED_HOLOGRAMS.keySet();
    }

    public static Collection<Hologram> getCachedHolograms() {
        return CACHED_HOLOGRAMS.values();
    }

    /*
     *	Static Methods
     */

    @SuppressWarnings("unchecked")
    public static @Nullable Hologram fromFile(final String fileName) {
        final Configuration config = new Configuration(DECENT_HOLOGRAMS.getPlugin(), DECENT_HOLOGRAMS.getDataFolder(), "holograms/" + fileName);

        // Get hologram location
        String locationString = config.getString("location");
        Location location = LocationUtils.asLocation(locationString);
        if (location == null) return null;

        // Parse hologram name
        String name;
        if (fileName.toLowerCase().startsWith("hologram_") && fileName.length() > "hologram_".length()) {
            name = fileName.substring("hologram_".length(), fileName.length() - 4);
        } else {
            name = fileName.substring(0, fileName.length() - 4);
        }

        boolean enabled = true;
        if (config.isBoolean("enabled")) {
            enabled = config.getBoolean("enabled");
        }

        Hologram hologram = new Hologram(name, location, config, enabled);
        if (config.isString("permission")) {
            hologram.setPermission(config.getString("permission"));
        }
        hologram.setDisplayRange(config.getInt("display-range", Settings.DEFAULT_DISPLAY_RANGE.getValue()));
        hologram.setUpdateRange(config.getInt("update-range", Settings.DEFAULT_UPDATE_RANGE.getValue()));
        hologram.setUpdateInterval(config.getInt("update-interval", Settings.DEFAULT_UPDATE_INTERVAL.getValue()));
        hologram.addFlags(config.getStringList("flags").stream().map(EnumFlag::valueOf).toArray(EnumFlag[]::new));
        hologram.setFacing((float) config.getDouble("facing", 0.0f));
        if (config.isBoolean("down-origin")) {
            hologram.setDownOrigin(config.getBoolean("down-origin", Settings.DEFAULT_DOWN_ORIGIN.getValue()));
        }
//        if (config.isBoolean("always-face-player")) {
//            hologram.setAlwaysFacePlayer(config.getBoolean("always-face-player", Settings.DEFAULT_ALWAYS_FACE_PLAYER.getValue()));
//        }

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
                        clickTypeActions.forEach(action -> page.addAction(clickType, new Action(action)));
                    }
                }
            }

            // Load lines
            if (map.containsKey("lines")) {
                for (Map<?, ?> lineMap : (List<Map<?, ?>>) map.get("lines")) {
                    Map<String, Object> values = null;
                    try {
                        values = (Map<String, Object>) lineMap;
                    } catch (Exception ignored) {}
                    if (values == null) continue;
                    HologramLine line = HologramLine.fromMap(values, page, page.getNextLineLocation());
                    page.addLine(line);
                }
            }

            /*
            if (map.containsKey("always-face-player")) {
                Object afp = map.get("always-face-player");
                if (afp instanceof Boolean) {
                    page.setAlwaysFacePlayer((boolean) afp);
                }
            }
             */
        }
        return hologram;
    }

    /*
     *	Fields
     */

    protected final String name;
    protected boolean saveToFile;
    protected final Configuration config;
    protected final Map<UUID, Integer> viewerPages = new ConcurrentHashMap<>();
    protected final DList<HologramPage> pages = new DList<>();
    protected boolean downOrigin = Settings.DEFAULT_DOWN_ORIGIN.getValue();
    protected boolean alwaysFacePlayer = false; //Settings.DEFAULT_ALWAYS_FACE_PLAYER.getValue();
    private final AtomicInteger tickCounter;

    /*
     *	Constructors
     */

    public Hologram(String name, Location location) {
        this(name, location, true);
    }

    public Hologram(String name, Location location, boolean saveToFile) {
        this(name, location, saveToFile ? new Configuration(DECENT_HOLOGRAMS.getPlugin(), DECENT_HOLOGRAMS.getDataFolder(), String.format("holograms/hologram_%s.yml", name)) : null);
    }

    public Hologram(String name, Location location, Configuration config) {
        this(name, location, config, true);
    }

    public Hologram(String name, Location location, Configuration config, boolean enabled) {
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
                "name=" + name +
                ", enabled=" + enabled +
                "} " + super.toString();
    }

    @Override
    public void delete() {
        super.delete();
        if (config != null) {
            config.delete();
        }
    }

    @Override
    public void destroy() {
        this.disable();
        this.viewerPages.clear();
        DECENT_HOLOGRAMS.getHologramManager().removeHologram(getName());
        CACHED_HOLOGRAMS.remove(getName());
    }

    @Override
    public void enable() {
        super.enable();
        this.showAll();
        this.register();
    }

    @Override
    public void disable() {
        this.unregister();
        this.hideAll();
        super.disable();
    }

    @Override
    public void setLocation(Location location) {
        super.setLocation(location);
        this.hideClickableEntitiesAll();
        this.showClickableEntitiesAll();
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
     * Save this hologram to a file.
     */
    public boolean save() {
        if (!saveToFile) return true;
        config.setLocation("location", location, false);
        config.set("enabled", enabled);
        config.set("permission", permission == null || permission.isEmpty() ? null : permission);
        config.set("flags", flags.isEmpty() ? null : flags.stream().map(EnumFlag::name).collect(Collectors.toList()));
        config.set("display-range", displayRange);
        config.set("update-range", updateRange);
        config.set("update-interval", updateInterval);
        config.set("facing", facing);
        config.set("down-origin", downOrigin);
//        config.set("always-face-player", alwaysFacePlayer);
        config.set("pages", pages.stream().map(HologramPage::serializeToMap).collect(Collectors.toList()));
        
        return config.saveData() && config.reload();
    }

    /**
     * Create a new instance of this hologram object that's identical to this one.
     *
     * @param location Location of the clone.
     * @return Cloned instance of this line.
     */
    public Hologram clone(String name, Location location, boolean temp) {
        Hologram hologram = new Hologram(name, location, !temp);
        hologram.setDownOrigin(this.isDownOrigin());
        hologram.setPermission(this.getPermission());
        hologram.setFacing(this.getFacing());
        hologram.setDisplayRange(this.getDisplayRange());
        hologram.setUpdateRange(this.getUpdateRange());
        hologram.setUpdateInterval(this.getUpdateInterval());
        hologram.addFlags(this.getFlags().toArray(new EnumFlag[0]));
        for (int i = 0; i < size(); i++) {
            HologramPage page = getPage(i);
            HologramPage clonePage = page.clone(hologram, i);
            if (hologram.pages.size() > i) {
                hologram.pages.set(i, clonePage);
            }
            hologram.pages.add(clonePage);
        }
        return hologram;
    }

    public boolean onClick(Player player, int entityId, ClickType clickType) {
        if (this.hasFlag(EnumFlag.DISABLE_ACTIONS)) {
            return false;
        }
        HologramPage page = getPage(player);
        if (page != null && page.hasEntity(entityId)) {
            if (EventFactory.handleHologramInteractEvent(player, this, page, clickType, entityId)) {
                if (page.isClickable()) {
                    page.executeActions(player, clickType);
                    return true;
                }
            }
        }
        return false;
    }

    public void onQuit(Player player) {
        hide(player);
        viewerPages.remove(player.getUniqueId());
    }

    /*
     *	Visibility Methods
     */

    /**
     * Show this hologram for given player on a given page.
     *
     * @param player Given player.
     * @param pageIndex Given page.
     */
    public boolean show(Player player, int pageIndex) {
        if (!isEnabled()) return false;
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
                S.sync(() -> showPageTo(player, page, pageIndex), 0L);
            }
            return true;
        }
        return false;
    }

    private void showPageTo(Player player, HologramPage page, int pageIndex){
        page.getLines().forEach(line -> line.show(player));
        // Add player to viewers
        viewerPages.put(player.getUniqueId(), pageIndex);
        viewers.add(player.getUniqueId());
        showClickableEntities(player);
    }

    public void showAll() {
        if (isEnabled()) {
            Bukkit.getOnlinePlayers().forEach(player -> show(player, getPlayerPage(player)));
        }
    }

    public void update(Player player) {
        if (hasFlag(EnumFlag.DISABLE_UPDATING) || !isVisible(player) || !isInUpdateRange(player)) {
            return;
        }

        HologramPage page = getPage(player);
        if (page != null) {
            page.getLines().forEach(line -> line.update(player));
        }
    }

    public void updateAll() {
        if (isEnabled() && !hasFlag(EnumFlag.DISABLE_UPDATING)) {
            getViewerPlayers().forEach(this::update);
        }
    }
    
    public void updateAnimations(Player player) {
        if (hasFlag(EnumFlag.DISABLE_ANIMATIONS) || !isVisible(player) || !isInUpdateRange(player)) {
            return;
        }
        
        HologramPage page = getPage(player);
        if (page != null) {
            page.getLines().forEach(line -> line.updateAnimations(player));
        }
    }

    public void updateAnimationsAll() {
        if (isEnabled() && !hasFlag(EnumFlag.DISABLE_ANIMATIONS)) {
            getViewerPlayers().forEach(this::updateAnimations);
        }
    }

    public void hide(Player player) {
        if (isVisible(player)) {
            HologramPage page = getPage(player);
            if (page != null) {
                page.getLines().forEach(line -> line.hide(player));
                hideClickableEntities(player);
            }
            viewers.remove(player.getUniqueId());
        }
    }

    public void hideAll() {
        if (isEnabled()) {
            getViewerPlayers().forEach(this::hide);
        }
    }

    public void showClickableEntities(Player player) {
        HologramPage page = getPage(player);
        if (page == null || !page.isClickable()) {
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

    public void hideClickableEntities(Player player) {
        HologramPage page = getPage(player);
        if (page == null || !page.isClickable()) return;
        NMS nms = NMS.getInstance();
        page.getClickableEntityIds().forEach(id -> nms.hideFakeEntities(player, id));
    }

    public void hideClickableEntitiesAll() {
        if (isEnabled()) {
            getViewerPlayers().forEach(this::hideClickableEntities);
        }
    }

    /**
     * Check whether the given player is in display range of this hologram object.
     *
     * @param player Given player.
     * @return Boolean whether the given player is in display range of this hologram object.
     */
    public boolean isInDisplayRange(Player player) {
        return  player != null &&
                player.getWorld().equals(location.getWorld()) &&
                player.getLocation().distanceSquared(location) < (displayRange * displayRange);
    }

    /**
     * Check whether the given player is in update range of this hologram object.
     *
     * @param player Given player.
     * @return Boolean whether the given player is in update range of this hologram object.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInUpdateRange(Player player) {
        return  player != null &&
                player.getWorld().equals(location.getWorld()) &&
                player.getLocation().distanceSquared(location) < (updateRange * updateRange);
    }

    public void setDownOrigin(boolean downOrigin) {
        this.downOrigin = downOrigin;
        this.hideClickableEntitiesAll();
        this.showClickableEntitiesAll();
    }

    /*
     *	Viewer Methods
     */

    public int getPlayerPage(Player player) {
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
     *     This method is good to use after teleporting the hologram.
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
        if (index < 0 || index > size()) return null;
        return pages.get(index);
    }

    public HologramPage getPage(Player player) {
        if (isVisible(player)) {
            return getPage(getPlayerPage(player));
        }
        return null;
    }

    public HologramPage removePage(int index) {
        if (index < 0 || index > size()) return null;
        HologramPage page = pages.remove(index);
        page.getLines().forEach(HologramLine::hide);

        // Update indexes of all the other pages.
        for (int i = 0; i < pages.size(); i++) {
            pages.get(i).setIndex(i);
        }
        // Update all page indexes of current viewers, so they still see the same page.
        if (pages.size() > 0) {
            for (UUID uuid : viewerPages.keySet()) {
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

    /**
     * Get the list of all pages in this hologram.
     * @return List of all pages in this hologram.
     */
    public List<HologramPage> getPages() {
        return ImmutableList.copyOf(pages);
    }

}
