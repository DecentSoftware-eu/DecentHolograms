package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.Configuration;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import org.bukkit.Location;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GHoloConverter implements IConvertor {
    
    private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
    private static final Pattern GRADIENT = Pattern.compile("\\[(#[0-9a-f]{6}) (\\w+) (#[0-9a-f]{6})]", Pattern.CASE_INSENSITIVE);
    
    @Override
    public ConvertorRest convert(){
        return convert(new File("plugins/GHolo/data/h.data"));
    }
    
    @Override
    public ConvertorRest convert(File file) {
        Common.log("Converting GHolo holograms...");
        if (!this.isFileValid(file)) {
            Common.log("Invalid file! Need 'h.data'");
            return new ConvertorRest(false, 0);
        }
        int count = 0;
        Configuration config = new Configuration(PLUGIN.getPlugin(), file);
        for (String name : config.getConfigurationSection("H").getKeys(false)) {
            String path = "H." + name;
            
            Location location = LocationUtils.asLocation(config.getString(path + ".l"));
            if(location == null){
                Common.log(Level.WARNING, "Skipping hologram '%s' with null location...", name);
                continue;
            }
            
            List<String> lines = prepareLines(config.getStringList(path + ".c"));
            
            count = ConverterCommon.createHologram(count, name, location, lines, PLUGIN);
        }
        Common.log("Successfully converted %d GHolo holograms!", count);
        return new ConvertorRest(true, count);
    }
    
    @Override
    public ConvertorRest convert(File... files) {
        int summary = 0;
        for (File file : files) {
            summary += this.convert(file).getCount();
        }
        return new ConvertorRest(true, summary);
    }
    
    private boolean isFileValid(final File file) {
        return file != null && file.exists() && !file.isDirectory() && file.getName().equals("h.data");
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
