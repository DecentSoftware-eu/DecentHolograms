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

package eu.decentsoftware.holograms.display.attribute.value.display;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValueType;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class BrightnessValueType implements AttributeValueType<BrightnessValue, DisplayBrightness> {

    public static final String TYPE_ID = "brightness";

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public Class<BrightnessValue> getValueClass() {
        return BrightnessValue.class;
    }

    @Override
    public Class<DisplayBrightness> getOutputType() {
        return DisplayBrightness.class;
    }

    @Override
    public void serialize(BrightnessValue value, ConfigurationNode node) throws SerializationException {
        node.node("block-light").set(value.getBlockLight());
        node.node("sky-light").set(value.getSkyLight());
    }

    @Override
    public BrightnessValue deserialize(ConfigurationNode node) throws SerializationException {
        Integer blockLight = node.node("block-light").get(Integer.class);
        if (blockLight == null) {
            throw new SerializationException("block-light is missing");
        }
        Integer skyLight = node.node("sky-light").get(Integer.class);
        if (skyLight == null) {
            throw new SerializationException("sky-light is missing");
        }
        return new BrightnessValue(blockLight, skyLight);
    }
}
