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

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationAvailabilityServiceTest {

    @Mock
    private JavaPlugin plugin;
    @Mock
    private PluginManager pluginManager;
    @InjectMocks
    private IntegrationAvailabilityService service;

    private static Object[][] provideInvalidParamsForConstruction() {
        return new Object[][]{
                {null, mock(PluginManager.class), "plugin cannot be null"},
                {mock(JavaPlugin.class), null, "pluginManager cannot be null"},
                {null, null, "plugin cannot be null"},
        };
    }

    @ParameterizedTest
    @MethodSource("provideInvalidParamsForConstruction")
    void testConstruction_invalidParams(JavaPlugin plugin, PluginManager pluginManager, String expectedMessage) {
        NullPointerException exception = assertThrows(NullPointerException.class,
                () -> new IntegrationAvailabilityService(plugin, pluginManager));

        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    void testInitialize() {
        when(pluginManager.isPluginEnabled(anyString())).thenReturn(true);

        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);

            service.initialize();

            for (Integration integration : Integration.values()) {
                boolean isAvailable = service.isIntegrationAvailable(integration);
                assertTrue(isAvailable, "Integration " + integration + " should be available after initialization.");
                verify(pluginManager).isPluginEnabled(integration.getPluginName());
            }
            verify(pluginManager).registerEvents(any(IntegrationAvailabilityListener.class), eq(plugin));
        }
    }

    @Test
    void testInitialize_notPrimaryThread() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(false);

            IllegalStateException exception = assertThrows(IllegalStateException.class, service::initialize);

            assertEquals("This method must be called from the Main thread.", exception.getMessage());
            verifyNoInteractions(pluginManager);
        }
    }

    @Test
    void testInitialize_alreadyInitialized() {
        when(pluginManager.isPluginEnabled(anyString())).thenReturn(true);

        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);

            // Initialize once
            service.initialize();

            // Attempt to initialize again
            IllegalStateException exception = assertThrows(IllegalStateException.class, service::initialize);

            assertEquals("IntegrationAvailabilityService has already been initialized.", exception.getMessage());
        }
    }

    @Test
    void testShutdown() {
        try (MockedStatic<HandlerList> mockedHandlerList = mockStatic(HandlerList.class)) {
            service.shutdown();

            mockedHandlerList.verify(() -> HandlerList.unregisterAll(any(IntegrationAvailabilityListener.class)));
        }
    }

    @Test
    void testIsIntegrationAvailable_notInitialized() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.isIntegrationAvailable(Integration.PLACEHOLDER_API));

        assertEquals("IntegrationAvailabilityService has not been initialized.", exception.getMessage());
    }

    @Test
    void testIsIntegrationAvailable_nullIntegration() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);

            service.initialize();
            NullPointerException exception = assertThrows(NullPointerException.class, () -> service.isIntegrationAvailable(null));

            assertEquals("integration cannot be null", exception.getMessage());
        }
    }

    @Test
    void testSetIntegrationAvailability_notInitialized() {
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> service.setIntegrationAvailability(Integration.PLACEHOLDER_API, true));

        assertEquals("IntegrationAvailabilityService has not been initialized.", exception.getMessage());
    }

    @Test
    void testSetIntegrationAvailability_nullIntegration() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);

            service.initialize();
            NullPointerException exception = assertThrows(NullPointerException.class,
                    () -> service.setIntegrationAvailability(null, true));

            assertEquals("integration cannot be null", exception.getMessage());
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testSetIntegrationAvailability(boolean available) {
        when(pluginManager.isPluginEnabled(anyString())).thenReturn(!available); // Opposite initial state

        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class)) {
            mockedBukkit.when(Bukkit::isPrimaryThread).thenReturn(true);

            service.initialize();
            service.setIntegrationAvailability(Integration.PLACEHOLDER_API, available);

            assertEquals(available, service.isIntegrationAvailable(Integration.PLACEHOLDER_API));
            assertEquals(!available, service.isIntegrationAvailable(Integration.HEAD_DATABASE)); // Other integrations are unaffected
        }
    }
}