package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.Configuration;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class HolographicDisplaysConvertor implements IConvertor {

	private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();

	@Override
	public boolean convert() {
		return convert(new File("plugins/HolographicDisplays/database.yml"));
	}

	@Override
	public boolean convert(final File file) {
		Common.log("Converting HolographicDisplays holograms...");
		if (!this.isFileValid(file)) {
			Common.log("Invalid file! Need 'database.yml'");
			return false;
		}
//		Common.log(file.getAbsolutePath());
		int count = 0;
		Configuration config = new Configuration(PLUGIN.getPlugin(), file);
		for (String name : config.getKeys(false)) {
			Location location = parseLocation(config, name);
			if(location == null){
				Common.log(Level.WARNING, "Location for '%s' was null. Skipping...", name);
				continue;
			}
			
			List<String> lines = prepareLines(config.getStringList(name + ".lines"));
			
			count = ConverterCommon.createHologram(count, name, location, lines, PLUGIN);
		}
		Common.log("Successfully converted %d HolographicDisplays holograms!", count);
		return true;
	}

	@Override
	public boolean convert(final File... files) {
		for (final File file : files) {
			this.convert(file);
		}
		return true;
	}
	
	@Override
	public List<String> prepareLines(List<String> lines){
		return lines.stream().map(line -> {
			line = line.replace("[x]", "\u2588");
			line = line.replace("[X]", "\u2588");
			line = line.replace("[/]", "\u258C");
			line = line.replace("[.]", "\u2591");
			line = line.replace("[..]", "\u2592");
			line = line.replace("[...]", "\u2593");
			line = line.replace("[p]", "\u2022");
			line = line.replace("[P]", "\u2022");
			line = line.replace("[|]", "\u23B9");
			if (line.toUpperCase().startsWith("ICON: ")) {
				return "#" + line;
			}
			return line;
		}).collect(Collectors.toList());
	}

	private boolean isFileValid(final File file) {
		return file != null && file.exists() && !file.isDirectory() && file.getName().equals("database.yml");
	}

	private Location parseLocation(YamlConfiguration config, String name) {
		String locationString = config.getString(name + ".location");
		if (locationString != null && !locationString.trim().isEmpty()) {
			return LocationUtils.asLocation(locationString.replace(", ", ":"));
		}
		
		// HolographicDisplays v3.0.0+ has a new file format.
		String world = config.getString(name + ".position.world");
		double x = config.getDouble(name + ".position.x");
		double y = config.getDouble(name + ".position.y");
		double z = config.getDouble(name + ".position.z");
		
		// World couldn't be retrieved. Return null
		if (world == null) {
			return null;
		}
		
		return LocationUtils.asLocation(String.format("%s:%f:%f:%f", world, x, y, z));
	}

}
