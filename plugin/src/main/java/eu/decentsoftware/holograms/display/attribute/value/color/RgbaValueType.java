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

package eu.decentsoftware.holograms.display.attribute.value.color;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValueType;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class RgbaValueType implements AttributeValueType<RgbaValue, DecentColor> {

    public static final String TYPE_ID = "rgba";

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public Class<RgbaValue> getValueClass() {
        return RgbaValue.class;
    }

    @Override
    public Class<DecentColor> getOutputType() {
        return DecentColor.class;
    }

    @Override
    public void serialize(RgbaValue value, ConfigurationNode node) throws SerializationException {
        node.node("red").set(value.getRed());
        node.node("green").set(value.getGreen());
        node.node("blue").set(value.getBlue());
        node.node("alpha").set(value.getAlpha());
    }

    @Override
    public RgbaValue deserialize(ConfigurationNode node) {
        int red = node.node("red").getInt(0);
        int green = node.node("green").getInt(0);
        int blue = node.node("blue").getInt(0);
        Integer alpha = node.node("alpha").getInt();
        return new RgbaValue(red, green, blue, alpha);
    }
}
