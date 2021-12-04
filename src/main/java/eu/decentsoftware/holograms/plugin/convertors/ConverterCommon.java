package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.HologramLine;
import eu.decentsoftware.holograms.api.holograms.HologramPage;
import eu.decentsoftware.holograms.api.utils.Common;
import org.bukkit.Location;

import java.util.List;
import java.util.logging.Level;

public class ConverterCommon {
    
    public static int createHologram(int count, String name, Location location, List<String> lines, DecentHolograms plugin){
        if (plugin.getHologramManager().containsHologram(name)) {
            Common.log(Level.WARNING, "A hologram with name '%s' already exists, skipping...", name);
            return count;
        }
        Hologram hologram = new Hologram(name, location);
        HologramPage page = hologram.getPage(0);
        plugin.getHologramManager().registerHologram(hologram);
        lines.forEach((line) -> page.addLine(new HologramLine(page, page.getNextLineLocation(), line)));
        hologram.save();
        count++;
        return count;
    }
}
