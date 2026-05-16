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
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayAlignment;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class TextAlignmentValueType implements AttributeValueType<TextAlignmentValue, TextDisplayAlignment> {

    public static final String TYPE_ID = "text_alignment";

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public Class<TextAlignmentValue> getValueClass() {
        return TextAlignmentValue.class;
    }

    @Override
    public Class<TextDisplayAlignment> getOutputType() {
        return TextDisplayAlignment.class;
    }

    @Override
    public void serialize(TextAlignmentValue value, ConfigurationNode node) throws SerializationException {
        node.set(value.getAlignment());
    }

    @Override
    public TextAlignmentValue deserialize(ConfigurationNode node) throws SerializationException {
        TextDisplayAlignment alignment = node.get(TextDisplayAlignment.class);
        return new TextAlignmentValue(alignment);
    }
}
