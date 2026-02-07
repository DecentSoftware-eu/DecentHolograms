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
import eu.decentsoftware.holograms.display.attribute.value.compiled.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBrightness;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BrightnessAttributeDefinition implements AttributeDefinition<DisplayBrightness> {

    public static final AttributeKey<DisplayBrightness> KEY = AttributeKey.of("brightness", DisplayBrightness.class);

    @Override
    public @NotNull AttributeKey<DisplayBrightness> getKey() {
        return KEY;
    }

    @Override
    public DisplayBrightness getDefaultValue() {
        return null;
    }

    @Override
    public void apply(CompiledAttributeValue<DisplayBrightness> value, FinalDisplayRenderState state) {
        DisplayBrightness finalValue = value.identity();
        if (finalValue != null) {
            state.addMetadata(BuiltInMetadataKeys.BRIGHTNESS.createValue(finalValue));
        } else {
            state.addMetadata(BuiltInMetadataKeys.BRIGHTNESS.createValue(getDefaultValue()));
        }
    }

    @Override
    public String format(DisplayBrightness value) {
        if (value == null) {
            return null;
        }
        return "Block Light: " + value.getBlockLight() + ", Sky Light: " + value.getSkyLight();
    }

    @Override
    public @NotNull DisplayBrightness parse(String[] args) {
        if (args.length == 2) {
            int blockLight = parseSingleValue(args[0], "Block Light");
            int skyLight = parseSingleValue(args[1], "Sky Light");
            return DisplayBrightness.of(blockLight, skyLight);
        } else {
            throw new AttributeParseException("Brightness must be specified as two separate values for block light and sky light.");
        }
    }

    private int parseSingleValue(String value, String name) {
        try {
            int parsed = Integer.parseInt(value);
            if (parsed < 0 || parsed > 15) {
                throw new AttributeParseException(name + " must be between 0 and 15.");
            }
            return parsed;
        } catch (NumberFormatException e) {
            throw new AttributeParseException(name + " must be an integer.");
        }
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1 || args.length == 2) {
            return IntStream.range(0, 16).boxed().map(String::valueOf).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
