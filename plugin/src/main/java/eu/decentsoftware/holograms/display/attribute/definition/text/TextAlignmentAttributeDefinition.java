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

package eu.decentsoftware.holograms.display.attribute.definition.text;

import eu.decentsoftware.holograms.display.DisplayType;
import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.EnumDisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayAlignment;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TextAlignmentAttributeDefinition implements AttributeDefinition<TextDisplayAlignment> {

    public static final AttributeKey<TextDisplayAlignment> KEY = AttributeKey.of("alignment", TextDisplayAlignment.class);
    private static final List<String> VALUE_HINTS = Arrays.stream(TextDisplayAlignment.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    private final EnumDisplayAttributeParser<TextDisplayAlignment> parser = new EnumDisplayAttributeParser<>(TextDisplayAlignment.class);

    @Override
    public @NotNull AttributeKey<TextDisplayAlignment> getKey() {
        return KEY;
    }

    @Override
    public @NotNull DisplayAttributeParser<TextDisplayAlignment> getParser() {
        return parser;
    }

    @Override
    public TextDisplayAlignment getDefaultValue() {
        return TextDisplayAlignment.CENTER;
    }

    @Override
    public @NotNull DisplayType[] getApplicableDisplayTypes() {
        return new DisplayType[]{DisplayType.TEXT};
    }

    @Override
    public @NotNull List<String> valueHints(CommandSender sender, String currentInput) {
        return VALUE_HINTS;
    }
}
