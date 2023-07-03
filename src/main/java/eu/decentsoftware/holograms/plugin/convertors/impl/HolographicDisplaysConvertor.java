package eu.decentsoftware.holograms.plugin.convertors.impl;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import eu.decentsoftware.holograms.plugin.convertors.ConverterCommon;
import eu.decentsoftware.holograms.plugin.convertors.ConvertorResult;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HolographicDisplaysConvertor implements IConvertor {

	private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
	private static final Pattern PAPI_PATTERN = Pattern.compile("\\{papi: (.+)}");

	@Override
	public ConvertorResult convert() {
		return convert(new File(PLUGIN.getDataFolder().getParent() + "/HolographicDisplays/", "database.yml"));
	}

	@Override
	public ConvertorResult convert(final File file) {
		Common.log("Converting HolographicDisplays holograms...");
		if (ConverterCommon.notValidFile(file, "database.yml")) {
			Common.log("Invalid file! Need 'database.yml'");
			return ConvertorResult.createFailed();
		}

		FileConfig config = new FileConfig(PLUGIN.getPlugin(), file);
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
	public List<String> prepareLines(List<String> lines) {
		List<String> parsed = new ArrayList<>(lines.size());
		// Go through each line and convert any {papi: <placeholder>} pattern to %<placeholder>%
		for(String line : lines) {
			String parsedLine = line;
			Matcher matcher = PAPI_PATTERN.matcher(line);
			if(matcher.find()) {
				StringBuffer buffer = new StringBuffer();
				do {
					matcher.appendReplacement(buffer, "%" + matcher.group(1) + "%");
				} while(matcher.find());
				
				matcher.appendTail(buffer);
				parsedLine = buffer.toString();
			}
			
			parsed.add(parsedLine);
		}
		
		return parsed.stream().map(line -> {
			if (line.toUpperCase().startsWith("ICON:")) {
				return "#" + line;
			}
			if (line.trim().equalsIgnoreCase("{empty}")) {
				return "";
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
