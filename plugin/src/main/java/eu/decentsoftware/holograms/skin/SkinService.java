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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import eu.decentsoftware.holograms.api.utils.Log;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Service for retrieving player skins.
 * This service fetches skin textures from external sources based on player names.
 * It uses a {@link SkinSource} to retrieve the skin data.
 *
 * @author d0by
 * @since 2.9.6
 */
public class SkinService {

    private static final Cache<String, String> textureCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build();
    private final SkinSource skinSource;

    /**
     * Constructs a new SkinService with the specified skin source.
     *
     * @param skinSource The source from which to fetch skin textures.
     * @throws NullPointerException if skinSource is null.
     */
    public SkinService(@NotNull SkinSource skinSource) {
        Objects.requireNonNull(skinSource, "skinSource cannot be null");
        this.skinSource = skinSource;
    }

    /**
     * Retrieves the skin texture for a given player name.
     *
     * @param playerName The name of the player whose skin texture is to be fetched.
     * @return The skin texture as a string, or null if the texture could not be fetched.
     * @throws NullPointerException if playerName is null.
     */
    public String getSkinTextureByPlayerName(@NotNull String playerName) {
        Objects.requireNonNull(playerName, "playerName cannot be null");

        String cachedTexture = textureCache.getIfPresent(playerName);
        if (cachedTexture != null) {
            return cachedTexture;
        }

        try {
            String fetchedTexture = skinSource.fetchSkinTextureByPlayerName(playerName);
            textureCache.put(playerName, fetchedTexture);
            return fetchedTexture;
        } catch (SkinSourceException e) {
            Log.warn(e.getMessage());
        } catch (Exception e) {
            Log.error("An unexpected error occurred while fetching skin texture for player '%s'.", e, playerName);
        }

        return null;
    }
}
