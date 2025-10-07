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

package eu.decentsoftware.holograms.api.utils.entity;

import com.cryptomorin.xseries.XEntityType;
import org.bukkit.entity.EntityType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DecentEntityTypeTest {

    private static XEntityType[] provideBlacklistedXEntityTypes() {
        return new XEntityType[]{
                XEntityType.AREA_EFFECT_CLOUD,
                XEntityType.BLOCK_DISPLAY,
                XEntityType.CHEST_MINECART,
                XEntityType.COMMAND_BLOCK_MINECART,
                XEntityType.DRAGON_FIREBALL,
                XEntityType.EVOKER_FANGS,
                XEntityType.EXPERIENCE_ORB,
                XEntityType.FALLING_BLOCK,
                XEntityType.FIREBALL,
                XEntityType.FIREWORK_ROCKET,
                XEntityType.FISHING_BOBBER,
                XEntityType.FURNACE_MINECART,
                XEntityType.GLOW_ITEM_FRAME,
                XEntityType.HOPPER_MINECART,
                XEntityType.INTERACTION,
                XEntityType.ITEM,
                XEntityType.ITEM_DISPLAY,
                XEntityType.ITEM_FRAME,
                XEntityType.LEASH_KNOT,
                XEntityType.LIGHTNING_BOLT,
                XEntityType.MARKER,
                XEntityType.MINECART,
                XEntityType.PAINTING,
                XEntityType.PLAYER,
                XEntityType.SMALL_FIREBALL,
                XEntityType.SPAWNER_MINECART,
                XEntityType.TEXT_DISPLAY,
                XEntityType.TNT_MINECART,
                XEntityType.TNT,
                XEntityType.UNKNOWN,
                XEntityType.WITHER_SKULL,
                XEntityType.ACACIA_BOAT,
                XEntityType.ACACIA_CHEST_BOAT,
                XEntityType.BIRCH_BOAT,
                XEntityType.BIRCH_CHEST_BOAT,
                XEntityType.CHERRY_BOAT,
                XEntityType.CHERRY_CHEST_BOAT,
                XEntityType.DARK_OAK_BOAT,
                XEntityType.DARK_OAK_CHEST_BOAT,
                XEntityType.JUNGLE_BOAT,
                XEntityType.JUNGLE_CHEST_BOAT,
                XEntityType.MANGROVE_BOAT,
                XEntityType.MANGROVE_CHEST_BOAT,
                XEntityType.OAK_BOAT,
                XEntityType.OAK_CHEST_BOAT,
                XEntityType.PALE_OAK_BOAT,
                XEntityType.PALE_OAK_CHEST_BOAT,
                XEntityType.SPRUCE_BOAT,
                XEntityType.SPRUCE_CHEST_BOAT
        };
    }

    @Test
    void testGetAllowedEntityTypeNames() {
        List<String> allowedEntityNames = DecentEntityType.getAllowedEntityTypeNames();

        assertNotNull(allowedEntityNames, "Allowed entity names list should not be null.");
        assertFalse(allowedEntityNames.isEmpty(), "Allowed entity names list should not be empty.");

        for (XEntityType blacklistedType : provideBlacklistedXEntityTypes()) {
            EntityType entityType = blacklistedType.get();
            if (entityType != null) {
                assertFalse(allowedEntityNames.contains(entityType.name()),
                        "Blacklisted entity type " + entityType.name() + " should not be present in the allowed list.");
            }
        }

        Set<XEntityType> blacklistedTypes = new HashSet<>(Arrays.asList(provideBlacklistedXEntityTypes()));
        Set<XEntityType> allTypes = new HashSet<>(getAllXEntityTypes());

        for (XEntityType type : allTypes) {
            EntityType entityType = type.get();
            if (entityType != null && !blacklistedTypes.contains(type)) {
                assertTrue(allowedEntityNames.contains(entityType.name()),
                        "Non-blacklisted entity type " + entityType.name() + " should be present in the allowed list.");
            }
        }
    }

    private static List<XEntityType> getAllXEntityTypes() {
        return Arrays.stream(XEntityType.values())
                .collect(Collectors.toList());
    }

    @Test
    void testParseEntityType_nullInput() {
        assertNull(DecentEntityType.parseEntityType(null));
    }

    @Test
    void testParseEntityType_invalidInput() {
        // Invalid entity type
        assertNull(DecentEntityType.parseEntityType("INVALID_ENTITY"));
        // Blacklisted entity type
        assertNull(DecentEntityType.parseEntityType("AREA_EFFECT_CLOUD"));
    }

    @Test
    void testParseEntityType_onlyKnownAsXEntityType() {
        EntityType result = DecentEntityType.parseEntityType("EXPERIENCE_BOTTLE");

        assertNotNull(result);
        assertEquals(EntityType.THROWN_EXP_BOTTLE, result);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ZOMBIE",
            "zombie",
            "minecraft:zombie",
    })
    void testParseEntityType_validInput() {
        EntityType result = DecentEntityType.parseEntityType("ZOMBIE");

        assertNotNull(result);
        assertEquals(EntityType.ZOMBIE, result);
    }
}