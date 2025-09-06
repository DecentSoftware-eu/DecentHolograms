/*
 * This file is part of DecentHolograms, licensed under the GNU GPL v3.0 License.
 * Copyright (C) DecentSoftware.eu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package eu.decentsoftware.holograms.display.config;

import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.file.FileUtils;
import eu.decentsoftware.holograms.api.utils.items.DecentMaterial;
import eu.decentsoftware.holograms.api.utils.items.HologramItem;
import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DecentLocation;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.display.TextDisplayPage;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.DisplayAttributeValueType;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinitionRegistry;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import eu.decentsoftware.holograms.nms.api.display.data.ItemDisplayType;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayAlignment;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * Data Access Object for loading displays from configuration files.
 *
 * @author d0by
 */
public class DisplayDao {

    private final JavaPlugin plugin;
    private final AttributeDefinitionRegistry attributeDefinitionRegistry;

    public DisplayDao(JavaPlugin plugin, AttributeDefinitionRegistry attributeDefinitionRegistry) {
        this.plugin = plugin;
        this.attributeDefinitionRegistry = attributeDefinitionRegistry;
    }

    /**
     * Saves a display to a file.
     *
     * @param display Display to save.
     */
    public void saveDisplay(DisplayBase display) {
        DisplayConfig config = new DisplayConfig(plugin, "displays/" + display.getName() + ".yml");
        config.setDecentLocation("location", display.getLocation());
        DisplayType type = display.getType();
        config.set("type", type.name());

        saveAttribute(config, "attributes.translation", display.getTranslationAttribute(), config::setDisplayVector3f);
        saveAttribute(config, "attributes.scale", display.getScaleAttribute(), config::setDisplayVector3f);
        saveAttribute(config, "attributes.billboard", display.getBillboardAttribute(), config::set);
        saveAttribute(config, "attributes.brightness", display.getBrightnessAttribute(), config::setDisplayBrightness);
        saveAttribute(config, "attributes.shadow-radius", display.getShadowRadiusAttribute(), config::set);
        saveAttribute(config, "attributes.shadow-strength", display.getShadowStrengthAttribute(), config::set);

        switch (type) {
            case TEXT:
                saveTextDisplay((TextDisplay) display, config);
                break;
            case ITEM:
                saveItemDisplay((ItemDisplay) display, config);
                break;
            case BLOCK:
                saveBlockDisplay((BlockDisplay) display, config);
                break;
        }

        config.saveData();
    }

    private void saveTextDisplay(TextDisplay display, DisplayConfig config) {
        List<Map<String, Object>> pages = new ArrayList<>();
        for (TextDisplayPage page : display.getPages()) {
            pages.add(Collections.singletonMap("lines", page.getLines()));
        }

        config.set("pages", pages);
        saveAttribute(config, "attributes.line-width", display.getLineWidthAttribute(), config::set);
        saveAttribute(config, "attributes.background-color", display.getBackgroundColorAttribute(), config::setDisplayColor);
        saveAttribute(config, "attributes.text-opacity", display.getTextOpacityAttribute(), config::set);
        saveAttribute(config, "attributes.text-shadow", display.getTextShadowAttribute(), config::set);
        saveAttribute(config, "attributes.see-through", display.getSeeThroughAttribute(), config::set);
        saveAttribute(config, "attributes.alignment", display.getAlignmentAttribute(), config::set);
    }

    private void saveItemDisplay(ItemDisplay display, DisplayConfig config) {
        config.set("item", display.getDisplayedItem().getContent());
        saveAttribute(config, "attributes.display-type", display.getDisplayTypeAttribute(), config::set);
        saveAttribute(config, "attributes.glow-color", display.getGlowColorAttribute(), config::setDisplayColor);
    }

    private void saveBlockDisplay(BlockDisplay display, DisplayConfig config) {
        config.set("material", display.getMaterial().name());
        saveAttribute(config, "attributes.glow-color", display.getGlowColorAttribute(), config::setDisplayColor);
    }

