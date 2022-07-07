package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.Configuration;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FutureHologramsConverter implements IConvertor {
    
    private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
    
    @Override
    public ConvertorResult convert(){
        return convert(new File("plugins/FutureHolograms/holograms.yml"));
    }
    
    @Override
    public ConvertorResult convert(File file){
        Common.log("Converting FutureHolograms holograms...");
        if(!ConverterCommon.isValidFile(file, "holograms.yml")){
            Common.log("Invalid file! Need 'holograms.yml");
            return ConvertorResult.createFailed();
        }
        
        Configuration config = new Configuration(PLUGIN.getPlugin(), file);
        ConvertorResult convertorResult = new ConvertorResult();
        for (String name : config.getKeys(false)) {
            Location loc = LocationUtils.asLocation(config.getString(name + ".location").replace(",", ":"));
            if (loc == null) {
                Common.log("Skipping auto-generated next/prev page hologram '%s'...", name);
                convertorResult.addFailed();
                continue;
            }
            List<List<String>> pages = new ArrayList<>();
            for (String page : config.getConfigurationSection(name).getKeys(false)) {
                if (isNotHologram(page)) {
                    continue;
                }
                
                ConfigurationSection section = config.getConfigurationSection(name + "." + page);
                List<String> lines = section.getStringList("lines");
                if (lines == null || lines.isEmpty()) {
                    continue;
                }
                
                pages.add(lines);
            }
            
            ConverterCommon.createHologramPages(convertorResult, name, loc, pages, PLUGIN);
        }
        
        return convertorResult;
    }
    
    @Override
    public List<String> prepareLines(List<String> lines){
        return null;
    }
    
    private boolean isNotHologram(String name) {
        return name.equals("default") ||
               name.equals("refresh") ||
               name.equals("cooldown") ||
               name.equals("refreshRate") ||
               name.equals("location");
    }
}
