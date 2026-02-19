package eu.decentsoftware.holograms.plugin.convertors.impl;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import eu.decentsoftware.holograms.plugin.convertors.ConverterCommon;
import eu.decentsoftware.holograms.plugin.convertors.ConvertorResult;
import org.bukkit.Location;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GHoloConverter implements IConvertor {
    
    private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
    private static final Pattern GRADIENT = Pattern.compile("\\[(#[0-9a-f]{6}) (\\w+) (#[0-9a-f]{6})]", Pattern.CASE_INSENSITIVE);
    
    @Override
    public ConvertorResult convert(){
        return convert(new File(PLUGIN.getDataFolder().getParent() + "/GHolo/data/", "h.data"));
    }
    
    @Override
    public ConvertorResult convert(File file) {
        Log.info("Converting GHolo holograms...");
        if (ConverterCommon.notValidFile(file, "h.data")) {
            Log.warn("Invalid file! Need 'h.data'");
            return ConvertorResult.createFailed();
        }

        FileConfig config = new FileConfig(PLUGIN.getPlugin(), file);
        ConvertorResult convertorResult = new ConvertorResult();
        for (String name : config.getConfigurationSection("H").getKeys(false)) {
            String path = "H." + name;
            
            Location location = LocationUtils.asLocation(config.getString(path + ".l"));
            if(location == null){
                Log.warn("Cannot convert '%s'! Invalid location.", name);
                convertorResult.addFailed();
                continue;
            }
            
            List<String> lines = prepareLines(config.getStringList(path + ".c"));
            ConverterCommon.createHologram(convertorResult, name, location, lines, PLUGIN);
        }
        return convertorResult;
    }
    
    @Override
    public List<String> prepareLines(List<String> lines){
        return lines.stream().map(line -> {
            line = line.replace("[x]", "\u2588");
            line = line.replace("[X]", "\u2588");
            line = line.replace("[|]", "\u23B9");
            if (line.toUpperCase(Locale.ROOT).startsWith("ICON:") || line.toUpperCase(Locale.ROOT).startsWith("ENTITY:")) {
                return "#" + line;
            }
            Matcher matcher = GRADIENT.matcher(line);
            if (matcher.find()) {
                StringBuffer temp = new StringBuffer();
                do {
                    matcher.appendReplacement(temp, "<" + matcher.group(1) + ">" + matcher.group(2) + "</" + matcher.group(3) + ">");
                } while (matcher.find());
                matcher.appendTail(temp);
                return temp.toString();
            }
            return line;
        }).collect(Collectors.toList());
    }
}
