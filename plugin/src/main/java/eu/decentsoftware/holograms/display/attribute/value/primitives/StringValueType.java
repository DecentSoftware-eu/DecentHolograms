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

package eu.decentsoftware.holograms.display.attribute.value.primitives;

import eu.decentsoftware.holograms.display.attribute.value.AttributeValueType;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

public final class StringValueType implements AttributeValueType<StringValue, String> {

    public static final String TYPE_ID = "string";
    private final StringValueFactory valueFactory;

    public StringValueType(StringValueFactory valueFactory) {
        this.valueFactory = valueFactory;
    }

    @Override
    public String getTypeId() {
        return TYPE_ID;
    }

    @Override
    public Class<StringValue> getValueClass() {
        return StringValue.class;
    }

    @Override
    public Class<String> getOutputType() {
        return String.class;
    }

    @Override
    public void serialize(StringValue value, ConfigurationNode node) throws SerializationException {
        node.set(value.getValue());
    }

    @Override
    public StringValue deserialize(ConfigurationNode node) {
        String rawValue = node.getString();
        return valueFactory.create(rawValue);
    }
}
