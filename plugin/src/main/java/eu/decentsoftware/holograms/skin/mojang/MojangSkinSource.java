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

package eu.decentsoftware.holograms.skin.mojang;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.skin.SkinSource;
import eu.decentsoftware.holograms.skin.SkinSourceException;
import eu.decentsoftware.holograms.url.UrlReader;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of SkinSource that fetches skin textures from Mojang's session server.
 *
 * @author d0by
 * @see <a href="https://minecraft.wiki/w/Mojang_API">Mojang API Documentation</a>
 * @since 1.0.0
 */
public class MojangSkinSource implements SkinSource {

    private static final Gson gson = new GsonBuilder().create();

    @NotNull
    @Override
    public String fetchSkinTextureByPlayerName(@NotNull String playerName) {
        Objects.requireNonNull(playerName, "playerName cannot be null");

        String uuid = fetchUniqueIdByPlayerName(playerName);
        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
            String jsonResponse = UrlReader.readString(url);
            return extractSkinTextureFromJson(playerName, jsonResponse);
        } catch (SkinSourceException e) {
            throw e;
        } catch (IOException e) {
            Log.warn("Failed to fetch skin texture for player %s.", e, playerName);
            throw new SkinSourceException("Failed to fetch skin texture for player " + playerName + ".");
        } catch (Exception e) {
            Log.error("An unexpected error occurred while fetching skin texture for player %s.", e, playerName);
            throw new SkinSourceException("An unexpected error occurred while fetching skin texture for player " + playerName + ".");
        }
    }

    private String fetchUniqueIdByPlayerName(String playerName) {
        try {
            URL url = new URL("https://api.minecraftservices.com/minecraft/profile/lookup/name/" + playerName);
            String jsonResponse = UrlReader.readString(url);
            return extractUniqueIdFromJson(playerName, jsonResponse);
        } catch (SkinSourceException e) {
            throw e;
        } catch (FileNotFoundException e) {
            Log.warn("Cannot fetch skin texture for player %s. Player not found.", playerName);
            throw new SkinSourceException("Player " + playerName + " not found.");
        } catch (IOException e) {
            Log.warn("Failed to fetch unique ID for player %s.", e, playerName);
            throw new SkinSourceException("Failed to fetch unique ID for player " + playerName + ".");
        } catch (Exception e) {
            Log.error("An unexpected error occurred while fetching unique ID for player %s.", e, playerName);
            throw new SkinSourceException("An unexpected error occurred while fetching unique ID for player " + playerName + ".");
        }
    }

    private String extractUniqueIdFromJson(String playerName, String json) {
        try {
            MojangUuidResponse response = gson.fromJson(json, MojangUuidResponse.class);
            String errorMessage = response.getErrorMessage();
            if (errorMessage != null) {
                Log.warn("Error fetching UUID for player: %s. Error message: %s", playerName, errorMessage);
                throw new SkinSourceException(errorMessage);
            }
            String uniqueId = response.getId();
            if (uniqueId != null && !uniqueId.isEmpty()) {
                return uniqueId;
            }
            throw new SkinSourceException("No unique ID found in JSON response: " + json);
        } catch (SkinSourceException e) {
            throw e;
        } catch (JsonSyntaxException e) {
            Log.warn("Failed to parse JSON response: %s", e, json);
            throw new SkinSourceException("Failed to parse JSON response: " + json);
        } catch (Exception e) {
            Log.error("An unexpected error occurred while extracting unique ID from JSON: %s", e, json);
            throw new SkinSourceException("An unexpected error occurred while extracting unique ID from JSON.");
        }
    }

    private String extractSkinTextureFromJson(String playerName, String json) {
        try {
            if (json == null || json.isEmpty()) {
                throw new SkinSourceException("Received empty JSON response while fetching skin texture for player " + playerName + ".");
            }
            MojangProfileResponse response = gson.fromJson(json, MojangProfileResponse.class);
            List<MojangProfileProperty> properties = response.getProperties();
            if (properties == null || properties.isEmpty()) {
                throw new SkinSourceException("No profile properties found in JSON response: " + json);
            }
            for (MojangProfileProperty property : properties) {
                // There should only ever be one property, and it should be "textures",
                // but according to the API documentation, this is "for now"
                // so we check all properties to be safe in case more properties are added in the future.
                if ("textures".equals(property.getName())) {
                    return property.getValue();
                }
            }
            throw new SkinSourceException("No skin texture found in JSON response: " + json);
        } catch (SkinSourceException e) {
            throw e;
        } catch (JsonParseException e) {
            Log.warn("Failed to parse JSON response: %s", e, json);
            throw new SkinSourceException("Failed to parse JSON response: " + json);
        } catch (Exception e) {
            Log.error("An unexpected error occurred while extracting skin texture from JSON: %s", e, json);
            throw new SkinSourceException("An unexpected error occurred while extracting skin texture from JSON.");
        }
    }
}
