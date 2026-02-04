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
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentVector3f;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScaleAttributeDefinition implements AttributeDefinition<DecentVector3f> {

    public static final AttributeKey<DecentVector3f> KEY = AttributeKey.of("scale", DecentVector3f.class);
    private static final int MAX_VALUE = 100;
    private final DecentVector3f defaultValue = new DecentVector3f(1, 1, 1);

    @Override
    public @NotNull AttributeKey<DecentVector3f> getKey() {
        return KEY;
    }

    @Override
    public DecentVector3f getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void apply(DisplayAttribute<DecentVector3f> attribute, DisplayRenderState state, DisplayRenderContext context) {
        DecentVector3f value = attribute.getValue();
        if (value != null) {
            state.addMetadata(BuiltInMetadataKeys.SCALE.createValue(value));
        } else {
            state.addMetadata(BuiltInMetadataKeys.SCALE.createValue(getDefaultValue()));
        }
    }

    @Override
    public String format(DecentVector3f value) {
        if (value == null) {
            return null;
        }
        if (value.getX() == value.getY() && value.getY() == value.getZ()) {
            return String.valueOf(value.getX());
        }
        return "X: " + value.getX() + ", Y: " + value.getY() + ", Z: " + value.getZ();
    }

    @Override
    public @NonNull DecentVector3f parse(String[] args) {
        if (args.length == 1) {
            float uniformScale = parseSingleValue(args[0], "Scale");
            return new DecentVector3f(uniformScale, uniformScale, uniformScale);
        } else if (args.length == 3) {
            float x = parseSingleValue(args[0], "X");
            float y = parseSingleValue(args[1], "Y");
            float z = parseSingleValue(args[2], "Z");
            return new DecentVector3f(x, y, z);
        } else {
            throw new AttributeParseException("Scale must be specified as either a single uniform value or three separate values for X, Y, and Z.");
        }
    }

    private float parseSingleValue(String value, String name) {
        try {
            float parsed = Float.parseFloat(value);
            if (parsed <= 0 || parsed > MAX_VALUE) {
                throw new AttributeParseException(name + " must be greater than 0 and not exceed " + MAX_VALUE + ".");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new AttributeParseException(name + " must be a number.");
        }
    }

    @Override
    public @NonNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length >= 1 && args.length <= 3) {
            return Arrays.asList("0.25", "0.5", "0.75", "1", "1.5", "2", "3", "4", "5", "10");
        }
        return Collections.emptyList();
    }
}
