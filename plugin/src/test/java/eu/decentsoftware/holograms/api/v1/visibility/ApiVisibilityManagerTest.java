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

package eu.decentsoftware.holograms.api.v1.visibility;

import eu.decentsoftware.holograms.api.v1.platform.GenericPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiVisibilityManagerTest {

    private eu.decentsoftware.holograms.api.v1.visibility.ApiVisibilityManager visibilityManager;

    @BeforeEach
    void setUp() {
        visibilityManager = new eu.decentsoftware.holograms.api.v1.visibility.ApiVisibilityManager();
    }

    @Test
    void testDefaultVisibility() {
        assertEquals(Visibility.VISIBLE, visibilityManager.getDefaultVisibility());
    }

    @Test
    void testSetDefaultVisibility_null() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> visibilityManager.setDefaultVisibility(null));

        assertEquals("visibility cannot be null", exception.getMessage());
    }

    @ParameterizedTest
    @EnumSource(Visibility.class)
    void testSetDefaultVisibility(Visibility visibility) {
        visibilityManager.setDefaultVisibility(visibility);

        assertEquals(visibility, visibilityManager.getDefaultVisibility());
    }

    private static Object[][] provideTestIsVisibleByDefault() {
        return new Object[][]{
                {Visibility.VISIBLE, true},
                {Visibility.HIDDEN, false},
        };
    }

    @ParameterizedTest
    @MethodSource("provideTestIsVisibleByDefault")
    void testIsVisibleByDefault(Visibility visibility, boolean expectedResult) {
        visibilityManager.setDefaultVisibility(visibility);

        assertEquals(expectedResult, visibilityManager.isVisibleByDefault());
    }

    @Test
    void testDefaultPlayerVisibility() {
        GenericPlayer player = mockPlayer();

        assertNull(visibilityManager.getPlayerVisibility(player));
    }

    @ParameterizedTest
    @NullSource
    @EnumSource(Visibility.class)
    void testSetPlayerVisibility(Visibility visibility) {
        GenericPlayer player1 = mockPlayer();
        GenericPlayer player2 = mockPlayer();

        visibilityManager.setPlayerVisibility(player1, visibility);

        assertEquals(visibility, visibilityManager.getPlayerVisibility(player1));
        // Another player should not have visibility set
        assertNull(visibilityManager.getPlayerVisibility(player2));
    }

    @Test
    void testResetPlayerVisibility() {
        GenericPlayer player = mockPlayer();

        visibilityManager.setPlayerVisibility(player, Visibility.VISIBLE);
        assertEquals(Visibility.VISIBLE, visibilityManager.getPlayerVisibility(player));

        visibilityManager.resetPlayerVisibility(player);
        assertNull(visibilityManager.getPlayerVisibility(player));
    }

    private static Object[][] provideTestIsVisibleTo() {
        return new Object[][]{
                // Default visibility | Player visibility | Expected result
                {Visibility.VISIBLE, Visibility.VISIBLE, true},
                {Visibility.VISIBLE, Visibility.HIDDEN, false},
                {Visibility.VISIBLE, null, true},
                {Visibility.HIDDEN, Visibility.VISIBLE, true},
                {Visibility.HIDDEN, Visibility.HIDDEN, false},
                {Visibility.HIDDEN, null, false},
        };
    }

    @ParameterizedTest
    @MethodSource("provideTestIsVisibleTo")
    void testIsVisibleTo(Visibility defaultVisibility, Visibility playerVisibility, boolean expectedResult) {
        GenericPlayer player = mockPlayer();

        visibilityManager.setDefaultVisibility(defaultVisibility);
        visibilityManager.setPlayerVisibility(player, playerVisibility);

        boolean isVisible = visibilityManager.isVisibleTo(player);

        assertEquals(expectedResult, isVisible);
    }

    private static GenericPlayer mockPlayer() {
        GenericPlayer player = mock(GenericPlayer.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        return player;
    }
}