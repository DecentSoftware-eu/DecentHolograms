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
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FutureHologramsConverter implements IConvertor {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();

    @Override
    public ConvertorResult convert() {
        return convert(new File(PLUGIN.getDataFolder().getParent() + "/FutureHolograms/", "holograms.yml"));
    }

    @Override
    public ConvertorResult convert(File file) {
        Log.info("正在转换 FutureHolograms 悬浮字...");
        if (ConverterCommon.notValidFile(file, "holograms.yml")) {
            Log.warn("无效文件！需要 'holograms.yml'");
            return ConvertorResult.createFailed();
        }

        FileConfig config = new FileConfig(PLUGIN.getPlugin(), file);
        ConvertorResult convertorResult = new ConvertorResult();
        for (String name : config.getKeys(false)) {
            Location loc = LocationUtils.asLocation(config.getString(name + ".location").replace(",", ":"));
            if (loc == null) {
                Log.info("跳过自动生成的下一页/上一页悬浮字 '%s'...", name);
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
    public List<String> prepareLines(List<String> lines) {
        return null;
    }

    private boolean isNotHologram(String name) {
        return name.equals("default")
               || name.equals("refresh")
               || name.equals("cooldown")
               || name.equals("refreshRate")
               || name.equals("location");
    }
}
