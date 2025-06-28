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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CMIConverter implements IConvertor {

    private static final DecentHolograms PLUGIN = DecentHologramsAPI.get();

    @Override
    public ConvertorResult convert() {
        File file = new File(PLUGIN.getDataFolder().getParent() + "/CMI/Saves/", "holograms.yml");
        if (ConverterCommon.notValidFile(file, "holograms.yml")) {
            // Probably old location...
            file = new File(PLUGIN.getDataFolder().getParent() + "/CMI/", "holograms.yml");
        }

        return convert(file);
    }

    @Override
    public ConvertorResult convert(File file) {
        Log.info("正在转换 CMI 悬浮字...");
        if (ConverterCommon.notValidFile(file, "holograms.yml")) {
            Log.warn("无效文件！需要 'holograms.yml'");
            return ConvertorResult.createFailed();
        }

        FileConfig config = new FileConfig(PLUGIN.getPlugin(), file);
        ConvertorResult convertorResult = new ConvertorResult();
        for (String name : config.getKeys(false)) {
            // Skip Auto-generated holograms to change pages.
            if (name.endsWith("#>") || name.endsWith("#<")) {
                Log.info("跳过自动生成的下一页/上一页悬浮字 '%s'...", name);
                convertorResult.addSkipped();
                continue;
            }

            Location loc = LocationUtils.asLocation(config.getString(name + ".Loc").replace(";", ":"));
            if (loc == null) {
                Log.warn("无法转换 '%s'！位置无效。", name);
                convertorResult.addFailed();
                continue;
            }

            List<List<String>> pages = createPages(config.getStringList(name + ".Lines"));

            ConverterCommon.createHologramPages(convertorResult, name, loc, pages, PLUGIN);
        }
        return convertorResult;
    }

    @Override
    public List<String> prepareLines(List<String> lines) {
        return lines.stream().map(line -> {
            if (line.toUpperCase(Locale.ROOT).startsWith("ICON:")) {
                return "#" + line;
            }
            return line;
        }).collect(Collectors.toList());
    }

    private List<List<String>> createPages(List<String> lines) {
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
