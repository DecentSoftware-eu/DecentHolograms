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

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BukkitDecentHologramsApiServiceTest {

    @Mock
    private JavaPlugin plugin;
    @Mock
    private BukkitDecentHologramsApiProviderImpl provider;
    @Mock
    private BukkitDecentHologramsApiListener listener;
    @Mock
    private PluginManager pluginManager;
    @InjectMocks
    private BukkitDecentHologramsApiService service;

    @Test
    void testInitialize() {
        try (MockedStatic<Bukkit> bukkitMock = mockStatic(Bukkit.class);
             MockedStatic<BukkitDecentHologramsApiProvider> providerMock = mockStatic(BukkitDecentHologramsApiProvider.class)) {
            bukkitMock.when(Bukkit::getPluginManager).thenReturn(pluginManager);

            service.initialize();

            providerMock.verify(() -> BukkitDecentHologramsApiProvider.setImplementation(provider));
            verify(pluginManager).registerEvents(listener, plugin);
        }
    }

    @Test
    void testDestroy() {
        try (MockedStatic<HandlerList> handlerListMock = mockStatic(HandlerList.class);
             MockedStatic<BukkitDecentHologramsApiProvider> providerMock = mockStatic(BukkitDecentHologramsApiProvider.class)) {

            service.destroy();

            handlerListMock.verify(() -> HandlerList.unregisterAll(listener));
            providerMock.verify(() -> BukkitDecentHologramsApiProvider.setImplementation(null));
            verify(provider).destroy();
        }
    }
}