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
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextLineWidthAttributeDefinition implements AttributeDefinition<Integer> {

    public static final AttributeKey<Integer> KEY = AttributeKey.of("line-width", Integer.class);

    @Override
    public @NotNull AttributeKey<Integer> getKey() {
        return KEY;
    }

    @Override
    public @Nullable Integer getDefaultValue() {
        return 300;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }

    @Override
    public void apply(CompiledAttributeValue<Integer> value, FinalDisplayRenderState state) {
        Integer finalValue = value.identity();
        if (finalValue != null) {
            state.addMetadata(BuiltInMetadataKeys.TEXT_LINE_WIDTH.createValue(finalValue));
        } else {
            state.addMetadata(BuiltInMetadataKeys.TEXT_LINE_WIDTH.createValue(getDefaultValue()));
        }
    }

    @Override
    public @NotNull Integer parse(String[] args) {
        try {
            int lineWidth = Integer.parseInt(args[0]);
            if (lineWidth < 1) {
                throw new AttributeParseException("Line width must be higher than 0.");
            }
            return lineWidth;
        } catch (NumberFormatException e) {
            throw new AttributeParseException("Line width must be a positive integer.");
        }
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("200", "300", "400", "500", "1000");
        }
        return Collections.emptyList();
    }
}