    private <T> void saveAttribute(DisplayConfig config, String path, DisplayAttribute<T> attribute, BiConsumer<String, T> setter) {
        if (attribute == null) {
            setter.accept(path, null);
            return;
        }
        config.set(path + ".value-type", DisplayAttributeValueType.STATIC.name());
        setter.accept(path + ".value", attribute.getValue());
    }

    /**
     * Loads all displays from the {@code DecentHolograms/displays} folder.
     *
     * @return List of all loaded displays.
     * @throws DisplayConfigException If one of the displays failed to load.
     */
    public List<DisplayBase> loadAllDisplays() {
        File displayFolder = new File(plugin.getDataFolder(), "displays");
        List<File> files = FileUtils.getFilesFromTree(displayFolder, Common.NAME_REGEX + "\\.yml", true);
        if (files.isEmpty()) {
            return Collections.emptyList();
        }
        return files.stream().map(this::loadDisplay).collect(Collectors.toList());
    }

    /**
     * Loads a display from a file.
     *
     * @param file File to load from.
     * @return Display loaded from the file.
     * @throws DisplayConfigException If the display failed to load.
     */
    public DisplayBase loadDisplay(File file) {
        String name = file.getName().substring(0, ".yml".length());
        DisplayConfig config = new DisplayConfig(plugin, file);
        DecentLocation location = config.getDecentLocation("location");
        if (location == null) {
            throw new DisplayConfigException("Invalid location");
        }
        DisplayType type = config.getEnum("type", DisplayType.class);
        if (type == null) {
            throw new DisplayConfigException("Invalid display type");
        }

        DisplayBase display = loadDisplayOfType(config, name, type, location);
        loadGeneralAttributes(config, display);
        return display;
    }

    private DisplayBase loadDisplayOfType(DisplayConfig config, String id, DisplayType type, DecentLocation location) {
        DisplayBase display;
        switch (type) {
            case TEXT:
                display = loadTextDisplay(config, id, location);
                break;
            case ITEM:
                display = loadItemDisplay(config, id, location);
                break;
            case BLOCK:
                display = loadBlockDisplay(config, id, location);
                break;
            default:
                throw new DisplayConfigException("Unknown display type: " + type.name());
        }
        return display;
    }

    private void loadGeneralAttributes(DisplayConfig config, DisplayBase display) {
        DisplayAttribute<DisplayVector3f> translationAttribute = loadDisplayAttribute(config, "translation");
        DisplayAttribute<DisplayVector3f> scaleAttribute = loadDisplayAttribute(config, "scale");
        DisplayAttribute<DisplayBillboardConstraints> billboardAttribute = loadDisplayAttribute(config, "billboard");
        DisplayAttribute<DisplayBrightness> brightnessAttribute = loadDisplayAttribute(config, "brightness");
        DisplayAttribute<Float> shadowRadiusAttribute = loadDisplayAttribute(config, "shadow-radius");
        DisplayAttribute<Float> shadowStrengthAttribute = loadDisplayAttribute(config, "shadow-strength");

        display.setBrightnessAttribute(brightnessAttribute);
        display.setShadowRadiusAttribute(shadowRadiusAttribute);
        display.setShadowStrengthAttribute(shadowStrengthAttribute);
        display.setTranslationAttribute(translationAttribute);
        display.setScaleAttribute(scaleAttribute);
        display.setBillboardAttribute(billboardAttribute);
    }

    private TextDisplay loadTextDisplay(DisplayConfig config, String id, DecentLocation location) {
        List<List<String>> pageLines = getPageLines(config);
        DisplayAttribute<Integer> lineWidthAttribute = loadDisplayAttribute(config, "line-width");
        DisplayAttribute<DisplayColor> backgroundColorAttribute = loadDisplayAttribute(config, "background-color");
        DisplayAttribute<Byte> textOpacityAttribute = loadDisplayAttribute(config, "text-opacity");
        DisplayAttribute<Boolean> textShadowAttribute = loadDisplayAttribute(config, "text-shadow");
        DisplayAttribute<Boolean> seeThroughAttribute = loadDisplayAttribute(config, "see-through");
        DisplayAttribute<TextDisplayAlignment> alignmentAttribute = loadDisplayAttribute(config, "alignment");

        TextDisplay textDisplay = new TextDisplay(id, location);
        for (List<String> pageLine : pageLines) {
            TextDisplayPage page = new TextDisplayPage();
            page.setLines(pageLine);
            textDisplay.addPage(page);
        }
        textDisplay.setLineWidthAttribute(lineWidthAttribute);
        textDisplay.setBackgroundColorAttribute(backgroundColorAttribute);
        textDisplay.setTextOpacityAttribute(textOpacityAttribute);
        textDisplay.setTextShadowAttribute(textShadowAttribute);
        textDisplay.setSeeThroughAttribute(seeThroughAttribute);
        textDisplay.setAlignmentAttribute(alignmentAttribute);
        return textDisplay;
    }

