package eu.decentsoftware.holograms.api.holograms;

import com.google.common.util.concurrent.AtomicDouble;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.holograms.enums.HologramLineType;
import eu.decentsoftware.holograms.api.holograms.objects.HologramObject;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRenderer;

import eu.decentsoftware.holograms.nms.api.renderer.NmsTextHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

@Getter
@Setter
public class HologramLine extends HologramObject {

    protected static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    /*
     *	Static Methods
     */

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
        type = HologramLineType.TEXT;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_TEXT;
                previousRenderer = renderer;
                renderer = DECENT_HOLOGRAMS.getNmsAdapter().getHologramComponentFactory().createTextRenderer();

            text = parseCustomReplacements();

            containsPlaceholders = PAPI.containsPlaceholders(text);
        }
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
            string = parsePlaceholders(string, player, containsPlaceholders);
            // Update the cached text.
            playerTextMap.put(uuid, string);
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

        return content;
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
                updateTextIfNecessary(player);
            } else if (containsPlaceholders || force) {
                renderer.updateContent(player, getPartData(player, true, true));
            }
        }
    }

    /**
     * Update the location of this line for given players.
     *
     * @param players Given players.
     */
    public void updateLocation(Player... players) {
        if (isDisabled()) {
            return;
        }
        hidePreviousIfNecessary();
        List<Player> playerList = getPlayers(true, players);
        for (Player player : playerList) {
            renderer.move(player, getPartData(player, false, true));
        }
    }

    private void updateTextIfNecessary(Player player) {
        UUID uuid = player.getUniqueId();
        String lastText = lastTextMap.get(uuid);
        String updatedText = getText(player, true);
        if (!updatedText.equals(lastText)) {
            lastTextMap.put(uuid, updatedText);

            NmsHologramPartData<String> partData = new NmsHologramPartData<>(getPositionSupplier(), () -> updatedText);
            ((NmsTextHologramRenderer) renderer).updateContent(player, partData);
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends NmsHologramPartData<?>> T getPartData(Player player, boolean updateText, boolean cacheText) {
        Supplier<DecentPosition> positionSupplier = getPositionSupplier();
        return (T) getTextPartData(player, updateText, cacheText, positionSupplier);
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

    public double getOffsetX() {
        return offsetX.get();
    }

    public double getOffsetY() {
        return offsetY.get();
    }

    public double getOffsetZ() {
        return offsetZ.get();
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
