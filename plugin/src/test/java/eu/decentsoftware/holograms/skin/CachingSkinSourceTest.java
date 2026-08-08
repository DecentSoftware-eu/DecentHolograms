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

import eu.decentsoftware.holograms.cache.ExpiringCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachingSkinSourceTest {

    @Mock
    private SkinSource delegate;

    private AtomicLong clock;
    private CachingSkinSource source;

    @BeforeEach
    void setUp() {
        clock = new AtomicLong(0);
        source = new CachingSkinSource(delegate, new ExpiringCache<>(1, TimeUnit.HOURS, 16, clock::get));
    }

    @Test
    void testConstruction_nullDelegate() {
        Exception exception = assertThrows(NullPointerException.class, () -> new CachingSkinSource(null));

        assertEquals("delegate cannot be null", exception.getMessage());
    }

    @Test
    void testConstruction_nullCache() {
        Exception exception = assertThrows(NullPointerException.class, () -> new CachingSkinSource(delegate, null));

        assertEquals("cache cannot be null", exception.getMessage());
    }

    @Test
    void testFetchSkinTextureByPlayerName_nullPlayerName() {
        Exception exception = assertThrows(NullPointerException.class, () -> source.fetchSkinTextureByPlayerName(null));

        assertEquals("playerName cannot be null", exception.getMessage());
    }

    @Test
    void testFetchSkinTextureByPlayerName_cachesSuccessfulLookup() {
        when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn("textureData");

        assertEquals("textureData", source.fetchSkinTextureByPlayerName("d0by"));
        assertEquals("textureData", source.fetchSkinTextureByPlayerName("d0by"));
        assertEquals("textureData", source.fetchSkinTextureByPlayerName("d0by"));

        verify(delegate, times(1)).fetchSkinTextureByPlayerName("d0by");
    }

    @Test
    void testFetchSkinTextureByPlayerName_cachesPerPlayer() {
        when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn("textureA");
        when(delegate.fetchSkinTextureByPlayerName("Notch")).thenReturn("textureB");

        assertEquals("textureA", source.fetchSkinTextureByPlayerName("d0by"));
        assertEquals("textureB", source.fetchSkinTextureByPlayerName("Notch"));
        assertEquals("textureA", source.fetchSkinTextureByPlayerName("d0by"));

        verify(delegate, times(1)).fetchSkinTextureByPlayerName("d0by");
        verify(delegate, times(1)).fetchSkinTextureByPlayerName("Notch");
    }

    @Test
    void testFetchSkinTextureByPlayerName_refetchesAfterExpiry() {
        when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn("textureData");

        source.fetchSkinTextureByPlayerName("d0by");
        clock.set(TimeUnit.HOURS.toMillis(1));
        source.fetchSkinTextureByPlayerName("d0by");

        verify(delegate, times(2)).fetchSkinTextureByPlayerName("d0by");
    }

    @Test
    void testFetchSkinTextureByPlayerName_doesNotCacheFailures() {
        when(delegate.fetchSkinTextureByPlayerName("d0by")).thenThrow(new SkinSourceException("boom"));

        assertThrows(SkinSourceException.class, () -> source.fetchSkinTextureByPlayerName("d0by"));
        assertThrows(SkinSourceException.class, () -> source.fetchSkinTextureByPlayerName("d0by"));

        verify(delegate, times(2)).fetchSkinTextureByPlayerName("d0by");
    }

    @Test
    void testInvalidateAll() {
        when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn("textureData");

        source.fetchSkinTextureByPlayerName("d0by");
        source.invalidateAll();
        source.fetchSkinTextureByPlayerName("d0by");

        verify(delegate, times(2)).fetchSkinTextureByPlayerName("d0by");
    }
}