    private List<List<String>> getPageLines(DisplayConfig config) {
        if (!config.isList("pages")) {
            throw new DisplayConfigException("Display is missing pages");
        }
        List<Map<?, ?>> pages = config.getMapList("pages");
        if (pages.isEmpty()) {
            throw new DisplayConfigException("Display has no pages");
        }
        List<List<String>> pageLines = new ArrayList<>();
        int pageIndex = 0;
        for (Map<?, ?> page : pages) {
            List<String> lines = Optional.ofNullable(page.get("lines"))
                    .filter(List.class::isInstance)
                    .map(List.class::cast)
                    .map(list -> ((List<?>) list).stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .collect(Collectors.toList()))
                    .orElse(Collections.emptyList());
            if (lines.isEmpty()) {
                throw new DisplayConfigException("Page " + pageIndex + " has invalid lines");
            }
            pageLines.add(lines);
            pageIndex++;
        }
        return pageLines;
    }

    private ItemDisplay loadItemDisplay(DisplayConfig config, String id, DecentLocation location) {
        HologramItem item = loadItem(config);
        DisplayAttribute<ItemDisplayType> displayTypeAttribute = loadDisplayAttribute(config, "display-type");
        DisplayAttribute<DisplayColor> glowColorAttribute = loadDisplayAttribute(config, "glow-color");

        ItemDisplay itemDisplay = new ItemDisplay(id, location);
        itemDisplay.setDisplayedItem(item);
        itemDisplay.setDisplayTypeAttribute(displayTypeAttribute);
        itemDisplay.setGlowColorAttribute(glowColorAttribute);
        return itemDisplay;
    }

    private HologramItem loadItem(DisplayConfig config) {
        if (!config.isString("item")) {
            throw new DisplayConfigException("Display is missing item");
        }
        String itemContent = config.getString("item");
        return new HologramItem(itemContent);
    }

    private BlockDisplay loadBlockDisplay(DisplayConfig config, String id, DecentLocation location) {
        Material material = loadMaterial(config);
        DisplayAttribute<DisplayColor> glowColorAttribute = loadDisplayAttribute(config, "glow-color");

        BlockDisplay display = new BlockDisplay(id, location);
        display.setMaterial(material);
        display.setGlowColorAttribute(glowColorAttribute);
        return display;
    }

    private Material loadMaterial(DisplayConfig config) {
        if (!config.isString("material")) {
            throw new DisplayConfigException("Display is missing material");
        }
        String materialName = config.getString("material");
        Material material = DecentMaterial.parseMaterial(materialName);
        if (material == null) {
            throw new DisplayConfigException("Invalid material: " + materialName);
        }
        return material;
    }

    private <T> DisplayAttribute<T> loadDisplayAttribute(DisplayConfig config, String name) {
        AttributeDefinition<T> definition = getAttributeDefinition(name);
        if (definition == null) {
            throw new DisplayConfigException("Unknown attribute: " + name);
        }
        return config.getDisplayAttribute("attributes." + name, definition);
    }

    private <T> AttributeDefinition<T> getAttributeDefinition(String name) {
        try {
            return (AttributeDefinition<T>) attributeDefinitionRegistry.getDefinition(name);
        } catch (ClassCastException e) {
            throw new DisplayConfigException("Attribute " + name + " has invalid data type");
        }
    }
}
