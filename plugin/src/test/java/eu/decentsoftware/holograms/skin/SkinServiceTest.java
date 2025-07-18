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

package eu.decentsoftware.holograms.skin;

import eu.decentsoftware.holograms.api.utils.Log;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SkinServiceTest {

    @Mock
    private SkinSource source;
    @InjectMocks
    private SkinService service;

    @BeforeAll
    static void beforeAll() {
        Log.initializeForTests();
    }

    @Test
    void testConstruction() {
        Exception exception = assertThrows(NullPointerException.class, () -> new SkinService(null));

        assertEquals("skinSource cannot be null", exception.getMessage());
    }

    @Test
    void testGetSkinTextureByPlayerName_nullPlayerName() {
        Exception exception = assertThrows(NullPointerException.class, () -> service.getSkinTextureByPlayerName(null));

        assertEquals("playerName cannot be null", exception.getMessage());
    }

    @Test
    void testGetSkinTextureByPlayerName() {
        String playerName = "testPlayer1";
        String expectedTexture = "textureData";

        when(source.fetchSkinTextureByPlayerName(playerName)).thenReturn(expectedTexture);

        assertEquals(expectedTexture, service.getSkinTextureByPlayerName(playerName));
        // Future calls should return the cached texture
        assertEquals(expectedTexture, service.getSkinTextureByPlayerName(playerName));
        assertEquals(expectedTexture, service.getSkinTextureByPlayerName(playerName));

        verify(source, times(1)).fetchSkinTextureByPlayerName(playerName);
    }

    private static Object[] provideExceptions() {
        return new Object[]{
                new SkinSourceException("Error fetching skin"),
                new RuntimeException("Unexpected error")
        };
    }

    @ParameterizedTest
    @MethodSource("provideExceptions")
    void testGetSkinTextureByPlayerName_SkinSourceException(Exception exception) {
        String playerName = "testPlayer2";

        when(source.fetchSkinTextureByPlayerName(playerName)).thenThrow(exception);

        String result = service.getSkinTextureByPlayerName(playerName);

        assertNull(result);
        verify(source, times(1)).fetchSkinTextureByPlayerName(playerName);
    }
}