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

package eu.decentsoftware.holograms.platform.bukkit.render;

import eu.decentsoftware.holograms.api.utils.items.DecentMaterial;
import eu.decentsoftware.holograms.api.utils.items.ItemBuilder;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;
import eu.decentsoftware.holograms.platform.api.data.ItemDescriptor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BukkitItemFactory {

    public ItemStack createItemStack(ItemDescriptor descriptor) {
        Material material = resolveMaterial(descriptor.getMaterial());
        ItemBuilder itemBuilder = new ItemBuilder(material);
        applyEnchantGlint(descriptor, itemBuilder);
        applyCustomModelData(descriptor, itemBuilder);
        applySkullTexture(descriptor, material, itemBuilder);
        applyLeatherColor(descriptor, material, itemBuilder);
        return itemBuilder.toItemStack();
    }

    private static Material resolveMaterial(String materialName) {
        return Material.matchMaterial(materialName);
    }

    private static void applyEnchantGlint(ItemDescriptor descriptor, ItemBuilder itemBuilder) {
        if (descriptor.isEnchanted()) {
            itemBuilder.withUnsafeEnchantment(Enchantment.values()[0], 1);
        }
    }

    private static void applyCustomModelData(ItemDescriptor descriptor, ItemBuilder itemBuilder) {
        // TODO
    }

    private static void applySkullTexture(ItemDescriptor descriptor, Material material, ItemBuilder itemBuilder) {
        String skullTexture = descriptor.getSkullTexture();
        if (skullTexture != null && DecentMaterial.isSkull(material)) {
            if (skullTexture.length() <= 16) {
                itemBuilder.withSkullOwner(skullTexture);
            } else {
                itemBuilder.withSkullTexture(skullTexture);
            }
        }
    }

    private static void applyLeatherColor(ItemDescriptor descriptor, Material material, ItemBuilder itemBuilder) {
        DecentColor color = descriptor.getLeatherColor();
        if (color != null && DecentMaterial.isLeatherArmor(material)) {
            itemBuilder.withLeatherArmorColor(mapDecentColorToBukkitColor(color));
        }
    }

    private static Color mapDecentColorToBukkitColor(DecentColor decentColor) {
        return Color.fromRGB(decentColor.getRed(), decentColor.getGreen(), decentColor.getBlue());
    }
}
