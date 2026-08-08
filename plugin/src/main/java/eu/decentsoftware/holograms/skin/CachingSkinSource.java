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
import java.util.concurrent.TimeUnit;

/**
 * A {@link SkinSource} that caches successful lookups from another source.
 *
 * <p>Failed lookups are not cached — the exception is propagated and the next call will hit
 * the delegate again.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public class CachingSkinSource implements SkinSource {

    private static final long DEFAULT_TTL_HOURS = 1;

    private final SkinSource delegate;
    private final ExpiringCache<String, String> cache;

    /**
     * Constructs a caching source that remembers textures for one hour.
     *
     * @param delegate The source to fetch from on a cache miss.
     * @throws NullPointerException if delegate is null.
     */
    public CachingSkinSource(@NotNull SkinSource delegate) {
        this(delegate, new ExpiringCache<>(DEFAULT_TTL_HOURS, TimeUnit.HOURS));
    }

    /**
     * Constructs a caching source with a custom cache.
     *
     * @param delegate The source to fetch from on a cache miss.
     * @param cache    The cache to store textures in.
     * @throws NullPointerException if delegate or cache is null.
     */
    public CachingSkinSource(@NotNull SkinSource delegate, @NotNull ExpiringCache<String, String> cache) {
        Objects.requireNonNull(delegate, "delegate cannot be null");
        Objects.requireNonNull(cache, "cache cannot be null");
        this.delegate = delegate;
        this.cache = cache;
    }

    @NotNull
    @Override
    public String fetchSkinTextureByPlayerName(@NotNull String playerName) {
        Objects.requireNonNull(playerName, "playerName cannot be null");

        String cachedTexture = cache.getIfPresent(playerName);
        if (cachedTexture != null) {
            return cachedTexture;
        }

        String fetchedTexture = delegate.fetchSkinTextureByPlayerName(playerName);
        cache.put(playerName, fetchedTexture);
        return fetchedTexture;
    }

    /**
     * Discards all cached textures.
     */
    public void invalidateAll() {
        cache.invalidateAll();
    }
}
