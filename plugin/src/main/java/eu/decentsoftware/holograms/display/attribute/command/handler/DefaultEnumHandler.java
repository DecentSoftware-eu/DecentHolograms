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

package eu.decentsoftware.holograms.display.attribute.command.handler;

import eu.decentsoftware.holograms.display.attribute.AttributeParseException;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultEnumHandler<E extends Enum<E>> implements AttributeCommandHandler<E> {

    private final Class<E> enumClass;
    private final Function<E, AttributeValue<E>> createValueFunction;
    private final List<String> hints;

    public DefaultEnumHandler(Class<E> enumClass, Function<E, AttributeValue<E>> createValueFunction) {
        this.enumClass = enumClass;
        this.createValueFunction = createValueFunction;
        this.hints = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public String getKeyword() {
        return null;
    }

    @Override
    public AttributeValue<E> parse(String[] args) {
        String input = args[0].toUpperCase();
        try {
            E value = Enum.valueOf(enumClass, input);
            return createValueFunction.apply(value);
        } catch (IllegalArgumentException e) {
            throw new AttributeParseException("Valid options are: " + String.join(", ", hints));
        }
    }

    @Override
    public List<String> getHints(CommandSender sender, String[] args) {
        if (args.length == 1) {
            return hints;
        }
        return Collections.emptyList();
    }
}
