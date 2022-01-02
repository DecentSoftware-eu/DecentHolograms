package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.Configuration;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CMIConverter implements IConvertor {
    
    private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
    
    @Override
    public boolean convert() {
        return convert(new File("plugins/CMI/holograms.yml"));
    }
    
    @Override
    public boolean convert(File file) {
        Common.log("Converting CMI holograms...");
        if(!ConverterCommon.isValidFile(file, "holograms.yml")){
            Common.log("Invalid file! Need 'holograms.yml'");
            return false;
        }
        int count = 0;
        Configuration config = new Configuration(PLUGIN.getPlugin(), file);
        for(String name : config.getKeys(false)) {
            // Auto-generated holograms to change pages.
            if(name.endsWith("#>") || name.endsWith("#<")) {
                Common.log("Skipping auto-generated next/prev page holograms...");
                continue;
            }
            
            Location loc = LocationUtils.asLocation(config.getString(name + ".Loc").replace(";", ":"));
            List<List<String>> pages = createPages(config.getStringList(name + ".Lines"));
            
            count = ConverterCommon.createHologramPages(count, name, loc, pages, PLUGIN);
        }
        Common.log("Successfully converted %d CMI Holograms!", count);
        return true;
    }
    
    @Override
    public boolean convert(File... files) {
        for(final File file : files) {
            this.convert(file);
        }
        return true;
    }
    
    @Override
    public List<String> prepareLines(List<String> lines){
        return lines.stream().map(line -> {
            if (line.toUpperCase(Locale.ROOT).startsWith("ICON:")) {
                return "#" + line;
            }
            return line;
        }).collect(Collectors.toList());
    }
    
    private List<List<String>> createPages(List<String> lines){
        List<String> temp = new ArrayList<>();
        List<List<String>> pages = new ArrayList<>();
        
        for (String line : lines) {
            if (line.toLowerCase(Locale.ROOT).equals("!nextpage!")) {
                pages.add(temp);
                temp.clear();
                continue;
            }
            
            temp.add(line);
        }
        
        if (!temp.isEmpty()) {
            pages.add(temp);
        }
        
        return pages.stream().map(this::prepareLines).collect(Collectors.toList());
    }
}
