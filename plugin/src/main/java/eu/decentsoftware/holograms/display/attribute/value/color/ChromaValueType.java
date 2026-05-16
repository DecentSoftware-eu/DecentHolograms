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

public final class ChromaValueType implements AttributeValueType<ChromaValue, DecentColor> {

    public static final String TYPE_ID = "chroma";

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public Class<ChromaValue> getValueClass() {
        return ChromaValue.class;
    }

    @Override
    public Class<DecentColor> getOutputType() {
        return DecentColor.class;
    }

    @Override
    public void serialize(ChromaValue value, ConfigurationNode node) throws SerializationException {
        node.node("period").set(value.getPeriod());
        node.node("alpha").set(value.getAlpha());
        node.node("saturation").set(value.getSaturation());
        node.node("value").set(value.getValue());
    }

    @Override
    public ChromaValue deserialize(ConfigurationNode node) {
        int period = node.node("period").getInt(ChromaCompiledValue.DEFAULT_PERIOD);
        int alpha = node.node("alpha").getInt(255);
        int saturation = node.node("saturation").getInt(ChromaCompiledValue.DEFAULT_SATURATION);
        int value = node.node("value").getInt(ChromaCompiledValue.DEFAULT_VALUE);
        return new ChromaValue(period, alpha, saturation, value);
    }
}
