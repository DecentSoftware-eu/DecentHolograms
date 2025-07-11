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
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PAPITest {

    @Mock
    private PluginManager pluginManager;
    @Mock
    private Player player;
    @Mock
    private DecentHolograms decentHolograms;
    @Mock
    private Logger logger;

    @Test
    void testSetPlaceholders_PlaceholderAPINotEnabled() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class);
             MockedStatic<PlaceholderAPI> mockedPlaceholderAPI = mockStatic(PlaceholderAPI.class)) {
            mockedBukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            mockedBukkit.when(() -> pluginManager.isPluginEnabled("PlaceholderAPI")).thenReturn(false);

            String result = PAPI.setPlaceholders(player, "Test %placeholder%");

            assertEquals("Test %placeholder%", result);
            mockedPlaceholderAPI.verifyNoInteractions();
        }
    }

    @Test
    void testSetPlaceholders_PlaceholderAPIThrows() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class);
             MockedStatic<PlaceholderAPI> mockedPlaceholderAPI = mockStatic(PlaceholderAPI.class);
             MockedStatic<DecentHologramsAPI> mockedDecentHologramsAPI = mockStatic(DecentHologramsAPI.class)) {
            mockedBukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            mockedBukkit.when(() -> pluginManager.isPluginEnabled("PlaceholderAPI")).thenReturn(true);
            mockedPlaceholderAPI.when(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"))
                    .thenThrow(new RuntimeException("PlaceholderAPI exception"));
            mockedDecentHologramsAPI.when(DecentHologramsAPI::get).thenReturn(decentHolograms);
            when(decentHolograms.getLogger()).thenReturn(logger);

            String result = PAPI.setPlaceholders(player, "Test %placeholder%");

            assertEquals("Test %placeholder%", result);
            mockedPlaceholderAPI.verify(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"));
        }
    }

    @Test
    void testSetPlaceholders() {
        try (MockedStatic<Bukkit> mockedBukkit = mockStatic(Bukkit.class);
             MockedStatic<PlaceholderAPI> mockedPlaceholderAPI = mockStatic(PlaceholderAPI.class)) {
            mockedBukkit.when(Bukkit::getPluginManager).thenReturn(pluginManager);
            mockedBukkit.when(() -> pluginManager.isPluginEnabled("PlaceholderAPI")).thenReturn(true);
            mockedPlaceholderAPI.when(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"))
                    .thenReturn("Test replaced");

            String result = PAPI.setPlaceholders(player, "Test %placeholder%");

            assertEquals("Test replaced", result);
            mockedPlaceholderAPI.verify(() -> PlaceholderAPI.setPlaceholders(player, "Test %placeholder%"));
        }
    }
}