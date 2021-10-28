package eu.decentsoftware.holograms.core.holograms;

import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramLineType;
import eu.decentsoftware.holograms.api.nms.NMSAdapter;
import eu.decentsoftware.holograms.api.objects.enums.EnumFlag;
import eu.decentsoftware.holograms.utils.PAPI;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.entity.HologramEntity;
import eu.decentsoftware.holograms.utils.items.HologramItem;
import eu.decentsoftware.holograms.utils.reflect.ServerVersion;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Getter
@Setter
public class DefaultHologramLine implements HologramLine {

	protected static final DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();
	protected static final AtomicInteger lastEntityId = new AtomicInteger(1_000_000);

	public static HologramLine fromFile(ConfigurationSection config, Location location) {
		DefaultHologramLine line = new DefaultHologramLine(location, config.getString("content", Settings.DEFAULT_TEXT.getValue()));
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

	/*
	 *	Variables
	 */

	protected final Set<EnumFlag> flags = Sets.newHashSet();
	protected final Set<UUID> viewers = Sets.newHashSet();
	protected HologramLineType type;
	protected int entityId;
	protected Hologram parent;
	protected String permission;
	protected Location location;
	protected double offsetX, offsetY, offsetZ;
	protected double height;
	protected String content;
	protected String text;
	protected HologramItem item;
	protected HologramEntity entity;

	/*
	 *	Constructors
	 */

	public DefaultHologramLine(Location location, String content) {
		this.type = HologramLineType.UNKNOWN;
		this.entityId = lastEntityId.addAndGet(250);
		this.location = location;
		this.content = content;
		this.height = Settings.DEFAULT_HEIGHT_TEXT.getValue();
		this.permission = null;
		this.offsetX = 0.0D;
		this.offsetY = 0.0D;
		this.offsetZ = 0.0D;
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

	@Override
	public HologramLine clone(Location location) {
		HologramLine line = new DefaultHologramLine(location, this.getContent());
		line.setHeight(this.getHeight());
		line.setOffsetY(this.getOffsetY());
		line.setOffsetX(this.getOffsetX());
		line.setOffsetZ(this.getOffsetZ());
		line.setPermission(this.getPermission());
		line.addFlags(this.getFlags().toArray(new EnumFlag[0]));
		return line;
	}

	@Override
	public void destroy() {
		this.setParent(null);
		this.hide(Bukkit.getOnlinePlayers().toArray(new Player[0]));
	}

	@Override
	public void save(ConfigurationSection config) {
		config.set("content", content);
		config.set("flags", flags.isEmpty() ? null : flags.stream().map(EnumFlag::name).collect(Collectors.toList()));
		config.set("permission", permission == null || permission.isEmpty() ? null : permission);
		config.set("height", height);
		config.set("offsetX", offsetX == 0.0D ? null : offsetX);
		config.set("offsetZ", offsetZ == 0.0D ? null : offsetZ);
	}

	@Override
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
			height = PLUGIN.getNMSAdapter().getEntityHeigth(entity.getType()) + 0.15;
			offsetY = -(height + (Common.SERVER_VERSION.isAfterOrEqual(ServerVersion.v1_13_R1) ? 0.1 : 0.2));
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

	@Override
	public void setContent(String content) {
		this.content = content;
		this.parseContent();
	}

	@Override
	public HologramLineType getType() {
		return type != null ? type : HologramLineType.UNKNOWN;
	}

	/*
	 *	Visibility Methods
	 */

	private String getText(Player player) {
		if (!HologramLineType.TEXT.equals(type)) return "";
		String string = this.text;
		if (!hasFlag(EnumFlag.DISABLE_PLACEHOLDERS) && (parent == null || !parent.hasFlag(EnumFlag.DISABLE_PLACEHOLDERS))) {
			string = PAPI.setPlaceholders(player, string);
		}
		return Common.colorize(string);
	}

	@Override
	public void show(Player... players) {
		if (players.length == 0) players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
		NMSAdapter nmsAdapter = PLUGIN.getNMSAdapter();
		for (Player player : players) {
			if (!isVisible(player) && canShow(player) && (parent == null || parent.isInDisplayRange(player))) {
				switch (type) {
					case TEXT: case HEAD: case SMALLHEAD:
						nmsAdapter.showFakeArmorStand(player, getLocation(), entityId, !HologramLineType.HEAD.equals(type));
						break;
					case ICON:
						nmsAdapter.showFakeArmorStand(player, getLocation(), entityId, true);
						nmsAdapter.showFakeItem(player, getLocation(), entityId + 1, HologramItem.parseItemStack(getItem().getContent(), player));
						nmsAdapter.attachFakeEnity(player, entityId, entityId + 1);
						break;
					case ENTITY:
						int entityTypeId = PLUGIN.getNMSAdapter().getEntityTypeId(new HologramEntity(PAPI.setPlaceholders(player, getEntity().getContent())).getType());
						if (entityTypeId == -1) return;
						nmsAdapter.showFakeArmorStand(player, getLocation(), entityId, true);
						if (entity.getType().isAlive()) {
							nmsAdapter.showFakeEntityLiving(player, getLocation(), entityId + 1, entityTypeId);
						} else {
							nmsAdapter.showFakeEntity(player, getLocation(), entityId + 1, entityTypeId);
						}
						nmsAdapter.attachFakeEnity(player, entityId, entityId + 1);
						break;
					default: break;
				}
				viewers.add(player.getUniqueId());

				if (HologramLineType.TEXT.equals(type)) {
					nmsAdapter.updateFakeEntityName(player, entityId, getText(player));
				} else if (HologramLineType.HEAD.equals(type) || HologramLineType.SMALLHEAD.equals(type)) {
					ItemStack itemStack = PAPI.containsPlaceholders(getItem().getContent()) ? HologramItem.parseItemStack(getItem().getContent(), player) : getItem().parse();
					nmsAdapter.helmetFakeEntity(player, entityId, itemStack);
				}
			}
		}
	}

	@Override
	public void update(Player... players) {
		if (players.length == 0) players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
		NMSAdapter nmsAdapter = PLUGIN.getNMSAdapter();
		for (Player player : players) {
			if (parent != null && !parent.isInUpdateRange(player)) continue;
			if (!isVisible(player)) {
				show(player);
				continue;
			}

			if (HologramLineType.TEXT.equals(type)) {
				nmsAdapter.updateFakeEntityName(player, entityId, getText(player));
			} else if (HologramLineType.HEAD.equals(type) || HologramLineType.SMALLHEAD.equals(type)) {
				nmsAdapter.helmetFakeEntity(player, entityId, HologramItem.parseItemStack(getItem().getContent(), player));
			}
		}
	}

	@Override
	public void hide(Player... players) {
		if (players.length == 0) players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
		for (Player player : players) {
			if (isVisible(player)) {
				PLUGIN.getNMSAdapter().hideFakeEntity(player, entityId);
				viewers.remove(player.getUniqueId());
			}
		}
	}

	@Override
	public void updateLocation(Player... players) {
		if (players.length == 0) players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
		for (Player player : players) {
			if (parent != null && !parent.isInUpdateRange(player)) continue;
			if (!isVisible(player)) {
				show(player);
				continue;
			}

			if (!HologramLineType.ENTITY.equals(type)) {
				PLUGIN.getNMSAdapter().teleportFakeEntity(player, getLocation(), entityId);
			} else {
				this.hide();
				this.show();
			}
		}
	}

	@Override
	public boolean isVisible(Player player) {
		return viewers.contains(player.getUniqueId());
	}

	@Override
	public boolean canShow(Player player) {
		return permission == null || player.hasPermission(permission);
	}

	/*
	 *	Flags Methods
	 */

	@Override
	public void addFlags(EnumFlag... flags) {
		this.flags.addAll(Arrays.asList(flags));
	}

	@Override
	public void removeFlags(EnumFlag... flags) {
		for (EnumFlag flag : flags) {
			this.flags.remove(flag);
		}
	}

	@Override
	public boolean hasFlag(EnumFlag flag) {
		return flags.contains(flag);
	}

}
