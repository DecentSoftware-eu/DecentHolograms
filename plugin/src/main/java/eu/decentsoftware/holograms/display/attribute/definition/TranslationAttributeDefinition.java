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

package eu.decentsoftware.holograms.display.attribute.definition;

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TranslationAttributeDefinition implements AttributeDefinition<DecentVector3f> {

    public static final AttributeKey<DecentVector3f> KEY = AttributeKey.of("translation", DecentVector3f.class);
    private static final int MIN_VALUE = -8;
    private static final int MAX_VALUE = 8;
    private final DecentVector3f defaultValue = new DecentVector3f(0, 0, 0);

    @Override
    public @NotNull AttributeKey<DecentVector3f> getKey() {
        return KEY;
    }

    @Override
    public DecentVector3f getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void apply(AttributeValue<DecentVector3f> value, FinalDisplayRenderState state) {
        DecentVector3f finalValue = value.identity();
        if (finalValue != null) {
            state.addMetadata(BuiltInMetadataKeys.TRANSLATION.createValue(finalValue));
        } else {
            state.addMetadata(BuiltInMetadataKeys.TRANSLATION.createValue(getDefaultValue()));
        }
    }

    @Override
    public String format(DecentVector3f value) {
        if (value == null) {
            return null;
        }
        return "X: " + value.getX() + ", Y: " + value.getY() + ", Z: " + value.getZ();
    }

    @Override
    public @NotNull DecentVector3f parse(String[] args) {
        if (args.length == 3) {
            float x = parseSingleValue(args[0], "X");
            float y = parseSingleValue(args[1], "Y");
            float z = parseSingleValue(args[2], "Z");
            return new DecentVector3f(x, y, z);
        } else {
            throw new AttributeParseException("Translation must be specified as three separate values for X, Y, and Z.");
        }
    }

    private float parseSingleValue(String value, String name) {
        try {
            float parsed = Float.parseFloat(value);
            if (parsed <= MIN_VALUE || parsed > MAX_VALUE) {
                throw new AttributeParseException(name + " must be between " + MIN_VALUE + " and " + MAX_VALUE + ".");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new AttributeParseException(name + " must be a number.");
        }
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length >= 1 && args.length <= 3) {
            return Arrays.asList(
                    "-0.25", "-0.5", "-0.75", "-1", "-1.5", "-2", "-3", "-4", "-8",
                    "0.25", "0.5", "0.75", "1", "1.5", "2", "3", "4", "8"
            );
        }
        return Collections.emptyList();
    }
}
