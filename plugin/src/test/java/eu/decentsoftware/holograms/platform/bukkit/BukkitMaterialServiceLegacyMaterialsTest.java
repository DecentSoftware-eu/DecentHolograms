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

package eu.decentsoftware.holograms.platform.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BukkitMaterialServiceLegacyMaterialsTest {

    private BukkitMaterialService service;

    @BeforeEach
    void setUp() {
        service = new BukkitMaterialService();
    }

    @BeforeAll
    static void beforeAll() throws ClassNotFoundException {
        try (MockedStatic<Bukkit> mockedBukkit = Mockito.mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::getServer).thenReturn(Mockito.mock(Server.class));
            mockedBukkit.when(Bukkit::getVersion).thenReturn("MC: 1.12");

            // Initialize XMaterial with the mocked server version
            Class.forName("com.cryptomorin.xseries.XMaterial");
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "",
            "INVALID_MATERIAL",
            "WOOL:99",
            "INK_SACK:20",
            "NOT_A_MATERIAL:0",
            "LOG:10",
            ":5",
            "MATERIAL:",
    })
    void testMappingLegacyMaterials_invalid(String from) {
        assertNull(service.toMojangNamespacedKey(from).orElse(null));
    }

    @ParameterizedTest
    @CsvSource({
            "INK_SACK,minecraft:black_dye",
            "INK_SACK:0,minecraft:black_dye",
            "INK_SACK:1,minecraft:red_dye",
            "INK_SACK:2,minecraft:green_dye",
            "INK_SACK:3,minecraft:cocoa_beans",
            "INK_SACK:4,minecraft:lapis_lazuli",
            "INK_SACK:5,minecraft:purple_dye",
            "INK_SACK:6,minecraft:cyan_dye",
            "INK_SACK:7,minecraft:light_gray_dye",
            "INK_SACK:8,minecraft:gray_dye",
            "INK_SACK:9,minecraft:pink_dye",
            "INK_SACK:10,minecraft:lime_dye",
            "INK_SACK:11,minecraft:yellow_dye",
            "INK_SACK:12,minecraft:light_blue_dye",
            "INK_SACK:13,minecraft:magenta_dye",
            "INK_SACK:14,minecraft:orange_dye",
            "INK_SACK:15,minecraft:bone_meal",

            "WOOL,minecraft:black_wool",
            "WOOL:0,minecraft:white_wool",
            "WOOL:1,minecraft:orange_wool",
            "WOOL:2,minecraft:magenta_wool",
            "WOOL:3,minecraft:light_blue_wool",
            "WOOL:4,minecraft:yellow_wool",
            "WOOL:5,minecraft:lime_wool",
            "WOOL:6,minecraft:pink_wool",
            "WOOL:7,minecraft:gray_wool",
            "WOOL:8,minecraft:light_gray_wool",
            "WOOL:9,minecraft:cyan_wool",
            "WOOL:10,minecraft:purple_wool",
            "WOOL:11,minecraft:blue_wool",
            "WOOL:12,minecraft:brown_wool",
            "WOOL:13,minecraft:green_wool",
            "WOOL:14,minecraft:red_wool",
            "WOOL:15,minecraft:black_wool",

            "LOG,minecraft:birch_log",
            "LOG:0,minecraft:oak_log",
            "LOG:1,minecraft:spruce_log",
            "LOG:2,minecraft:birch_log",
            "LOG:3,minecraft:jungle_log",

            "STONE,minecraft:stone",
            "STONE:0,minecraft:stone",
            "STONE:1,minecraft:granite",
            "STONE:2,minecraft:polished_granite",
            "STONE:3,minecraft:diorite",
            "STONE:4,minecraft:polished_diorite",
            "STONE:5,minecraft:andesite",
            "STONE:6,minecraft:polished_andesite",

            "DIRT,minecraft:dirt",
            "DIRT:0,minecraft:dirt",
            "DIRT:1,minecraft:coarse_dirt",
            "DIRT:2,minecraft:podzol",

            "SKULL,minecraft:creeper_head",
            "SKULL:0,minecraft:skeleton_skull",
            "SKULL:1,minecraft:wither_skeleton_skull",
            "SKULL:2,minecraft:zombie_head",
            "SKULL:3,minecraft:player_head",
            "SKULL_ITEM,minecraft:creeper_head",
            "SKULL_ITEM:0,minecraft:skeleton_skull",
            "SKULL_ITEM:1,minecraft:wither_skeleton_skull",
            "SKULL_ITEM:2,minecraft:zombie_head",
            "SKULL_ITEM:3,minecraft:player_head",

            "DIAMOND_SWORD,minecraft:diamond_sword",
            "GOLD_PICKAXE,minecraft:golden_pickaxe",
            "STONE_PICKAXE,minecraft:stone_pickaxe",
            "GOLDEN_APPLE,minecraft:golden_apple",
            "IRON_INGOT,minecraft:iron_ingot",
            "REDSTONE_COMPARATOR,minecraft:comparator",
            "WATCH,minecraft:clock",
            "MONSTER_EGG:56,minecraft:ghast_spawn_egg",
            "SPECKLED_MELON,minecraft:glistering_melon_slice",
            "SULPHUR,minecraft:gunpowder",
            "CONCRETE:13,minecraft:green_concrete",
            "LEASH,minecraft:lead",
            "POTATO_ITEM,minecraft:potato",
    })
    void testMappingLegacyMaterials(String from, String expected) {
        assertEquals(expected, service.toMojangNamespacedKey(from).orElse(null));
    }
}