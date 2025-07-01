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

package eu.decentsoftware.holograms.api.v1;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class BukkitDecentHologramsApiProviderTest {

    @Test
    void testGetImplementation_ThrowsExceptionWhenNotInitialized() {
        Plugin mockPlugin = mockPlugin();

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> BukkitDecentHologramsApiProvider.getImplementation(mockPlugin)
        );

        assertEquals("DecentHolograms API is not initialized. Please ensure that the DecentHolograms plugin is loaded before accessing the API.",
                exception.getMessage());
    }

    @Test
    void testGetImplementation_ReturnsSetImplementation() {
        Plugin mockPlugin = mock(Plugin.class);
        BukkitDecentHologramsApiProvider mockProvider = mock(BukkitDecentHologramsApiProvider.class);

        try {
            BukkitDecentHologramsApiProvider.setImplementation(mockProvider);

            BukkitDecentHologramsApiProvider result = BukkitDecentHologramsApiProvider.getImplementation(mockPlugin);

            assertEquals(mockProvider, result);
        } finally {
            resetImplementation();
        }
    }

    @Test
    void testSetImplementation_ThrowsExceptionWhenAlreadyInitialized() {
        BukkitDecentHologramsApiProvider firstProvider = mock(BukkitDecentHologramsApiProvider.class);
        BukkitDecentHologramsApiProvider secondProvider = mock(BukkitDecentHologramsApiProvider.class);

        try {
            BukkitDecentHologramsApiProvider.setImplementation(firstProvider);

            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> BukkitDecentHologramsApiProvider.setImplementation(secondProvider));

            assertEquals("DecentHolograms API is already initialized.", exception.getMessage());
        } finally {
            resetImplementation();
        }
    }

    private static Plugin mockPlugin() {
        Plugin mockPlugin = mock(Plugin.class);
        PluginDescriptionFile descriptionFile = new PluginDescriptionFile("MockPlugin", "1.0.0", "MockMain");
        when(mockPlugin.getDescription()).thenReturn(descriptionFile);
        when(mockPlugin.getName()).thenReturn("MockPlugin");
        PluginLogger pluginLogger = mock(PluginLogger.class);
        when(mockPlugin.getLogger()).thenReturn(pluginLogger);
        return mockPlugin;
    }

    private void resetImplementation() {
        try {
            Field field = BukkitDecentHologramsApiProvider.class.getDeclaredField("implementation");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException("Failed to reset implementation state", e);
        }
    }
}