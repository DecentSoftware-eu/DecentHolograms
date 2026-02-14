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
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.primitives.FloatValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class YawAttributeDefinition implements AttributeDefinition<Float> {

    public static final AttributeKey<Float> KEY = AttributeKey.of("yaw", Float.class);

    @Override
    public @NotNull AttributeKey<Float> getKey() {
        return KEY;
    }

    @Override
    public @Nullable AttributeValue<Float> getDefaultValue() {
        return new FloatValue(0.0f);
    }

    @Override
    public void apply(CompiledAttributeValue<Float> value, FinalDisplayRenderState state) {
        Float finalValue = value.evaluate();
        if (finalValue != null) {
            DecentLocation location = state.getLocation();
            state.setLocation(new DecentLocation(
                    location.getWorldName(),
                    location.getX(),
                    location.getY(),
                    location.getZ(),
                    finalValue,
                    location.getPitch()
            ));
        }
    }

    @Override
    public @NotNull AttributeValue<Float> parse(String[] args) {
        try {
            float parsed = Float.parseFloat(args[0]);
            if (parsed < 0.0f || parsed > 360.0f) {
                throw new AttributeParseException("Yaw must be between 0.0 and 360.0.");
            }
            return new FloatValue(parsed);
        } catch (NumberFormatException e) {
            throw new AttributeParseException("Yaw must be a number.");
        }
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("0", "45", "90", "135", "180", "225", "270", "315", "360");
        }
        return Collections.emptyList();
    }
}
