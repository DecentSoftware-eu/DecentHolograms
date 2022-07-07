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
	public ConvertorResult convert() {
		return convert(new File("plugins/HolographicDisplays/database.yml"));
	}

	@Override
	public ConvertorResult convert(final File file) {
		Common.log("Converting HolographicDisplays holograms...");
		if (!ConverterCommon.isValidFile(file, "database.yml")) {
			Common.log("Invalid file! Need 'database.yml'");
			return ConvertorResult.createFailed();
		}
		
		Configuration config = new Configuration(PLUGIN.getPlugin(), file);
		ConvertorResult convertorResult = new ConvertorResult();
		for (String name : config.getKeys(false)) {
			Location location = parseLocation(config, name);
			if(location == null){
				Common.log(Level.WARNING, "Cannot convert '%s'! Invalid location.", name);
				convertorResult.addFailed();
				continue;
			}
			
			List<String> lines = prepareLines(config.getStringList(name + ".lines"));
			ConverterCommon.createHologram(convertorResult, name, location, lines, PLUGIN);
		}
		return convertorResult;
	}
	
	@Override
	public List<String> prepareLines(List<String> lines){
		return lines.stream().map(line -> {
			if (line.toUpperCase().startsWith("ICON: ")) {
				return "#" + line;
			}
			return line;
		}).collect(Collectors.toList());
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
