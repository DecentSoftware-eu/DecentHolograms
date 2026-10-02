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

import eu.decentsoftware.holograms.skin.SkinSourceException;
import eu.decentsoftware.holograms.url.HttpStatusException;
import eu.decentsoftware.holograms.url.UrlReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class MojangSkinSourceTest {

    private MojangSkinSource skinSource;

    @BeforeEach
    void setUp() {
        skinSource = new MojangSkinSource();
    }

    @Test
    void testFetchSkinTextureByPlayerName() {
        String playerName = "d0by";
        String uuidJson = "{\"id\": \"fd8070f3d722429b9fdea09d21bf4375\"}";
        String textureJson = "{\"properties\": [{\"name\": \"textures\", \"value\": \"base64texture\"}]}";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(uuidJson);
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/profile/fd8070f3d722429b9fdea09d21bf4375"))))
                    .thenReturn(textureJson);

            Optional<String> result = skinSource.fetchSkinTextureByPlayerName(playerName);

            assertEquals(Optional.of("base64texture"), result);
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_uuidApiIOException() {
        String playerName = "d0by";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(any(URL.class))).thenThrow(new IOException("Network error"));

            SkinSourceException exception = assertThrows(SkinSourceException.class,
                    () -> skinSource.fetchSkinTextureByPlayerName(playerName));

            assertEquals("Failed to fetch unique ID for player " + playerName + ".", exception.getMessage());
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_uuidApiFileNotFoundException() {
        String playerName = "d0by";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(any(URL.class))).thenThrow(new FileNotFoundException("Player not found"));

            // No such player is a definitive answer, not a failure.
            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_textureApiFileNotFoundException() {
        String playerName = "d0by";
        String fakeUUIDJson = "{\"id\": \"fd8070f3d722429b9fdea09d21bf4375\"}";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(fakeUUIDJson);
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/profile/fd8070f3d722429b9fdea09d21bf4375"))))
                    .thenThrow(new FileNotFoundException("Profile not found"));

            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_uuidApiMalformedJson() {
        String playerName = "d0by";
        String malformedJson = "{\"id\" \"fd8070f3d722429b9fdea09d21bf4375\"}"; // missing colon

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(malformedJson);

            SkinSourceException exception = assertThrows(SkinSourceException.class,
                    () -> skinSource.fetchSkinTextureByPlayerName(playerName));

            assertEquals("Failed to parse JSON response: " + malformedJson, exception.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"id\": \"\"}",
            "{}",
    })
    void testFetchSkinTextureByPlayerName_uuidApiInvalidUuid(String invalidUuidJson) {
        String playerName = "d0by";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(invalidUuidJson);

            // A well-formed response with no id means there is nothing to look up.
            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_textureApiMalformedJson() {
        String playerName = "d0by";
        String fakeUUIDJson = "{\"id\": \"fd8070f3d722429b9fdea09d21bf4375\"}";
        String malformedJson = "{\"invalid\": true"; // missing closing brace

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(fakeUUIDJson);
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/profile/fd8070f3d722429b9fdea09d21bf4375"))))
                    .thenReturn(malformedJson);

            SkinSourceException exception = assertThrows(SkinSourceException.class,
                    () -> skinSource.fetchSkinTextureByPlayerName(playerName));

            assertEquals("Failed to parse JSON response: " + malformedJson, exception.getMessage());
        }
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {""})
    void testFetchSkinTextureByPlayerName_emptyResponse(String emptyResponse) {
        String playerName = "d0by";
        String fakeUUIDJson = "{\"id\": \"fd8070f3d722429b9fdea09d21bf4375\"}";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(fakeUUIDJson);
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/profile/fd8070f3d722429b9fdea09d21bf4375"))))
                    .thenReturn(emptyResponse);

            SkinSourceException exception = assertThrows(SkinSourceException.class,
                    () -> skinSource.fetchSkinTextureByPlayerName(playerName));

            assertEquals("Received empty JSON response while fetching skin texture for player " + playerName + ".", exception.getMessage());
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_textureApiIOException() {
        String playerName = "d0by";
        String fakeUUIDJson = "{\"id\": \"fd8070f3d722429b9fdea09d21bf4375\"}";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(fakeUUIDJson);
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/profile/fd8070f3d722429b9fdea09d21bf4375"))))
                    .thenThrow(new IOException("Network error"));

            SkinSourceException exception = assertThrows(SkinSourceException.class,
                    () -> skinSource.fetchSkinTextureByPlayerName(playerName));

            assertEquals("Failed to fetch skin texture for player " + playerName + ".", exception.getMessage());
        }
    }

    @Test
    void testExtractUniqueIdFromJson_withErrorMessage() {
        String playerName = "InvalidUser";
        String json = "{\"errorMessage\": \"Player not found\"}";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(json);

            // The API understood the request and refused it, so there is no skin to find.
            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"properties\": []}",
            "{}",
    })
    void testExtractSkinTextureFromJson_noProperties(String invalidTextureJson) {
        String playerName = "d0by";
        String uuidJson = "{\"id\": \"deadbeef\"}";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(uuidJson);
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/profile/deadbeef"))))
                    .thenReturn(invalidTextureJson);

            // The profile exists but carries no properties, so the player has no skin.
            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"properties\": [{\"name\": \"something_else\", \"value\": \"xyz\"}]}",
            "{\"properties\": [{\"name\": \"textures\"}]}",
            "{\"properties\": [{\"name\": \"textures\", \"value\": \"\"}]}",
    })
    void testExtractSkinTextureFromJson_noUsableTexture(String textureJson) {
        String playerName = "d0by";
        String uuidJson = "{\"id\": \"deadbeef\"}";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/lookup/name/" + playerName))))
                    .thenReturn(uuidJson);
            urlReaderMock.when(() -> UrlReader.readString(argThat(url -> url.toString().contains("/profile/deadbeef"))))
                    .thenReturn(textureJson);

            // A missing or blank textures value means no skin, not a broken lookup.
            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "%player_name%",       // unresolved PlaceholderAPI placeholder
            "{player}",            // unresolved internal placeholder
            "name with spaces",
            "toolongusernameforminecraft",
            "",
    })
    void testFetchSkinTextureByPlayerName_impossibleName(String playerName) {
        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            // No name of this shape can exist, so Mojang answers 400. Asking anyway costs a round
            // trip on every hologram update, which is what this avoids.
            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());

            urlReaderMock.verifyNoInteractions();
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_rejectedRequestIsAnAnswer() {
        String playerName = "d0by";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(any(URL.class)))
                    .thenThrow(new HttpStatusException(400, "Server returned HTTP 400"));

            // Empty rather than thrown, so CachingSkinSource remembers it instead of retrying
            // every tick.
            assertFalse(skinSource.fetchSkinTextureByPlayerName(playerName).isPresent());
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_rateLimitIsNotAnAnswer() {
        String playerName = "d0by";

        try (MockedStatic<UrlReader> urlReaderMock = mockStatic(UrlReader.class)) {
            urlReaderMock.when(() -> UrlReader.readString(any(URL.class)))
                    .thenThrow(new HttpStatusException(429, "Server returned HTTP 429"));

            // Caching a rate limit as "no skin" would blank the skin for an hour.
            assertThrows(SkinSourceException.class, () -> skinSource.fetchSkinTextureByPlayerName(playerName));
        }
    }

    @Test
    void testFetchSkinTextureByPlayerName_nullPlayerName() {
        assertThrows(NullPointerException.class, () -> skinSource.fetchSkinTextureByPlayerName(null));
    }
}