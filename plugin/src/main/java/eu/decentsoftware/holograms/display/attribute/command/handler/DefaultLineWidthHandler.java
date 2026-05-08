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
import eu.decentsoftware.holograms.display.attribute.value.primitives.IntegerValue;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefaultLineWidthHandler implements AttributeCommandHandler<Integer> {

    @Override
    public String getKeyword() {
        return null;
    }

    @Override
    public @NotNull AttributeValue<Integer> parse(String[] args) {
        try {
            int lineWidth = Integer.parseInt(args[0]);
            if (lineWidth < 1) {
                throw new AttributeParseException("Line width must be higher than 0.");
            }
            return new IntegerValue(lineWidth);
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
