package eu.decentsoftware.holograms.core.managers;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.managers.HologramManager;
import eu.decentsoftware.holograms.core.holograms.DefaultHologram;
import eu.decentsoftware.holograms.core.holograms.DefaultHologramLine;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class DefaultHologramManager implements HologramManager {

	private final Map<String, Hologram> hologramMap = Maps.newLinkedHashMap();
	private final Set<HologramLine> temporaryLines = Sets.newHashSet();

	public DefaultHologramManager() {
		this.reload();
	}

	@Override
	public HologramLine spawnTemporaryHologramLine(Location location, String content, long duration) {
		HologramLine line = new DefaultHologramLine(location, content);
		temporaryLines.add(line);
		line.show();
		Bukkit.getScheduler().runTaskLaterAsynchronously(DecentHologramsProvider.getDecentHolograms().getPlugin(), () -> {
			line.destroy();
			temporaryLines.remove(line);
		}, duration);
		return line;
	}

	@Override
	public void reload() {
		this.destroy();
		this.loadHolograms();
	}

	@Override
	public void destroy() {
		if (!hologramMap.isEmpty()) {
			hologramMap.values().forEach(Hologram::destroy);
			hologramMap.clear();
		}
		if (!temporaryLines.isEmpty()) {
			temporaryLines.forEach(HologramLine::destroy);
			temporaryLines.clear();
		}
	}

	@Override
	public void showAll(Player player) {
		for (Hologram hologram : getHolograms()) {
			if (hologram.isEnabled()) {
				hologram.show(player);
			}
		}
	}

	@Override
	public void hideAll(Player player) {
		for (Hologram hologram : getHolograms()) {
			hologram.hide(player);
		}
		for (HologramLine hologramLine : temporaryLines) {
			hologramLine.hide(player);
		}
	}

	@Override
	public boolean containsHologram(String name) {
		return hologramMap.containsKey(name);
	}

	@Override
	public Hologram registerHologram(Hologram hologram) {
		return hologramMap.put(hologram.getName(), hologram);
	}

	@Override
	public Hologram getHologram(String name) {
		return hologramMap.get(name);
	}

	@Override
	public Hologram removeHologram(String name) {
		Hologram hologram = hologramMap.remove(name);
		hologram.delete();
		return hologram;
	}

	@Override
	public Set<String> getHologramNames() {
		return hologramMap.keySet();
	}

	@Override
	public Collection<Hologram> getHolograms() {
		return hologramMap.values();
	}

	private void loadHolograms() {
		hologramMap.clear();
		Common.log("Loading holograms...");

		final File dir = new File(DecentHologramsProvider.getDecentHolograms().getPlugin().getDataFolder() + "/holograms");
		int counter = 0;

		if (dir.exists() && dir.isDirectory()) {
			final String[] fileNames = dir.list((dir1, name) -> name.matches("hologram_\\S+\\.yml"));
			for (String fileName : fileNames) {
				Hologram hologram = DefaultHologram.fromFile(fileName);
				if (hologram != null && hologram.isEnabled()) {
					hologram.show();
					hologram.realignLines();
					registerHologram(hologram);
					counter++;
				}
			}
		} else {
			dir.mkdir();
		}
		Common.log("Loaded %d holograms!", counter);
	}

}
