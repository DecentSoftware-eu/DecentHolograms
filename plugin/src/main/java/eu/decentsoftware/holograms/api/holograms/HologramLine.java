package eu.decentsoftware.holograms.api.holograms;

import com.google.common.util.concurrent.AtomicDouble;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.holograms.enums.HologramLineType;
import eu.decentsoftware.holograms.api.holograms.objects.HologramObject;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.entity.HologramEntity;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.renderer.NmsEntityHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHeadHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsIconHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsSmallHeadHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsTextHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Getter
@Setter
public class HologramLine extends HologramObject {

    protected static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    /*
     *	Static Methods
     */

    @NonNull
    public static HologramLine fromFile(@NonNull ConfigurationSection config, @Nullable HologramPage parent, @NonNull Location location) {
        HologramLine line = new HologramLine(parent, location, config.getString("content", Settings.DEFAULT_TEXT));
        if (config.isString("permission")) {
            line.setPermission(config.getString("permission", null));
        }
        if (config.isList("flags")) {
            line.addFlags(config.getStringList("flags").stream().map(EnumFlag::valueOf).toArray(EnumFlag[]::new));
        }
        if (config.isDouble("height")) {
            line.setHeight(config.getDouble("height"));
        }
        if (config.isDouble("offsetX")) {
            line.setOffsetX(config.getDouble("offsetX"));
        }
        if (config.isDouble("offsetZ")) {
            line.setOffsetZ(config.getDouble("offsetZ"));
        }
        if (config.isDouble("facing")) {
            line.setFacing((float) config.getDouble("facing"));
        }
        return line;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    public static HologramLine fromMap(@NonNull Map<String, Object> map, @Nullable HologramPage parent, @NonNull Location location) {
        String content = (String) map.getOrDefault("content", Settings.DEFAULT_TEXT);
        HologramLine line = new HologramLine(parent, location, content);
        if (map.containsKey("height")) {
            Object height = map.get("height");
            if (height instanceof Double) {
                line.setHeight((Double) height);
            }
        }
        if (map.containsKey("flags")) {
            Object flags = map.get("flags");
            if (flags instanceof List) {
                try {
                    line.addFlags(((List<String>) flags).stream().map(EnumFlag::valueOf).toArray(EnumFlag[]::new));
                } catch (Exception e) {
                    Log.warn("Flags for line %s seem to be invalid!", content);
                }
            }
        }
        if (map.containsKey("permission")) {
            Object permission = map.get("permission");
            if (permission instanceof String) {
                line.setPermission((String) permission);
            }
        }
        if (map.containsKey("offsetX")) {
            Object offsetX = map.get("offsetX");
            if (offsetX instanceof Double) {
                line.setOffsetX((Double) offsetX);
            }
        }
        if (map.containsKey("offsetZ")) {
            Object offsetZ = map.get("offsetZ");
            if (offsetZ instanceof Double) {
                line.setOffsetZ((Double) offsetZ);
            }
        }
        if (map.containsKey("facing")) {
            Object facing = map.get("facing");
            if (facing instanceof Double) {
                line.setFacing(((Double) facing).floatValue());
            }
        }
        return line;
    }

    /*
     *	Fields
     */

    private final HologramPage parent;
    private final Map<UUID, String> playerTextMap = new ConcurrentHashMap<>();
    private final Map<UUID, String> lastTextMap = new ConcurrentHashMap<>();
    private HologramLineType type;
    private final AtomicDouble offsetX = new AtomicDouble(0d);
    private final AtomicDouble offsetY = new AtomicDouble(0d);
    private final AtomicDouble offsetZ = new AtomicDouble(0d);
    private double height;
    private String content;
    private String text;
    private HologramItem item;
    private HologramEntity entity;
    private NmsHologramRenderer<?> previousRenderer;
    private NmsHologramRenderer<?> renderer;

    private volatile boolean containsAnimations;
    private volatile boolean containsPlaceholders;

    /*
     *	Constructors
     */

    /**
     * @see eu.decentsoftware.holograms.api.DHAPI#createHologramLine(HologramPage, Location, String)
     */
    public HologramLine(@Nullable HologramPage parent, @NonNull Location location, @NotNull String content) {
        super(location);
        this.parent = parent;
        this.content = content;
        this.type = HologramLineType.UNKNOWN;
        this.height = Settings.DEFAULT_HEIGHT_TEXT;
        this.parseContent();
    }

    /*
     *	General Methods
     */

    @Override
    public String toString() {
        return "DefaultHologramLine{" +
                "content=" + content +
                "} " + super.toString();
    }

    /**
     * Set the content of the line.
     * <p>
     * This method also parses the content and updates the line.
     * <p>
     * NOTE: The new content can be null, but if it is, it will be
     * replaced with an empty string. It is recommended to not use
     * null as content.
     *
     * @param content The new content of the line.
     */
    public void setContent(@Nullable String content) {
        this.content = content == null ? "" : content;
        this.parseContent();
        this.update(true);
    }

    /**
     * Enable updating and showing to players automatically.
     */
    @Override
    public void enable() {
        super.enable();
        this.show();
    }

    /**
     * Disable updating and showing to players automatically.
     */
    @Override
    public void disable() {
        super.disable();
        this.hide();
    }

    /**
     * Parse the current content String.
     */
    public void parseContent() {
        HologramLineType prevType = type;
        String contentU = content.toUpperCase(Locale.ROOT);
        if (contentU.startsWith("#ICON:")) {
            type = HologramLineType.ICON;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_ICON;
                previousRenderer = renderer;
                renderer = DECENT_HOLOGRAMS.getNmsAdapter().getHologramComponentFactory().createIconRenderer();
            }
            item = new HologramItem(content.substring("#ICON:".length()));

            containsPlaceholders = PAPI.containsPlaceholders(item.getContent());
        } else if (contentU.startsWith("#SMALLHEAD:")) {
            type = HologramLineType.SMALLHEAD;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_SMALLHEAD;
                previousRenderer = renderer;
                renderer = DECENT_HOLOGRAMS.getNmsAdapter().getHologramComponentFactory().createSmallHeadRenderer();
            }
            item = new HologramItem(content.substring("#SMALLHEAD:".length()));
            containsPlaceholders = PAPI.containsPlaceholders(item.getContent());
        } else if (contentU.startsWith("#HEAD:")) {
            type = HologramLineType.HEAD;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_HEAD;
                previousRenderer = renderer;
                renderer = DECENT_HOLOGRAMS.getNmsAdapter().getHologramComponentFactory().createHeadRenderer();
            }
            item = new HologramItem(content.substring("#HEAD:".length()));
            containsPlaceholders = PAPI.containsPlaceholders(item.getContent());
        } else if (contentU.startsWith("#ENTITY:")) {
            type = HologramLineType.ENTITY;
            String entityContent = content.substring("#ENTITY:".length()).trim();
            entity = new HologramEntity(entityContent);
            if (prevType != type) {
                previousRenderer = renderer;
                renderer = DECENT_HOLOGRAMS.getNmsAdapter().getHologramComponentFactory().createEntityRenderer();
            }
            NmsHologramPartData<EntityType> data = new NmsHologramPartData<>(getPositionSupplier(), () -> entity.getType());
            height = ((NmsEntityHologramRenderer) renderer).getHeight(data);
            setOffsetY(-(height));
        } else {
            type = HologramLineType.TEXT;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_TEXT;
                previousRenderer = renderer;
                renderer = DECENT_HOLOGRAMS.getNmsAdapter().getHologramComponentFactory().createTextRenderer();
            }
            text = parseCustomReplacements();

            containsAnimations = DECENT_HOLOGRAMS.getAnimationManager().containsAnimations(text);
            containsPlaceholders = PAPI.containsPlaceholders(text);
        }
    }

    @NonNull
    public Map<String, Object> serializeToMap() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("content", content);
        map.put("height", height);
        if (!flags.isEmpty()) map.put("flags", flags.stream().map(EnumFlag::name).collect(Collectors.toList()));
        if (permission != null && !permission.trim().isEmpty()) map.put("permission", permission);
        if (getOffsetX() != 0.0d) map.put("offsetX", offsetX);
        if (getOffsetZ() != 0.0d) map.put("offsetZ", offsetZ);
        if (parent == null || getFacing() != parent.getParent().getFacing()) map.put("facing", facing);
        return map;
    }

    /**
     * Create a new instance of hologram line that's identical to this one.
     *
     * @param location Location of the clone.
     * @return Cloned instance of this line.
     */
    @NonNull
    public HologramLine clone(@Nullable HologramPage parent, @NonNull Location location) {
        HologramLine line = new HologramLine(parent, location, this.getContent());
        line.setHeight(this.getHeight());
        line.setOffsetY(this.getOffsetY());
        line.setOffsetX(this.getOffsetX());
        line.setOffsetZ(this.getOffsetZ());
        line.setFacing(this.getFacing());
        line.setPermission(this.getPermission());
        line.addFlags(this.getFlags().toArray(new EnumFlag[0]));
        return line;
    }

    /**
     * Get the type of this line.
     *
     * @return the type of this line.
     */
    @NonNull
    public HologramLineType getType() {
        return type != null ? type : HologramLineType.UNKNOWN;
    }

    /*
     *	Visibility Methods
     */

    @NotNull
    private String getText(@NonNull Player player, boolean update) {
        if (type != HologramLineType.TEXT) {
            return "";
        }

        UUID uuid = player.getUniqueId();
        String string = playerTextMap.get(uuid);

        // Update cache
        if (update || string == null) {
            string = text == null ? "" : text;
            // Parse placeholders.
            if (!hasFlag(EnumFlag.DISABLE_PLACEHOLDERS)) {
                string = parsePlaceholders(string, player, containsPlaceholders);
            }
            // Update the cached text.
            playerTextMap.put(uuid, string);
        }

        // Parse animations
        if (containsAnimations && !hasFlag(EnumFlag.DISABLE_ANIMATIONS)) {
            string = DECENT_HOLOGRAMS.getAnimationManager().parseTextAnimations(string);
            // Parse placeholders.
            if (Settings.ALLOW_PLACEHOLDERS_INSIDE_ANIMATIONS && !hasFlag(EnumFlag.DISABLE_PLACEHOLDERS)) {
                // This has been done to allow the use of placeholders in animation frames.
                string = parsePlaceholders(string, player, true);
            }
        }

        return Common.colorize(string);
    }

    @NonNull
    private List<Player> getPlayers(boolean onlyViewers, Player... players) {
        List<Player> playerList;
        if (players == null || players.length == 0) {
            playerList = onlyViewers ? getViewerPlayers() : new ArrayList<>(Bukkit.getOnlinePlayers());
        } else {
            playerList = Arrays.asList(players);
        }
        return playerList;
    }

    @NotNull
    private String parsePlaceholders(@NotNull String string, @NonNull Player player, boolean papi) {
        // Replace internal placeholders.
        string = string.replace("{player}", player.getName());
        string = string.replace("{page}", String.valueOf(parent != null ? parent.getIndex() + 1 : 1));
        string = string.replace("{pages}", String.valueOf(parent != null ? parent.getParent().size() : 1));

        // Replace PlaceholderAPI placeholders.
        if (papi) {
            string = PAPI.setPlaceholders(player, string);
            if (string == null) {
                // Some PlaceholderAPI placeholders might be replaced with null, so if the line content
                // is just a single placeholder, there is a possibility that the line will be null. So,
                // if that happens, replace the null with an empty string.
                string = "";
            }
        }
        return string;
    }

    @NonNull
    // Parses custom replacements that can be defined in the config
    private String parseCustomReplacements() {
        if (!content.isEmpty()) {
            for (Map.Entry<String, String> replacement : Settings.CUSTOM_REPLACEMENTS.entrySet()) {
                content = content.replace(replacement.getKey(), replacement.getValue());
            }
        }
        return content;
    }

    /**
     * Check if the given player has the permission to see this line, if any.
     *
     * @param player The player.
     * @return True, if the player has the permission to see this line, false otherwise.
     */
    public boolean hasPermission(@NonNull Player player) {
        return permission == null || permission.isEmpty() || player.hasPermission(permission);
    }

    /**
     * Update the visibility of this line for the given player. This method checks
     * if the player has the permission to see this line and if they are in the display
     * range. Then it updates the visibility accordingly.
     *
     * @param player The player to update visibility for.
     */
    public void updateVisibility(@NonNull Player player) {
        if (isVisible(player) && !(hasPermission(player) && isInDisplayRange(player))) {
            hide(player);
        } else if (!isVisible(player) && hasPermission(player) && isInDisplayRange(player)) {
            show(player);
        }
    }

    /**
     * Show this line for given players.
     *
     * @param players Given players.
     */
    public void show(Player... players) {
        if (isDisabled()) {
            return;
        }
        hidePreviousIfNecessary();
        List<Player> playerList = getPlayers(false, players);
        for (Player player : playerList) {
            if (player == null) {
                continue;
            }
            if (parent != null && parent.getParent().isHideState(player)) {
                continue;
            }
            if (!isVisible(player) && canShow(player) && isInDisplayRange(player)) {
                renderer.display(player, getPartData(player, true, false));
                viewers.add(player.getUniqueId());
            }
        }
    }

    /**
     * Update this line for given players.
     *
     * <p>This method will update the line only if it needs to be updated.</p>
     *
     * @param players Given players.
     */
    public void update(Player... players) {
        update(false, players);
    }

    /**
     * Update this line for given players.
     *
     * @param force   If true, the line will be updated even if it does not need to be.
     * @param players Given players.
     */
    public void update(boolean force, Player... players) {
        if (isDisabled() || hasFlag(EnumFlag.DISABLE_UPDATING)) {
            return;
        }

        hidePreviousIfNecessary();
        List<Player> playerList = getPlayers(true, players);
        for (Player player : playerList) {
            if (renderer instanceof NmsTextHologramRenderer) {
                updateTextIfNecessary(player, true);
            } else if (containsPlaceholders || force) {
                renderer.updateContent(player, getPartData(player, true, true));
            }
        }
    }

    private EntityType getEntityType(Player player) {
        String finalContent = PAPI.setPlaceholders(player, getEntity().getContent());
        HologramEntity hologramEntity = new HologramEntity(finalContent);
        return hologramEntity.getType();
    }

    /**
     * Update the location of this line for given players.
     *
     * @param players Given players.
     */
    public void updateLocation(boolean updateRotation, Player... players) {
        if (isDisabled()) {
            return;
        }
        hidePreviousIfNecessary();
        List<Player> playerList = getPlayers(true, players);
        for (Player player : playerList) {
            renderer.move(player, getPartData(player, false, true));
        }
    }

    public void updateAnimations(Player... players) {
        if (isDisabled() || type != HologramLineType.TEXT || hasFlag(EnumFlag.DISABLE_ANIMATIONS)) {
            return;
        }
        hidePreviousIfNecessary();
        List<Player> playerList = getPlayers(true, players);
        for (Player player : playerList) {
            if (renderer instanceof NmsTextHologramRenderer) {
                updateTextIfNecessary(player, false);
            }
        }
    }

    private void updateTextIfNecessary(Player player, boolean updatePlaceholders) {
        UUID uuid = player.getUniqueId();
        String lastText = lastTextMap.get(uuid);
        String updatedText = getText(player, updatePlaceholders);
        if (!updatedText.equals(lastText)) {
            lastTextMap.put(uuid, updatedText);

            NmsHologramPartData<String> partData = new NmsHologramPartData<>(getPositionSupplier(), () -> updatedText);
            ((NmsTextHologramRenderer) renderer).updateContent(player, partData);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends NmsHologramPartData<?>> T getPartData(Player player, boolean updateText, boolean cacheText) {
        Supplier<DecentPosition> positionSupplier = getPositionSupplier();
        if (renderer instanceof NmsTextHologramRenderer) {
            return (T) getTextPartData(player, updateText, cacheText, positionSupplier);
        } else if (renderer instanceof NmsSmallHeadHologramRenderer
                || renderer instanceof NmsHeadHologramRenderer
                || renderer instanceof NmsIconHologramRenderer) {
            return (T) new NmsHologramPartData<>(positionSupplier, () -> HologramItem.parseItemStack(item.getContent(), player));
        } else if (renderer instanceof NmsEntityHologramRenderer) {
            return (T) new NmsHologramPartData<>(positionSupplier, () -> getEntityType(player));
        }
        throw new IllegalStateException("Unsupported renderer type: " + renderer.getClass().getName());
    }

    private Supplier<DecentPosition> getPositionSupplier() {
        return () -> DecentPosition.fromBukkitLocation(getLocation());
    }

    private NmsHologramPartData<String> getTextPartData(Player player,
                                                        boolean updateText,
                                                        boolean cacheText,
                                                        Supplier<DecentPosition> positionSupplier) {
        if (!cacheText) {
            return new NmsHologramPartData<>(positionSupplier, () -> getText(player, updateText));
        }
        return new NmsHologramPartData<>(positionSupplier, () -> {
            UUID uuid = player.getUniqueId();
            String lastText = lastTextMap.get(uuid);
            String updatedText = getText(player, updateText);
            if (!updatedText.equals(lastText)) {
                lastTextMap.put(uuid, updatedText);
            }
            return updatedText;
        });
    }

    /**
     * Hide this line for given players.
     *
     * @param players Given players.
     */
    public void hide(Player... players) {
        hidePreviousIfNecessary();
        List<Player> playerList = getPlayers(true, players);
        for (Player player : playerList) {
            renderer.hide(player);
            viewers.remove(player.getUniqueId());
        }
    }

    private void hidePreviousIfNecessary() {
        if (previousRenderer == null) {
            return;
        }

        getViewerPlayers().forEach(previousRenderer::hide);
        previousRenderer = null;
    }

    public boolean isInDisplayRange(@NonNull Player player) {
        return parent == null || parent.getParent().isInDisplayRange(player);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInUpdateRange(@NonNull Player player) {
        return parent == null || parent.getParent().isInUpdateRange(player);
    }

    public double getOffsetX() {
        return offsetX.get();
    }

    public double getOffsetY() {
        return offsetY.get();
    }

    public double getOffsetZ() {
        return offsetZ.get();
    }

    public void setOffsetX(double offsetX) {
        this.offsetX.set(offsetX);
    }

    public void setOffsetY(double offsetY) {
        this.offsetY.set(offsetY);
    }

    public void setOffsetZ(double offsetZ) {
        this.offsetZ.set(offsetZ);
    }

    /*
     *	Override Methods
     */

    @Override
    public boolean hasFlag(@NonNull EnumFlag flag) {
        return super.hasFlag(flag) || (parent != null && parent.getParent().hasFlag(flag));
    }

    @Override
    public boolean canShow(@NonNull Player player) {
        return super.canShow(player) && (parent == null || parent.getParent().canShow(player));
    }

    public int[] getEntityIds() {
        return renderer.getEntityIds();
    }

}
