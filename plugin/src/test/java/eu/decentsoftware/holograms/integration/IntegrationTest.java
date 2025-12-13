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

package eu.decentsoftware.holograms.integration;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationTest {

    private static Object[][] provideIntegrationsAndPluginNames() {
        return new Object[][]{
                {Integration.PLACEHOLDER_API, "PlaceholderAPI"},
                {Integration.HEAD_DATABASE, "HeadDatabase"},
        };
    }

    @ParameterizedTest
    @MethodSource("provideIntegrationsAndPluginNames")
    void testGetByPluginName(Integration integration, String pluginName) {
        assertEquals(2, Integration.values().length, "New integration added without updating this test!");
        assertEquals(integration, Integration.getByPluginName(pluginName));
    }

    @Test
    void testGetByPluginName_invalidName() {
        assertNull(Integration.getByPluginName("invalidName"));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testIsAvailable(boolean available) {
        DecentHolograms plugin = mock(DecentHolograms.class);
        IntegrationAvailabilityService service = mock(IntegrationAvailabilityService.class);

        try (MockedStatic<DecentHologramsAPI> mockedDecentHologramsAPI = mockStatic(DecentHologramsAPI.class)) {
            mockedDecentHologramsAPI.when(DecentHologramsAPI::get).thenReturn(plugin);
            when(plugin.getIntegrationAvailabilityService()).thenReturn(service);
            when(service.isIntegrationAvailable(Integration.PLACEHOLDER_API)).thenReturn(available);

            assertEquals(available, Integration.PLACEHOLDER_API.isAvailable());
        }
    }
}