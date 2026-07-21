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

package eu.decentsoftware.holograms.api.commands;

import eu.decentsoftware.holograms.api.utils.items.DecentMaterial;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Helper class for simple command tab completions.
 */
public final class CommandTabCompleteHelper {

    private CommandTabCompleteHelper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Get all item material names, including "<HAND>" if the sender is a player.
     *
     * @param sender The sender.
     * @return A list of item material names.
     */
    public static List<String> getItemMaterialNames(CommandSender sender) {
        List<String> itemMaterialNames = new ArrayList<>();
        if (sender instanceof Player) {
            itemMaterialNames.add("<HAND>"); // Sets the item to the player's current held item in the main hand
        }
        itemMaterialNames.addAll(getItemMaterialNames());
        return itemMaterialNames;
    }

    private static List<String> getItemMaterialNames() {
        return Arrays.stream(Material.values())
                .filter(DecentMaterial::isItem)
                .map(Material::name)
                .collect(Collectors.toList());
    }

    /**
     * Get all block material names.
     *
     * @return A list of block material names.
     */
    public static List<String> getBlockMaterialNames() {
        return Arrays.stream(Material.values())
                .filter(Material::isBlock)
                .map(Material::name)
                .collect(Collectors.toList());
    }
}
