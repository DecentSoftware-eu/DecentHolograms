package eu.decentsoftware.holograms.core.holograms;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.objects.enums.EnumFlag;
import eu.decentsoftware.holograms.api.objects.enums.EnumOrigin;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.config.Configuration;
import eu.decentsoftware.holograms.utils.scheduler.ConsumerTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Getter
@Setter
public class DefaultHologram implements Hologram {

	private static final DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();

	public static Hologram fromFile(final String fileName) {
		Configuration config = new Configuration(PLUGIN.getPlugin(), "holograms/" + fileName);
		Location location = config.getLocation("location");
		String name = fileName.substring(0, fileName.length() - 4);
		if (name.toLowerCase().startsWith("hologram_") && name.length() > "hologram_".length()) {
			name = name.substring("hologram_".length());
		}
		if (location == null) {
			Common.log(Level.SEVERE, "Wrong location format in '%s'!", fileName);
			return null;
		}

		boolean enabled = true;
		if (config.isBoolean("enabled")) {
			enabled = config.getBoolean("enabled");
		}
		Hologram hologram = new DefaultHologram(name, location, config, enabled);
		if (config.isString("permission")) {
			hologram.setPermission(config.getString("permission"));
		}
		hologram.setDisplayRange(config.getInt("display-range", Settings.DEFAULT_DISPLAY_RANGE.getValue()));
		hologram.setUpdateRange(config.getInt("update-range", Settings.DEFAULT_UPDATE_RANGE.getValue()));
		hologram.setUpdateInterval(config.getInt("update-interval", Settings.DEFAULT_UPDATE_INTERVAL.getValue()));
		hologram.addFlags(config.getStringList("flags").stream().map(EnumFlag::valueOf).toArray(EnumFlag[]::new));
		hologram.setFacing((float) config.getDouble("facing", 0.0f));
		if (config.isString("origin")) {
			String originName = config.getString("origin", Settings.DEFAULT_ORIGIN.getValue());
			EnumOrigin origin = Arrays.stream(EnumOrigin.values()).filter(value -> value.name().equalsIgnoreCase(originName)).findFirst().orElse(EnumOrigin.UP);
			hologram.setOrigin(origin);
		}

		List<String> keys = Lists.newArrayList(config.getSectionKeys("lines"));
		Collections.sort(keys);
		for (int i = 1; i < keys.size() + 1; i++) {
			String path = "lines." + i;
			if (!config.contains(path)) {
				config.createSection(path);
			}
			HologramLine line = DefaultHologramLine.fromFile(config.getConfigurationSection(path), hologram.getNextLineLocation());
			line.setParent(hologram);
			hologram.addLine(line);
 		}
		return hologram;
	}

	/*
	 *	Variables
	 */

	private final String name;
	private final Configuration config;
	private final ConsumerTask<Hologram> updateTask;
	private final List<HologramLine> lines;
	private final Set<UUID> viewers;
	private final Set<EnumFlag> flags;
	private boolean enabled;
	private boolean saveToFile;
	private EnumOrigin origin;
	private String permission;
	private Location location;
	private float facing;
	private int displayRange;
	private int updateRange;
	private int updateInterval;

	/*
	 *	Constructors
	 */

	public DefaultHologram(String name, Location location) {
		this(name, location, true);
	}

	public DefaultHologram(String name, Location location, boolean saveToFile) {
		this(name, location, saveToFile ? new Configuration(PLUGIN.getPlugin(), String.format("holograms/hologram_%s.yml", name)) : null);
	}

	public DefaultHologram(String name, Location location, Configuration config) {
		this(name, location, config, true);
	}

