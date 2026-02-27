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

import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplaySettings;
import eu.decentsoftware.holograms.display.ItemDisplay;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.display.attribute.AttributeConfigMapper;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.config.dto.ConfigAttribute;
import eu.decentsoftware.holograms.display.config.dto.ConfigDecentLocation;
import eu.decentsoftware.holograms.display.config.dto.ConfigDisplay;
import eu.decentsoftware.holograms.display.config.dto.ConfigDisplaySettings;
import eu.decentsoftware.holograms.display.config.dto.ConfigTextPage;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DisplayConfigMapper {

    private final AttributeConfigMapper attributeConfigMapper;

    public DisplayConfigMapper(AttributeConfigMapper attributeConfigMapper) {
        this.attributeConfigMapper = attributeConfigMapper;
    }

    public DisplayBase toDomain(ConfigDisplay dto) {
        DecentLocation location = locationToDomain(dto.getLocation());
        DisplaySettings settings = settingsToDomain(dto.getSettings());
        DisplayBase display;
        switch (dto.getType()) {
            case TEXT:
                display = textDisplayToDomain(dto, location, settings);
                break;
            case ITEM:
                display = itemDisplayToDomain(dto, location, settings);
                break;
            case BLOCK:
                display = blockDisplayToDomain(dto, location, settings);
                break;
            default:
                throw new DisplayConfigException("Unknown display type: " + dto.getType());
        }
        attributeConfigMapper.attributesToDomain(display, dto.getAttributes());
        return display;
    }

    private BlockDisplay blockDisplayToDomain(ConfigDisplay dto, DecentLocation location, DisplaySettings settings) {
        if (dto.getBlock() == null) {
            throw new DisplayConfigException("Block display must have a material");
        }
        BlockDisplay blockDisplay = new BlockDisplay(dto.getName(), location, settings);
        blockDisplay.setMaterial(dto.getBlock());
        return blockDisplay;
    }

    private ItemDisplay itemDisplayToDomain(ConfigDisplay dto, DecentLocation location, DisplaySettings settings) {
        if (dto.getItem() == null) {
            throw new DisplayConfigException("Item display must have an item");
        }
        ItemDisplay itemDisplay = new ItemDisplay(dto.getName(), location, settings);
        itemDisplay.setMaterial(dto.getItem());
        return itemDisplay;
    }

    private TextDisplay textDisplayToDomain(ConfigDisplay dto, DecentLocation location, DisplaySettings settings) {
        List<ConfigTextPage> pages = dto.getPages();
        if (pages == null || pages.isEmpty()) {
            throw new DisplayConfigException("Text display must have at least one page");
        }
        TextDisplay textDisplay = new TextDisplay(dto.getName(), location, settings);
        textDisplay.setLines(dto.getPages().get(0).getLines());
        return textDisplay;
    }

    private DecentLocation locationToDomain(ConfigDecentLocation dto) {
        return new DecentLocation(dto.getWorld(), dto.getX(), dto.getY(), dto.getZ(), dto.getYaw(), dto.getPitch());
    }

    private DisplaySettings settingsToDomain(ConfigDisplaySettings dto) {
        if (dto == null) {
            return new DisplaySettings();
        }
        DisplaySettings settings = new DisplaySettings();
        settings.setEnabled(dto.isEnabled());
        settings.setDisplayRange(dto.getDisplayRange());
        settings.setUpdateInterval(dto.getUpdateInterval());
        return settings;
    }

    public ConfigDisplay toDto(DisplayBase domain) {
        ConfigDisplay dto = new ConfigDisplay();
        dto.setName(domain.getName());
        dto.setType(domain.getType());
        dto.setLocation(locationToDto(domain.getLocation()));
        dto.setSettings(settingsToDto(domain.getSettings()));
        dto.setAttributes(attributesToDto(domain.getAttributes()));
        switch (domain.getType()) {
            case TEXT:
                dto.setPages(pagesToDto((TextDisplay) domain));
                break;
            case ITEM:
                dto.setItem(((ItemDisplay) domain).getMaterial());
                break;
            case BLOCK:
                dto.setBlock(((BlockDisplay) domain).getMaterial());
                break;
        }
        return dto;
    }

    private List<ConfigTextPage> pagesToDto(TextDisplay domain) {
        return Collections.singletonList(pageToDto(domain.getLines()));
    }

    private ConfigTextPage pageToDto(List<String> lines) {
        ConfigTextPage pageDto = new ConfigTextPage();
        pageDto.setLines(lines);
        return pageDto;
    }

    private ConfigDecentLocation locationToDto(DecentLocation location) {
        ConfigDecentLocation dto = new ConfigDecentLocation();
        dto.setWorld(location.getWorldName());
        dto.setX(location.getX());
        dto.setY(location.getY());
        dto.setZ(location.getZ());
        dto.setYaw(location.getYaw());
        dto.setPitch(location.getPitch());
        return dto;
    }

    private ConfigDisplaySettings settingsToDto(DisplaySettings settings) {
        ConfigDisplaySettings dto = new ConfigDisplaySettings();
        dto.setEnabled(settings.isEnabled());
        dto.setDisplayRange(settings.getDisplayRange());
        dto.setUpdateInterval(settings.getUpdateInterval());
        return dto;
    }

    private Map<String, ConfigAttribute> attributesToDto(Collection<DisplayAttribute<?>> attributes) {
        return attributes.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(
                        attribute -> attribute.getKey().getName(),
                        this::attributeToDto
                ));
    }

    private ConfigAttribute attributeToDto(DisplayAttribute<?> attribute) {
        ConfigAttribute dto = new ConfigAttribute();
        AttributeValue<?> attributeValue = attribute.getValue();
        if (attributeValue != null) {
            dto.setValueType(attributeValue.getTypeKey());
        }
        dto.setValue(attributeValue);
        return dto;
    }
}
