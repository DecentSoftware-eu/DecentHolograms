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
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.render.state.FinalDisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GlowColorAttributeDefinition implements AttributeDefinition<DecentColor> {

    public static final AttributeKey<DecentColor> KEY = AttributeKey.of("glow-color", DecentColor.class);
    private final DecentColorAttributeCommandHandler commandHandler = new DecentColorAttributeCommandHandler();

    @Override
    public @NotNull AttributeKey<DecentColor> getKey() {
        return KEY;
    }

    @Override
    public DecentColor getDefaultValue() {
        return null;
    }

    @Override
    public void apply(AttributeValue<DecentColor> value, FinalDisplayRenderState state) {
        DecentColor finalValue = value.identity();
        if (finalValue != null) {
            state.addMetadata(BuiltInMetadataKeys.GLOWING.createValue(true));
            state.addMetadata(BuiltInMetadataKeys.GLOW_COLOR_OVERRIDE.createValue(finalValue));
        } else {
            state.addMetadata(BuiltInMetadataKeys.GLOWING.createValue(false));
        }
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.ITEM, DisplayType.BLOCK};
    }

    @Override
    public String format(DecentColor value) {
        return commandHandler.format(value);
    }

    @Override
    public @NotNull DecentColor parse(String[] args) {
        return commandHandler.parseColor(args);
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        return commandHandler.getHints(args);
    }
}
