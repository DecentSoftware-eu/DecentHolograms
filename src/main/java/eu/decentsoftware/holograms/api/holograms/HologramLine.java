package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.holograms.enums.HologramLineType;
import eu.decentsoftware.holograms.api.holograms.objects.HologramObject;
import eu.decentsoftware.holograms.api.nms.NMS;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.entity.DecentEntityType;
import eu.decentsoftware.holograms.api.utils.entity.HologramEntity;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Getter
@Setter
public class HologramLine extends HologramObject {

    protected static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    /*
     *	Static Methods
     */

    public static HologramLine fromFile(ConfigurationSection config, HologramPage parent, Location location) {
        HologramLine line = new HologramLine(parent, location, config.getString("content", Settings.DEFAULT_TEXT.getValue()));
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
        return line;
    }

    @SuppressWarnings("unchecked")
    public static HologramLine fromMap(@NonNull Map<String, Object> map, HologramPage parent, Location location) {
        String content = (String) map.getOrDefault("content", Settings.DEFAULT_TEXT.getValue());
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
                } catch (Exception ignored) {}
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
        return line;
    }

    /*
     *	Fields
     */

    private final HologramPage parent;
    private final Map<UUID, String> playerTextMap = new ConcurrentHashMap<>();
    private final Map<UUID, String> lastTextMap = new ConcurrentHashMap<>();
    private HologramLineType type;
    private int[] entityIds = new int[256];
    private double offsetX = 0.0D, offsetY = 0.0D, offsetZ = 0.0D;
    private double height;
    private String content;
    private String text;
    private HologramItem item;
    private HologramEntity entity;

    /*
     *	Constructors
     */

    public HologramLine(HologramPage parent, Location location, String content) {
        super(location);
        this.parent = parent;
        NMS nms = NMS.getInstance();
        this.entityIds[0] = nms.getFreeEntityId();
        this.entityIds[1] = nms.getFreeEntityId();
        this.content = content;
        this.type = HologramLineType.UNKNOWN;
        this.height = Settings.DEFAULT_HEIGHT_TEXT.getValue();
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

    public void setContent(String content) {
        this.content = content;
        this.parseContent();
        this.update();
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

    public boolean hasParent() {
        return parent != null;
    }

    public boolean isClickable() {
        return !hasFlag(EnumFlag.DISABLE_ACTIONS) && (!hasParent() || parent.isClickable());
    }

    public Location getCenter() {
        Location center = getLocation().clone();
        return hasParent() && parent.getParent().isDownOrigin() ?
                center.add(0, getHeight() / 2, 0) :
                center.subtract(0, getHeight() / 2, 0);
    }

    /**
     * Parse the current content String.
     */
    public void parseContent() {
        HologramLineType prevType = type;
        String contentU = content.toUpperCase();
        if (contentU.startsWith("#ICON:")) {
            type = HologramLineType.ICON;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_ICON.getValue();
            }
            item = new HologramItem(content.substring("#ICON:".length()));
        } else if (contentU.startsWith("#SMALLHEAD:")) {
            type = HologramLineType.SMALLHEAD;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_SMALLHEAD.getValue();
            }
            item = new HologramItem(content.substring("#SMALLHEAD:".length()));
        } else if (contentU.startsWith("#HEAD:")) {
            type = HologramLineType.HEAD;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_HEAD.getValue();
            }
            item = new HologramItem(content.substring("#HEAD:".length()));
        } else if (contentU.startsWith("#ENTITY:")) {
            type = HologramLineType.ENTITY;
            entity = new HologramEntity(content.substring("#ENTITY:".length()));
            height = NMS.getInstance().getEntityHeigth(entity.getType()) + 0.15;
            offsetY = -(height + (Common.SERVER_VERSION.isAfterOrEqual(Version.v1_13_R1) ? 0.1 : 0.2));
            return;
        } else {
            type = HologramLineType.TEXT;
            if (prevType != type) {
                height = Settings.DEFAULT_HEIGHT_TEXT.getValue();
            }
            text = content;
        }
        this.offsetY = type.getOffsetY();
    }

    public Map<String, Object> serializeToMap() {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put("content", content);
        map.put("height", height);
        if (!flags.isEmpty()) map.put("flags", flags.stream().map(EnumFlag::name).collect(Collectors.toList()));
        if (permission != null && !permission.trim().isEmpty()) map.put("permission", permission);
        if (offsetX != 0.0d) map.put("offsetX", offsetX);
        if (offsetZ != 0.0d) map.put("offsetZ", offsetZ);
        return map;
    }

    /**
     * Create a new instance of hologram line that's identical to this one.
     *
     * @param location Location of the clone.
     * @return Cloned instance of this line.
     */
    public HologramLine clone(Location location) {
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
    public HologramLineType getType() {
        return type != null ? type : HologramLineType.UNKNOWN;
    }

    /*
     *	Visibility Methods
     */

    private String getText(Player player, boolean update) {
        if (!HologramLineType.TEXT.equals(type)) return "";
        UUID uuid = player.getUniqueId();
        String string = playerTextMap.get(uuid);
        if (update || string == null) {
            string = this.text;
            // Parse placeholders
            if (!hasFlag(EnumFlag.DISABLE_PLACEHOLDERS)) {
                string = PAPI.setPlaceholders(player, string);
            }
            string = string.replace("{player}", player.getName())
                    .replace("{page}", String.valueOf(hasParent() ? parent.getIndex() + 1 : 1))
                    .replace("{pages}", String.valueOf(hasParent() ? parent.getParent().size() : 1));
            playerTextMap.put(uuid, string);
        }
        // Replace Animations
        if (!hasFlag(EnumFlag.DISABLE_ANIMATIONS)) {
            string = DECENT_HOLOGRAMS.getAnimationManager().parseTextAnimations(string);
        }
        return Common.colorize(string);
    }

    private List<Player> getPlayers(boolean onlyViewers, Player... players) {
        List<Player> playerList;
        if (players == null || players.length == 0) {
            playerList = onlyViewers ? getViewerPlayers() : new ArrayList<>(Bukkit.getOnlinePlayers());
        } else {
            playerList = Arrays.asList(players);
        }
        return playerList;
    }

    /**
     * Show this line for given players.
     *
     * @param players Given players.
     */
    public void show(Player... players) {
        if (!isEnabled()) return;
        List<Player> playerList = getPlayers(false, players);
        NMS nms = NMS.getInstance();
        for (Player player : playerList) {
            if (!isVisible(player) && canShow(player) && isInDisplayRange(player)) {
                switch (type) {
                    case TEXT:
                        nms.showFakeEntityArmorStand(player, getLocation(), entityIds[0], true, !HologramLineType.HEAD.equals(type), false);
                        nms.updateFakeEntityCustomName(player, getText(player, true), entityIds[0]);
                        break;
                    case HEAD: case SMALLHEAD:
                        nms.showFakeEntityArmorStand(player, getLocation(), entityIds[0], true, !HologramLineType.HEAD.equals(type), false);
                        ItemStack itemStack = PAPI.containsPlaceholders(getItem().getContent()) ? HologramItem.parseItemStack(getItem().getContent(), player) : getItem().parse();
                        nms.helmetFakeEntity(player, itemStack, entityIds[0]);
                        break;
                    case ICON:
                        nms.showFakeEntityArmorStand(player, getLocation(), entityIds[0], true, true, false);
                        nms.showFakeEntityItem(player, getLocation(), HologramItem.parseItemStack(getItem().getContent(), player), entityIds[1]);
                        nms.attachFakeEnity(player, entityIds[0], entityIds[1]);
                        break;
                    case ENTITY:
                        EntityType entityType = new HologramEntity(PAPI.setPlaceholders(player, getEntity().getContent())).getType();
                        if (entityType == null || !DecentEntityType.isAllowed(entityType)) break;
                        nms.showFakeEntityArmorStand(player, getLocation(), entityIds[0], true, true, false);

                        if (entity.getType().isAlive()) {
                            nms.showFakeEntityLiving(player, getLocation(), entityType, entityIds[1]);
                        } else {
                            nms.showFakeEntity(player, getLocation(), entityType, entityIds[1]);
                        }
                        nms.attachFakeEnity(player, entityIds[0], entityIds[1]);
                        break;
                    default: break;
                }
                viewers.add(player.getUniqueId());
            }
        }
    }

    /**
     * Update this line for given players.
     *
     * @param players Given players.
     */
    public void update(Player... players) {
        if (!isEnabled() || hasFlag(EnumFlag.DISABLE_UPDATING)) return;
        List<Player> playerList = getPlayers(true, players);
        NMS nms = NMS.getInstance();
        for (Player player : playerList) {
            if (!isVisible(player) || !isInUpdateRange(player)) continue;
            if (HologramLineType.TEXT.equals(type)) {
                UUID uuid = player.getUniqueId();
                String lastText = lastTextMap.get(uuid);
                String text = getText(player, true);
                if (!text.equals(lastText)) {
                    lastTextMap.put(uuid, text);
                    nms.updateFakeEntityCustomName(player, text, entityIds[0]);
                }
            } else if (HologramLineType.HEAD.equals(type) || HologramLineType.SMALLHEAD.equals(type)) {
                nms.helmetFakeEntity(player, HologramItem.parseItemStack(getItem().getContent(), player), entityIds[0]);
            }
        }
    }

    /**
     * Update the location of this line for given players.
     *
     * @param players Given players.
     */
    public void updateLocation(Player... players) {
        if (!isEnabled()) return;
        List<Player> playerList = getPlayers(true, players);
        for (Player player : playerList) {
            if (!isVisible(player) || !isInUpdateRange(player)) continue;
            if (!HologramLineType.ENTITY.equals(type)) {
                NMS.getInstance().teleportFakeEntity(player, getLocation(), entityIds[0]);
            } else {
                this.hide();
                this.show();
            }
        }
    }

    public void updateAnimations(Player... players) {
        if (!isEnabled() || hasFlag(EnumFlag.DISABLE_ANIMATIONS)) return;
        List<Player> playerList = getPlayers(true, players);
        NMS nms = NMS.getInstance();
        for (Player player : playerList) {
            if (!isVisible(player) || !isInUpdateRange(player)) continue;
            if (HologramLineType.TEXT.equals(type)) {
                UUID uuid = player.getUniqueId();
                String lastText = lastTextMap.get(uuid);
                String text = getText(player, false);
                if (!text.equals(lastText)) {
                    lastTextMap.put(uuid, text);
                    nms.updateFakeEntityCustomName(player, text, entityIds[0]);
                }
            }
        }
    }

    /**
     * Hide this line for given players.
     *
     * @param players Given players.
     */
    public void hide(Player... players) {
        List<Player> playerList = getPlayers(true, players);
        for (Player player : playerList) {
            if (isVisible(player)) {
                NMS.getInstance().hideFakeEntities(player, entityIds[0], entityIds[1]);
                viewers.remove(player.getUniqueId());
            }
        }
    }

    public boolean isInDisplayRange(Player player) {
        return parent == null || parent.getParent().isInDisplayRange(player);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isInUpdateRange(Player player) {
        return parent == null || parent.getParent().isInDisplayRange(player);
    }

    /*
     *	Override Methods
     */

    @Override
    public boolean hasFlag(EnumFlag flag) {
        return super.hasFlag(flag) || (parent != null && parent.getParent().hasFlag(flag));
    }

    @Override
    public boolean canShow(Player player) {
        return super.canShow(player) && (parent == null || parent.getParent().canShow(player));
    }

}
