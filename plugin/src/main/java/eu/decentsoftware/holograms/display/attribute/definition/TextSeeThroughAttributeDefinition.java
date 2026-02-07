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
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayProperties;
import eu.decentsoftware.holograms.platform.api.render.metadata.BuiltInMetadataKeys;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataValue;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TextSeeThroughAttributeDefinition implements AttributeDefinition<Boolean> {

    public static final AttributeKey<Boolean> KEY = AttributeKey.of("see-through", Boolean.class);

    @Override
    public @NotNull AttributeKey<Boolean> getKey() {
        return KEY;
    }

    @Override
    public Boolean getDefaultValue() {
        return false;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }

    @Override
    public void apply(AttributeValue<Boolean> value, FinalDisplayRenderState state) {
        MetadataValue<TextDisplayProperties> metadataValue = getTextDisplayPropertiesMetadataValue(state);
        metadataValue.getValue().setSeeThrough(value.identity());
    }

    private MetadataValue<TextDisplayProperties> getTextDisplayPropertiesMetadataValue(FinalDisplayRenderState state) {
        MetadataValue<TextDisplayProperties> metadataValue;
        if (state.hasMetadataValue(BuiltInMetadataKeys.TEXT_DISPLAY_PROPERTIES)) {
            metadataValue = state.getMetadataValue(BuiltInMetadataKeys.TEXT_DISPLAY_PROPERTIES);
        } else {
            metadataValue = BuiltInMetadataKeys.TEXT_DISPLAY_PROPERTIES.createValue(new TextDisplayProperties());
            state.addMetadata(metadataValue);
        }
        return metadataValue;
    }

    @Override
    public @NotNull Boolean parse(String[] args) {
        return Boolean.parseBoolean(args[0]);
    }

    @Override
    public @NotNull List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return Arrays.asList("true", "false");
        }
        return Collections.emptyList();
    }
}
