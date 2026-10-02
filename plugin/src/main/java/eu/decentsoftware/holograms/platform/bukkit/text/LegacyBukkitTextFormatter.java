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

package eu.decentsoftware.holograms.platform.bukkit.text;

import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import eu.decentsoftware.holograms.platform.api.text.TextFormat;
import eu.decentsoftware.holograms.platform.api.text.TextFormatter;
import org.jetbrains.annotations.NotNull;

/**
 * An implementation of the {@link TextFormatter} interface designed for Bukkit's
 * legacy text formatting. This formatter applies legacy color codes, RGB colors
 * and gradients.
 *
 * @author d0by
 * @see TextFormatter
 * @see TextFormat#LEGACY
 * @since 2.10.2
 */
public class LegacyBukkitTextFormatter implements TextFormatter {

    @NotNull
    @Override
    public String format(@NotNull String text) {
        return IridiumColorAPI.process(text);
    }
}
