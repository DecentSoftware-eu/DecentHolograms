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

package eu.decentsoftware.holograms.display.attribute.definition.general;

import eu.decentsoftware.holograms.display.attribute.AttributeKey;
import eu.decentsoftware.holograms.display.attribute.AttributeValidationException;
import eu.decentsoftware.holograms.display.attribute.definition.AttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.parser.DisplayAttributeParser;
import eu.decentsoftware.holograms.display.attribute.parser.Vector3fDisplayAttributeParser;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayVector3f;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class TranslationAttributeDefinition implements AttributeDefinition<DisplayVector3f> {

    public static final AttributeKey<DisplayVector3f> KEY = AttributeKey.of("translation", DisplayVector3f.class);
    private static final int MIN_VALUE = -8;
    private static final int MAX_VALUE = 8;
    private final Vector3fDisplayAttributeParser parser = new Vector3fDisplayAttributeParser();
    private final DisplayVector3f defaultValue = new DisplayVector3f(0, 0, 0);

    @Override
    public @NotNull AttributeKey<DisplayVector3f> getKey() {
        return KEY;
    }

    @Override
    public @NotNull DisplayAttributeParser<DisplayVector3f> getParser() {
        return parser;
    }

    @Override
    public DisplayVector3f getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void validate(DisplayVector3f value) throws AttributeValidationException {
        if (value.getX() <= MIN_VALUE || value.getY() <= MIN_VALUE || value.getZ() <= MIN_VALUE) {
            throw new AttributeValidationException("Scale values must be greater than " + MIN_VALUE + ".");
        }
        if (value.getX() > MAX_VALUE || value.getY() > MAX_VALUE || value.getZ() > MAX_VALUE) {
            throw new AttributeValidationException("Scale values must not exceed " + MAX_VALUE + ".");
        }
    }

    @Override
    public @NotNull List<String> valueHints(CommandSender sender, String currentInput) {
        return Arrays.asList("0,0,0", "0.5,0.5,0.5", "1,1,1", "2,2,2");
    }

    @Override
    public String format(DisplayVector3f value) {
        if (value == null) {
            return null;
        } else {
            return value.getX() + "," + value.getY() + "," + value.getZ();
        }
    }
}
