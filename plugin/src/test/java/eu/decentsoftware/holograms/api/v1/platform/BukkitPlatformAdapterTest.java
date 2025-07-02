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

package eu.decentsoftware.holograms.api.v1.platform;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BukkitPlatformAdapterTest {

    @Mock
    private Player platformPlayer;
    @Mock
    private ItemStack platformItemStack;
    private BukkitPlatformAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BukkitPlatformAdapter();
    }

    private static Object[] provideInvalidPlatformPlayer() {
        return new Object[]{
                null,
                new Object() // any object
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPlatformPlayer")
    void testGetGenericPlayer_invalidPlayer(Object invalidPlayer) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.getGenericPlayer(invalidPlayer));

        assertEquals("platformPlayer must be a Bukkit Player", exception.getMessage());
    }

    @Test
    void testGetGenericPlayer() {
        DecentPlayer decentPlayer = adapter.getGenericPlayer(platformPlayer);

        assertNotNull(decentPlayer);
        assertEquals(platformPlayer, decentPlayer.getPlatformPlayer());
    }

    private static Object[] provideInvalidPlatformItemStack() {
        return new Object[]{
                null,
                new Object() // any object
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPlatformItemStack")
    void testGetGenericItemStack_invalidItemStack(Object invalidItemStack) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> adapter.getGenericItemStack(invalidItemStack));

        assertEquals("platformItemStack must be a Bukkit ItemStack", exception.getMessage());
    }

    @Test
    void testGetGenericItemStack() {
        DecentItemStack decentItemStack = adapter.getGenericItemStack(platformItemStack);

        assertNotNull(decentItemStack);
        assertEquals(platformItemStack, decentItemStack.getPlatformItemStack());
    }
}