	public DefaultHologram(String name, Location location, Configuration config, boolean enabled) {
		this.name = name;
		this.location = location;
		this.location.setPitch(0.0f);
		this.config = config;
		this.enabled = enabled;
		this.saveToFile = this.config != null;
		this.lines = Lists.newArrayList();
		this.viewers = Sets.newHashSet();
		this.flags = Sets.newHashSet();
		this.permission = null;
		this.origin = EnumOrigin.valueOf(Settings.DEFAULT_ORIGIN.getValue());
		this.displayRange = Settings.DEFAULT_DISPLAY_RANGE.getValue();
		this.updateRange = Settings.DEFAULT_UPDATE_RANGE.getValue();
		this.updateInterval = Settings.DEFAULT_UPDATE_INTERVAL.getValue();
		this.updateTask = new ConsumerTask<>(PLUGIN.getPlugin(), this, 0, updateInterval);
		this.updateTask.addPart("update", (hologram) -> {
			for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if (hologram.isVisible(onlinePlayer) && hologram.isInUpdateRange(onlinePlayer)) {
					hologram.update(onlinePlayer);
				}
			}
		});
		this.startUpdate();
	}

	/*
	 *	General Methods
	 */

	@Override
	public String toString() {
		return "DefaultHologram{" +
				"name=" + name +
				", enabled=" + enabled +
				"} " + super.toString();
	}

	@Override
	public Hologram clone(String name, Location location, boolean temp) {
		Hologram hologram = new DefaultHologram(name, location, !temp);
		hologram.setOrigin(this.getOrigin());
		hologram.setPermission(this.getPermission());
		hologram.setFacing(this.getFacing());
		hologram.setDisplayRange(this.getDisplayRange());
		hologram.setUpdateRange(this.getUpdateRange());
		hologram.setUpdateInterval(this.getUpdateInterval());
		hologram.addFlags(this.getFlags().toArray(new EnumFlag[0]));
		for (HologramLine line : this.getLines()) {
			HologramLine cloneLine = line.clone(hologram.getNextLineLocation());
			hologram.addLine(cloneLine);
		}
		return hologram;
	}

	@Override
	public void destroy() {
		this.stopUpdate();
		this.hide();
	}

	@Override
	public void delete() {
		this.destroy();
		if (config != null) {
			config.delete();
		}
	}

	@Override
	public void save() {
		if (!saveToFile) return;
		config.setLocation("location", location, false);
		config.set("enabled", enabled);
		config.set("permission", permission == null || permission.isEmpty() ? null : permission);
		config.set("flags", flags.isEmpty() ? null : flags.stream().map(EnumFlag::name).collect(Collectors.toList()));
		config.set("display-range", displayRange);
		config.set("update-range", updateRange);
		config.set("update-interval", updateInterval);
		config.set("facing", facing);
		config.set("origin", origin == null ? Settings.DEFAULT_ORIGIN.getValue() : origin.name());
		config.set("lines", null);
		for (int i = 1; i <= lines.size(); i++) {
			HologramLine line = lines.get(i - 1);
			String path = "lines." + i;
			if (!config.contains(path)) {
				config.createSection(path);
			}
			line.save(config.getConfigurationSection(path));
		}
		config.saveData();
		config.reload();
	}

	@Override
	public void enable() {
		this.enabled = true;
		this.show();
		this.startUpdate();
		this.save();
	}

	@Override
	public void disable() {
		this.enabled = false;
		this.stopUpdate();
		this.hide();
		this.save();
	}

	@Override
	public int size() {
		return lines.size();
	}

	@Override
	public double getHeight() {
		double height = 0.0D;
		for (HologramLine hologramLine : lines) {
			height += hologramLine.getHeight();
		}
		return height;
	}

	@Override
	public void setFacing(float facing) {
		this.facing = facing;
		this.location.setYaw(facing);
	}

	@Override
	public void setLocation(Location location) {
		this.location = location;
		this.location.setPitch(0.0f);
	}

	@Override
	public List<Player> getViewers() {
		return viewers.stream()
				.map(Bukkit::getPlayer)
				.filter(player -> player != null && player.isOnline())
				.collect(Collectors.toList());
	}

	/*
	 *	Visibility Methods
	 */

	@Override
	public void show(Player... players) {
		if (!isEnabled()) return;
		if (players.length == 0) players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
		for (Player player : players) {
			if (!isVisible(player) && canShow(player) && isInDisplayRange(player)) {
				lines.forEach(line -> line.show(player));
				viewers.add(player.getUniqueId());
			}
		}
	}

	@Override
	public void update(Player... players) {
		if (!isEnabled()) return;
		if (players.length == 0) players = getViewers().toArray(new Player[0]);
		for (Player player : players) {
			if (!isInUpdateRange(player)) continue;
			if (!isVisible(player)) {
				show(player);
				continue;
			}
			lines.forEach(line -> line.update(player));
		}
	}

	@Override
	public void hide(Player... players) {
		if (players.length == 0) players = getViewers().toArray(new Player[0]);
		for (Player player : players) {
			if (isVisible(player)) {
				lines.forEach(line -> line.hide(player));
				viewers.remove(player.getUniqueId());
			}
		}
	}

	@Override
	public boolean isVisible(Player player) {
		return player != null && viewers.contains(player.getUniqueId());
	}

	@Override
	public boolean canShow(Player player) {
		return player != null && (permission == null || player.hasPermission(permission));
	}

	@Override
	public boolean isInDisplayRange(Player player) {
		return player != null && player.getWorld().getName().equals(location.getWorld().getName()) && player.getLocation().distanceSquared(location) < (displayRange * displayRange);
	}

	@Override
	public boolean isInUpdateRange(Player player) {
		return isInDisplayRange(player) && player.getLocation().distanceSquared(location) < (updateRange * updateRange);
	}

	/*
	 *	Lines Methods
	 */

	@Override
	public void realignLines() {
		Location currentLocation = getLocation().clone();
		if (EnumOrigin.DOWN.equals(origin)) {
			currentLocation.add(0, getHeight(), 0);
		}

		for (HologramLine line : lines) {
			line.setLocation(currentLocation.clone().add(line.getOffsetX(), line.getOffsetY(), line.getOffsetZ()));
			line.updateLocation();
			currentLocation.subtract(0, line.getHeight(), 0);
		}
	}

	@Override
	public boolean addLine(HologramLine line) {
		line.setParent(this);
		lines.add(line);
		realignLines();
		return true;
	}

	@Override
	public boolean insertLine(int index, HologramLine line) {
		line.setParent(this);
		lines.add(index, line);
		realignLines();
		return true;
	}

	@Override
	public boolean swapLines(int index1, int index2) {
		if (index1 < 0 || index1 >= size() || index2 < 0 || index2 >= size()) {
			return false;
		}
		Collections.swap(lines, index1, index2);
		realignLines();
		return true;
	}

	@Override
	public boolean setLine(int index, String content) {
		if (index < 0 || index >= size()) {
			return false;
		}
		HologramLine line = getLine(index);
		line.hide();
		line.setContent(content);
		line.show();
		realignLines();
		return true;
	}

	@Override
	public HologramLine getLine(int index) {
		return lines.get(index);
	}

	@Override
	public HologramLine removeLine(int index) {
		HologramLine line = lines.remove(index);
		if (line != null) {
			line.destroy();
			realignLines();
		}
		return line;
	}

	@Override
	public List<HologramLine> getLines() {
		return lines;
	}

	@Override
	public Location getNextLineLocation() {
		if (size() == 0) {
			return getLocation().clone();
		}
		HologramLine line = lines.get(lines.size() - 1);
		return line.getLocation().clone().subtract(0, line.getHeight(), 0);
	}

	/*
	 *	Updating Methods
	 */

	@Override
	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;

		this.updateTask.setInterval(this.updateInterval);
		this.stopUpdate();
		this.startUpdate();
	}

	@Override
	public void startUpdate() {
		if (!this.isEnabled() || this.hasFlag(EnumFlag.DISABLE_UPDATING)) return;
		updateTask.restart();
	}

	@Override
	public void stopUpdate() {
		updateTask.stop();
	}

	/*
	 *	Flags Methods
	 */

	@Override
	public void addFlags(EnumFlag... flags) {
		this.flags.addAll(Arrays.asList(flags));
		if (this.hasFlag(EnumFlag.DISABLE_UPDATING)) {
			this.stopUpdate();
		}
	}

	@Override
	public void removeFlags(EnumFlag... flags) {
		for (EnumFlag flag : flags) {
			this.flags.remove(flag);
		}
		if (!this.hasFlag(EnumFlag.DISABLE_UPDATING)) {
			this.startUpdate();
		}
	}

	@Override
	public boolean hasFlag(EnumFlag flag) {
		return flags.contains(flag);
	}

}