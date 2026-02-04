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
import eu.decentsoftware.holograms.platform.api.data.display.DisplayBillboardConstraints;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BillboardAttributeDefinition implements AttributeDefinition<DisplayBillboardConstraints> {

    public static final AttributeKey<DisplayBillboardConstraints> KEY = AttributeKey.of("billboard", DisplayBillboardConstraints.class);
    private static final List<String> VALUE_HINTS = Arrays.stream(DisplayBillboardConstraints.values())
            .map(Enum::name)
            .collect(Collectors.toList());

    @Override
    public @NotNull AttributeKey<DisplayBillboardConstraints> getKey() {
        return KEY;
    }

    @Override
    public DisplayBillboardConstraints getDefaultValue() {
        return DisplayBillboardConstraints.FIXED;
    }

    @Override
    public void apply(DisplayAttribute<DisplayBillboardConstraints> attribute, DisplayRenderState state, DisplayRenderContext context) {
        DisplayBillboardConstraints value = attribute.getValue();
        if (value != null) {
            state.addMetadata(BuiltInMetadataKeys.BILLBOARD_CONSTRAINTS.createValue(value));
        } else {
            state.addMetadata(BuiltInMetadataKeys.BILLBOARD_CONSTRAINTS.createValue(getDefaultValue()));
        }
    }

    @Override
    public @NotNull DisplayBillboardConstraints parse(String[] args) {
        try {
            return DisplayBillboardConstraints.valueOf(args[0]);
        } catch (IllegalArgumentException e) {
            throw new AttributeParseException("Billboard options are: " + String.join(", ", VALUE_HINTS));
        }
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return VALUE_HINTS;
        }
        return Collections.emptyList();
    }
}
