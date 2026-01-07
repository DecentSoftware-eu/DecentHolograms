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

import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import eu.decentsoftware.holograms.display.DecentLocation;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.DisplayAttributeValueType;
import eu.decentsoftware.holograms.display.attribute.StaticDisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayBrightness;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class DisplayConfig extends FileConfig {

    public DisplayConfig(@NotNull JavaPlugin plugin, @NotNull String path) {
        super(plugin, path);
    }

    public DisplayConfig(@NotNull JavaPlugin plugin, @NotNull File file) {
        super(plugin, file);
    }

    public DecentLocation getDecentLocation(String path) {
        if (isConfigurationSection(path)) {
            ConfigurationSection section = getConfigurationSection(path);
            if (section.isString("world") && section.isDouble("x") && section.isDouble("y") && section.isDouble("z")) {
                String world = section.getString("world");
                double x = section.getDouble("x");
                double y = section.getDouble("y");
                double z = section.getDouble("z");
                float yaw = 0.0f;
                float pitch = 0.0f;
                if (section.isDouble("yaw")) {
                    yaw = (float) section.getDouble("yaw");
                }
                if (section.isDouble("pitch")) {
                    pitch = (float) section.getDouble("pitch");
                }
                return new DecentLocation(world, x, y, z, yaw, pitch);
            }
        }
        return null;
    }

    public void setDecentLocation(String path, DecentLocation location) {
        if (location == null) {
            set(path, null);
            return;
        }
        set(path + ".world", location.getWorldName());
        set(path + ".x", location.getX());
        set(path + ".y", location.getY());
        set(path + ".z", location.getZ());
        set(path + ".yaw", location.getYaw());
        set(path + ".pitch", location.getPitch());
    }

    public DisplayVector3f getDisplayVector3f(String path) {
        if (isConfigurationSection(path)) {
            ConfigurationSection section = getConfigurationSection(path);
            if (section.isDouble("x") && section.isDouble("y") && section.isDouble("z")) {
                float x = (float) section.getDouble("x");
                float y = (float) section.getDouble("y");
                float z = (float) section.getDouble("z");
                return new DisplayVector3f(x, y, z);
            }
        }
        return null;
    }

    public void setDisplayVector3f(String path, DisplayVector3f value) {
        if (value == null) {
            set(path, null);
            return;
        }
        set(path + ".x", value.getX());
        set(path + ".y", value.getY());
        set(path + ".z", value.getZ());
    }

    public DisplayColor getDisplayColor(String path) {
        if (isConfigurationSection(path)) {
            ConfigurationSection section = getConfigurationSection(path);
            if (section.isInt("red") && section.isInt("green") && section.isInt("blue")) {
                int alpha;
                if (section.isInt("alpha")) {
                    alpha = section.getInt("alpha");
                } else {
                    alpha = 255;
                }
                int red = section.getInt("red");
                int green = section.getInt("green");
                int blue = section.getInt("blue");
                return new DisplayColor(alpha, red, green, blue);
            }
        }
        return null;
    }

    public void setDisplayColor(String path, DisplayColor color) {
        if (color == null) {
            set(path, null);
            return;
        }
        set(path + ".alpha", color.getAlpha());
        set(path + ".red", color.getRed());
        set(path + ".green", color.getGreen());
        set(path + ".blue", color.getBlue());
    }

    public DisplayBrightness getDisplayBrightness(String path) {
        if (isConfigurationSection(path)) {
            ConfigurationSection section = getConfigurationSection(path);
            if (section.isInt("block-light") && section.isInt("sky-light")) {
                int blockLight = getInt("block-light", 0, 15);
                int skyLight = getInt("sky-light", 0, 15);
                return DisplayBrightness.of(blockLight, skyLight);
            }
        }
        return null;
    }

    public void setDisplayBrightness(String path, DisplayBrightness brightness) {
        if (brightness == null) {
            set(path, null);
            return;
        }
        set(path + ".block-light", brightness.getBlockLight());
        set(path + ".sky-light", brightness.getSkyLight());
    }

    public <T extends Enum<?>> T getEnum(String path, Class<T> enumType) {
        if (isString(path)) {
            String name = getString(path);
            for (T enumConstant : enumType.getEnumConstants()) {
                if (enumConstant.name().equalsIgnoreCase(name)) {
                    return enumConstant;
                }
            }
        }
        return null;
    }

    public <T extends Enum<?>> T getEnum(String path, Class<T> enumType, T defaultValue) {
        T value = getEnum(path, enumType);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    private int getInt(String path, int min, int max) {
        int blockLight = getInt(path);
        if (blockLight < min) {
            blockLight = min;
        } else if (blockLight > max) {
            blockLight = max;
        }
        return blockLight;
    }

    public void setEnum(String path, Enum<?> value) {
        set(path, value == null ? null : value.name());
    }

    public <T> DisplayAttribute<T> getDisplayAttribute(String path, AttributeDefinition<T> definition) {
        if (isConfigurationSection(path)) {
            DisplayAttributeValueType valueType = getEnum(path + ".value-type", DisplayAttributeValueType.class, DisplayAttributeValueType.STATIC);
            if (valueType == DisplayAttributeValueType.STATIC) {
                return getStaticDisplayAttribute(path, definition);
            }
        }
        return null;
    }

    private <T> StaticDisplayAttribute<T> getStaticDisplayAttribute(String path, AttributeDefinition<T> definition) {
        if (definition.getValueType().isAssignableFrom(Boolean.class)) {
            Boolean value = getBoolean(path + ".value", (Boolean) definition.getDefaultValue());
            return new StaticDisplayAttribute<>((T) value);
        } else if (definition.getValueType().isAssignableFrom(Integer.class)) {
            Integer value = getInt(path + ".value", (Integer) definition.getDefaultValue());
            return new StaticDisplayAttribute<>((T) value);
        } else if (definition.getValueType().isAssignableFrom(Float.class)) {
            Float value = (float) getDouble(path + ".value", (Float) definition.getDefaultValue());
            return new StaticDisplayAttribute<>((T) value);
        } else if (definition.getValueType().isAssignableFrom(DisplayVector3f.class)) {
            DisplayVector3f value = getDisplayVector3f(path + ".value");
            if (value == null) {
                value = (DisplayVector3f) definition.getDefaultValue();
            }
            return new StaticDisplayAttribute<>((T) value);
        } else if (definition.getValueType().isAssignableFrom(DisplayColor.class)) {
            DisplayColor value = getDisplayColor(path + ".value");
            if (value == null) {
                value = (DisplayColor) definition.getDefaultValue();
            }
            return new StaticDisplayAttribute<>((T) value);
        } else if (definition.getValueType().isAssignableFrom(DisplayBrightness.class)) {
            DisplayBrightness value = getDisplayBrightness(path + ".value");
            if (value == null) {
                value = (DisplayBrightness) definition.getDefaultValue();
            }
            return new StaticDisplayAttribute<>((T) value);
        } else if (definition.getValueType().isEnum()) {
            Enum<?> value = getEnum(path + ".value", (Class<? extends Enum<?>>) definition.getValueType());
            if (value == null) {
                value = (Enum<?>) definition.getDefaultValue();
            }
            return new StaticDisplayAttribute<>((T) value);
        }
        return null;
    }
}
