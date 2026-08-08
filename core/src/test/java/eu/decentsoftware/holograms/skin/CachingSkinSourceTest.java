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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CachingSkinSourceTest {

    private static final long HIT_TTL_MINUTES = 60;
    private static final long MISS_TTL_MINUTES = 5;

    @Mock
    private SkinSource delegate;

    private AtomicLong clock;
    private CachingSkinSource source;

    @BeforeEach
    void setUp() {
        clock = new AtomicLong(0);
        ExpiringCache<String, Optional<String>> cache = new ExpiringCache<>(HIT_TTL_MINUTES, TimeUnit.MINUTES, 16, clock::get);
        source = new CachingSkinSource(delegate, cache, MISS_TTL_MINUTES, TimeUnit.MINUTES);
    }

    @Nested
    class ConstructionTests {

        @Test
        void rejectsNullDelegate() {
            Exception exception = assertThrows(NullPointerException.class, () -> new CachingSkinSource(null));

            assertEquals("delegate cannot be null", exception.getMessage());
        }

        @Test
        void rejectsNullCache() {
            Exception exception = assertThrows(NullPointerException.class,
                    () -> new CachingSkinSource(delegate, null, MISS_TTL_MINUTES, TimeUnit.MINUTES));

            assertEquals("cache cannot be null", exception.getMessage());
        }

        @Test
        void rejectsNonPositiveMissTtl() {
            ExpiringCache<String, Optional<String>> cache = new ExpiringCache<>(1, TimeUnit.MINUTES);

            assertThrows(IllegalArgumentException.class,
                    () -> new CachingSkinSource(delegate, cache, 0, TimeUnit.MINUTES));
        }

        @Test
        void rejectsNullPlayerName() {
            Exception exception = assertThrows(NullPointerException.class,
                    () -> source.fetchSkinTextureByPlayerName(null));

            assertEquals("playerName cannot be null", exception.getMessage());
        }
    }

    @Nested
    class FoundTextureTests {

        @Test
        void cachesTexture() {
            when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn(Optional.of("textureData"));

            assertEquals(Optional.of("textureData"), source.fetchSkinTextureByPlayerName("d0by"));
            assertEquals(Optional.of("textureData"), source.fetchSkinTextureByPlayerName("d0by"));
            assertEquals(Optional.of("textureData"), source.fetchSkinTextureByPlayerName("d0by"));

            verify(delegate, times(1)).fetchSkinTextureByPlayerName("d0by");
        }

        @Test
        void cachesPerPlayer() {
            when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn(Optional.of("textureA"));
            when(delegate.fetchSkinTextureByPlayerName("Notch")).thenReturn(Optional.of("textureB"));

            assertEquals(Optional.of("textureA"), source.fetchSkinTextureByPlayerName("d0by"));
            assertEquals(Optional.of("textureB"), source.fetchSkinTextureByPlayerName("Notch"));
            assertEquals(Optional.of("textureA"), source.fetchSkinTextureByPlayerName("d0by"));

            verify(delegate, times(1)).fetchSkinTextureByPlayerName("d0by");
            verify(delegate, times(1)).fetchSkinTextureByPlayerName("Notch");
        }

        @Test
        void refetchesOnceTextureExpires() {
            when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn(Optional.of("textureData"));

            source.fetchSkinTextureByPlayerName("d0by");
            clock.set(TimeUnit.MINUTES.toMillis(HIT_TTL_MINUTES));
            source.fetchSkinTextureByPlayerName("d0by");

            verify(delegate, times(2)).fetchSkinTextureByPlayerName("d0by");
        }
    }

    @Nested
    class AbsentTextureTests {

        @Test
        void cachesAbsence() {
            when(delegate.fetchSkinTextureByPlayerName("Ghost")).thenReturn(Optional.empty());

            assertFalse(source.fetchSkinTextureByPlayerName("Ghost").isPresent());
            assertFalse(source.fetchSkinTextureByPlayerName("Ghost").isPresent());
            assertFalse(source.fetchSkinTextureByPlayerName("Ghost").isPresent());

            verify(delegate, times(1)).fetchSkinTextureByPlayerName("Ghost");
        }

        @Test
        void absenceExpiresSoonerThanTexture() {
            when(delegate.fetchSkinTextureByPlayerName("Ghost")).thenReturn(Optional.empty());

            source.fetchSkinTextureByPlayerName("Ghost");
            clock.set(TimeUnit.MINUTES.toMillis(MISS_TTL_MINUTES) - 1);
            source.fetchSkinTextureByPlayerName("Ghost");
            verify(delegate, times(1)).fetchSkinTextureByPlayerName("Ghost");

            clock.set(TimeUnit.MINUTES.toMillis(MISS_TTL_MINUTES));
            source.fetchSkinTextureByPlayerName("Ghost");
            verify(delegate, times(2)).fetchSkinTextureByPlayerName("Ghost");
        }

        @Test
        void picksUpTextureSetAfterAbsenceExpires() {
            when(delegate.fetchSkinTextureByPlayerName("Ghost"))
                    .thenReturn(Optional.empty())
                    .thenReturn(Optional.of("textureData"));

            assertFalse(source.fetchSkinTextureByPlayerName("Ghost").isPresent());
            clock.set(TimeUnit.MINUTES.toMillis(MISS_TTL_MINUTES));

            assertEquals(Optional.of("textureData"), source.fetchSkinTextureByPlayerName("Ghost"));
        }
    }

    @Nested
    class FailedLookupTests {

        @Test
        void doesNotCacheFailures() {
            when(delegate.fetchSkinTextureByPlayerName("d0by")).thenThrow(new SkinSourceException("boom"));

            assertThrows(SkinSourceException.class, () -> source.fetchSkinTextureByPlayerName("d0by"));
            assertThrows(SkinSourceException.class, () -> source.fetchSkinTextureByPlayerName("d0by"));

            verify(delegate, times(2)).fetchSkinTextureByPlayerName("d0by");
        }

        @Test
        void recoversOnceLookupSucceeds() {
            when(delegate.fetchSkinTextureByPlayerName("d0by"))
                    .thenThrow(new SkinSourceException("boom"))
                    .thenReturn(Optional.of("textureData"));

            assertThrows(SkinSourceException.class, () -> source.fetchSkinTextureByPlayerName("d0by"));

            assertEquals(Optional.of("textureData"), source.fetchSkinTextureByPlayerName("d0by"));
        }

        @Test
        void doesNotEvictAnAlreadyCachedTexture() {
            when(delegate.fetchSkinTextureByPlayerName("d0by"))
                    .thenReturn(Optional.of("textureData"))
                    .thenThrow(new SkinSourceException("boom"));

            assertEquals(Optional.of("textureData"), source.fetchSkinTextureByPlayerName("d0by"));
            clock.set(TimeUnit.MINUTES.toMillis(HIT_TTL_MINUTES) - 1);

            assertEquals(Optional.of("textureData"), source.fetchSkinTextureByPlayerName("d0by"));
            verify(delegate, times(1)).fetchSkinTextureByPlayerName("d0by");
        }
    }

    @Test
    void invalidateAllDropsEverything() {
        when(delegate.fetchSkinTextureByPlayerName("d0by")).thenReturn(Optional.of("textureData"));

        source.fetchSkinTextureByPlayerName("d0by");
        source.invalidateAll();
        source.fetchSkinTextureByPlayerName("d0by");

        verify(delegate, times(2)).fetchSkinTextureByPlayerName("d0by");
    }
}
