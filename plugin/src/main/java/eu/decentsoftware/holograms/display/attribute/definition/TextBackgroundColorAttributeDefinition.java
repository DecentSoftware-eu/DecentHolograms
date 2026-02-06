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

import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextBackgroundColorAttributeDefinition implements AttributeDefinition<DecentColor> {

    public static final AttributeKey<DecentColor> KEY = AttributeKey.of("background-color", DecentColor.class);
    private final DecentColorAttributeCommandHandler commandHandler = new DecentColorAttributeCommandHandler();

    @Override
    public @NotNull AttributeKey<DecentColor> getKey() {
        return KEY;
    }

    @Override
    public DecentColor getDefaultValue() {
        return DecentColor.DEFAULT_BACKGROUND;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }

    @Override
    public void apply(DisplayAttribute<DecentColor> attribute, DisplayRenderState state, DisplayRenderContext context) {
        DecentColor value = attribute.getValue();
        if (value != null) {
            state.addMetadata(BuiltInMetadataKeys.TEXT_DISPLAY_BACKGROUND.createValue(value));
        } else {
            state.addMetadata(BuiltInMetadataKeys.TEXT_DISPLAY_BACKGROUND.createValue(getDefaultValue()));
        }
    }

    @Override
    public String format(DecentColor value) {
        if (value == null) {
            return null;
        }
        String rgbString = String.format("RGBA: %s, %s, %s, %s", value.getRed(), value.getGreen(), value.getBlue(), value.getAlpha());
        return value.asRGBString() + rgbString;
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
