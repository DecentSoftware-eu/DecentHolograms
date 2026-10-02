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
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import eu.decentsoftware.holograms.logging.Log;
import eu.decentsoftware.holograms.skin.SkinSource;
import eu.decentsoftware.holograms.skin.SkinSourceException;
import eu.decentsoftware.holograms.url.HttpStatusException;
import eu.decentsoftware.holograms.url.UrlReader;
import org.jetbrains.annotations.NotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Implementation of SkinSource that fetches skin textures from Mojang's session server.
 *
 * <p>A missing player, a missing profile, or a profile without a textures property are all
 * reported as an empty result. Anything that prevented the lookup from completing - network
 * errors, rate limiting, unparseable responses - is reported as a {@link SkinSourceException}
 * so that it is retried rather than remembered.</p>
 *
 * @author d0by
 * @see <a href="https://minecraft.wiki/w/Mojang_API">Mojang API Documentation</a>
 * @since 2.9.6
 */
public class MojangSkinSource implements SkinSource {

    private static final Gson gson = new Gson();
    /**
     * Mojang usernames are alphanumerics and underscores, at most 16 characters. Anything else is
     * rejected by the API, so there is nothing to be gained by asking.
     */
    private static final Pattern VALID_USERNAME = Pattern.compile("^[A-Za-z0-9_]{1,16}$");

    @NotNull
    @Override
    public Optional<String> fetchSkinTextureByPlayerName(@NotNull String playerName) {
        Objects.requireNonNull(playerName, "playerName cannot be null");

        if (!VALID_USERNAME.matcher(playerName).matches()) {
            Log.warn("Cannot fetch skin texture for an invalid player name: '%s'. "
                    + "If this looks like a placeholder, the plugin providing it may be missing.", playerName);
            return Optional.empty();
        }

        Optional<String> uniqueId = fetchUniqueIdByPlayerName(playerName);
        if (!uniqueId.isPresent()) {
            return Optional.empty();
        }

        try {
            URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uniqueId.get());
            String jsonResponse = UrlReader.readString(url);
            return extractSkinTextureFromJson(playerName, jsonResponse);
        } catch (SkinSourceException e) {
            throw e;
        } catch (FileNotFoundException e) {
            // The profile does not exist, even though the name lookup resolved. Nothing to fetch.
            return Optional.empty();
        } catch (HttpStatusException e) {
            if (e.isPermanentRejection()) {
                Log.warn("Cannot fetch skin texture for player %s: %s", playerName, e.getMessage());
                return Optional.empty();
            }
            Log.warn("Failed to fetch skin texture for player %s.", e, playerName);
            throw new SkinSourceException("Failed to fetch skin texture for player " + playerName + ".");
        } catch (IOException e) {
            Log.warn("Failed to fetch skin texture for player %s.", e, playerName);
            throw new SkinSourceException("Failed to fetch skin texture for player " + playerName + ".");
        } catch (Exception e) {
            Log.error("An unexpected error occurred while fetching skin texture for player %s.", e, playerName);
            throw new SkinSourceException("An unexpected error occurred while fetching skin texture for player " + playerName + ".");
        }
    }

    private Optional<String> fetchUniqueIdByPlayerName(String playerName) {
        try {
            URL url = new URL("https://api.minecraftservices.com/minecraft/profile/lookup/name/" + playerName);
            String jsonResponse = UrlReader.readString(url);
            return extractUniqueIdFromJson(playerName, jsonResponse);
        } catch (SkinSourceException e) {
            throw e;
        } catch (FileNotFoundException e) {
            // No such player.
            return Optional.empty();
        } catch (HttpStatusException e) {
            if (e.isPermanentRejection()) {
                Log.warn("Cannot fetch unique ID for player %s: %s", playerName, e.getMessage());
                return Optional.empty();
            }
            Log.warn("Failed to fetch unique ID for player %s.", e, playerName);
            throw new SkinSourceException("Failed to fetch unique ID for player " + playerName + ".");
        } catch (IOException e) {
            Log.warn("Failed to fetch unique ID for player %s.", e, playerName);
            throw new SkinSourceException("Failed to fetch unique ID for player " + playerName + ".");
        } catch (Exception e) {
            Log.error("An unexpected error occurred while fetching unique ID for player %s.", e, playerName);
            throw new SkinSourceException("An unexpected error occurred while fetching unique ID for player " + playerName + ".");
        }
    }

    private Optional<String> extractUniqueIdFromJson(String playerName, String json) {
        try {
            if (json == null || json.isEmpty()) {
                throw new SkinSourceException("Received empty JSON response while fetching unique ID for player " + playerName + ".");
            }
            MojangUuidResponse response = gson.fromJson(json, MojangUuidResponse.class);
            if (response == null) {
                throw new SkinSourceException("Received no usable JSON response while fetching unique ID for player " + playerName + ".");
            }
            String errorMessage = response.getErrorMessage();
            if (errorMessage != null) {
                // The API understood the request and refused it, e.g. unknown or malformed name.
                Log.warn("Error fetching UUID for player: %s. Error message: %s", playerName, errorMessage);
                return Optional.empty();
            }
            String uniqueId = response.getId();
            if (uniqueId == null || uniqueId.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(uniqueId);
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

    private Optional<String> extractSkinTextureFromJson(String playerName, String json) {
        try {
            if (json == null || json.isEmpty()) {
                throw new SkinSourceException("Received empty JSON response while fetching skin texture for player " + playerName + ".");
            }
            MojangProfileResponse response = gson.fromJson(json, MojangProfileResponse.class);
            if (response == null) {
                throw new SkinSourceException("Received no usable JSON response while fetching skin texture for player " + playerName + ".");
            }
            List<MojangProfileProperty> properties = response.getProperties();
            if (properties == null || properties.isEmpty()) {
                return Optional.empty();
            }
            for (MojangProfileProperty property : properties) {
                // There should only ever be one property, and it should be "textures",
                // but according to the API documentation, this is "for now"
                // so we check all properties to be safe in case more properties are added in the future.
                if (property != null && "textures".equals(property.getName())) {
                    String value = property.getValue();
                    return value == null || value.isEmpty() ? Optional.empty() : Optional.of(value);
                }
            }
            return Optional.empty();
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
