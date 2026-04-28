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

package eu.decentsoftware.holograms.api.utils;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.integration.Integration;
import eu.decentsoftware.holograms.integration.IntegrationAvailabilityService;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PAPITest {

    @Mock
    private DecentHolograms plugin;
    @Mock
    private IntegrationAvailabilityService integrationAvailabilityService;
    @Mock
    private Player player;

    @BeforeAll
    static void beforeAll() {
        Log.initializeForTests();
    }

    @Test
    void testSetPlaceholders_PlaceholderAPINotEnabled() {
        try (MockedStatic<DecentHologramsAPI> mockedDecentHologramsAPI = mockStatic(DecentHologramsAPI.class);
             MockedStatic<PlaceholderAPI> mockedPlaceholderAPI = mockStatic(PlaceholderAPI.class)) {
            mockedDecentHologramsAPI.when(DecentHologramsAPI::get).thenReturn(plugin);
            when(plugin.getIntegrationAvailabilityService()).thenReturn(integrationAvailabilityService);
            when(integrationAvailabilityService.isIntegrationAvailable(Integration.PLACEHOLDER_API))
                    .thenReturn(false);

            String result = PAPI.setPlaceholders(player, "Test %placeholder%");

            assertEquals("Test %placeholder%", result);
            mockedPlaceholderAPI.verifyNoInteractions();
        }
    }

    @Test
    void testSetPlaceholders_PlaceholderAPIThrows() {
        try (MockedStatic<DecentHologramsAPI> mockedDecentHologramsAPI = mockStatic(DecentHologramsAPI.class);
             MockedStatic<PlaceholderAPI> mockedPlaceholderAPI = mockStatic(PlaceholderAPI.class)) {
            mockedDecentHologramsAPI.when(DecentHologramsAPI::get).thenReturn(plugin);
            when(plugin.getIntegrationAvailabilityService()).thenReturn(integrationAvailabilityService);
            when(integrationAvailabilityService.isIntegrationAvailable(Integration.PLACEHOLDER_API))
                    .thenReturn(true);
            mockedPlaceholderAPI.when(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"))
                    .thenThrow(new RuntimeException("PlaceholderAPI exception"));

            String result = PAPI.setPlaceholders(player, "Test %placeholder%");

            assertEquals("Test %placeholder%", result);
            mockedPlaceholderAPI.verify(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"));
        }
    }

    @Test
    void testSetPlaceholders() {
        try (MockedStatic<DecentHologramsAPI> mockedDecentHologramsAPI = mockStatic(DecentHologramsAPI.class);
             MockedStatic<PlaceholderAPI> mockedPlaceholderAPI = mockStatic(PlaceholderAPI.class)) {
            mockedDecentHologramsAPI.when(DecentHologramsAPI::get).thenReturn(plugin);
            when(plugin.getIntegrationAvailabilityService()).thenReturn(integrationAvailabilityService);
            when(integrationAvailabilityService.isIntegrationAvailable(Integration.PLACEHOLDER_API))
                    .thenReturn(true);
            mockedPlaceholderAPI.when(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"))
                    .thenReturn("Test replaced");

            String result = PAPI.setPlaceholders(player, "Test %placeholder%");

            assertEquals("Test replaced", result);
            mockedPlaceholderAPI.verify(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"));
        }
    }
}