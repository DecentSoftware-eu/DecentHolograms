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
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * A {@link SkinSource} that remembers lookups already made against another source.
 *
 * <p>Both textures and confirmed absences are cached, so a player who does not exist or has no
 * skin stops generating requests. Absences are held for a shorter time than textures, so that a
 * player who sets a skin is picked up reasonably quickly.</p>
 *
 * <p>Failed lookups are never cached — the {@link SkinSourceException} is propagated and the
 * next call will hit the delegate again.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public class CachingSkinSource implements SkinSource {

    private static final long DEFAULT_TTL_MINUTES = TimeUnit.HOURS.toMinutes(1);
    private static final long DEFAULT_MISS_TTL_MINUTES = 5;

    private final SkinSource delegate;
    private final ExpiringCache<String, Optional<String>> cache;
    private final long missTtlMillis;

    /**
     * Constructs a caching source that remembers textures for an hour and absences for five minutes.
     *
     * @param delegate The source to fetch from on a cache miss.
     * @throws NullPointerException if delegate is null.
     */
    public CachingSkinSource(@NotNull SkinSource delegate) {
        this(delegate, new ExpiringCache<>(DEFAULT_TTL_MINUTES, TimeUnit.MINUTES), DEFAULT_MISS_TTL_MINUTES, TimeUnit.MINUTES);
    }

    /**
     * Constructs a caching source with a custom cache.
     *
     * @param delegate The source to fetch from on a cache miss.
     * @param cache    The cache to store lookups in. Its lifetime applies to found textures.
     * @param missTtl  How long to remember that a player has no skin.
     * @param missUnit The time unit of {@code missTtl}.
     * @throws NullPointerException     if any argument is null.
     * @throws IllegalArgumentException if missTtl is not positive.
     */
    public CachingSkinSource(@NotNull SkinSource delegate,
                             @NotNull ExpiringCache<String, Optional<String>> cache,
                             long missTtl,
                             @NotNull TimeUnit missUnit) {
        Objects.requireNonNull(delegate, "delegate cannot be null");
        Objects.requireNonNull(cache, "cache cannot be null");
        Objects.requireNonNull(missUnit, "missUnit cannot be null");
        if (missTtl <= 0) {
            throw new IllegalArgumentException("missTtl must be positive");
        }
        this.delegate = delegate;
        this.cache = cache;
        this.missTtlMillis = missUnit.toMillis(missTtl);
    }

    @NotNull
    @Override
    public Optional<String> fetchSkinTextureByPlayerName(@NotNull String playerName) {
        Objects.requireNonNull(playerName, "playerName cannot be null");

        Optional<String> cached = cache.getIfPresent(playerName);
        if (cached != null) {
            return cached;
        }

        Optional<String> fetched = delegate.fetchSkinTextureByPlayerName(playerName);
        if (fetched.isPresent()) {
            cache.put(playerName, fetched);
        } else {
            cache.put(playerName, fetched, missTtlMillis, TimeUnit.MILLISECONDS);
        }
        return fetched;
    }

    /**
     * Discards all cached lookups.
     */
    public void invalidateAll() {
        cache.invalidateAll();
    }
}
