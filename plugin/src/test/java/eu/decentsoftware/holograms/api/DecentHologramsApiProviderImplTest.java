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

package eu.decentsoftware.holograms.api;

import eu.decentsoftware.holograms.api.hologram.ApiHologramManager;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;

class DecentHologramsApiProviderImplTest {

    private DecentHologramsApiProviderImpl provider;

    @BeforeEach
    void setUp() {
        provider = new DecentHologramsApiProviderImpl();
    }

    @Test
    void testDestroy() {
        Plugin plugin = mock(Plugin.class);

        try (MockedConstruction<DecentHologramsApiImpl> ignored = mockConstruction(DecentHologramsApiImpl.class)) {
            // Register an API instance
            DecentHologramsApiImpl api = provider.getApi(plugin);

            provider.destroy();

            verify(api).destroy();
        }
    }

    @Test
    void testGetApi_nullPlugin() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> provider.getApi(null));

        assertEquals("plugin cannot be null", exception.getMessage());
    }

    @Test
    void testGetApi() {
        Plugin plugin = mock(Plugin.class);

        DecentHologramsApi api = provider.getApi(plugin);

        assertNotNull(api);
        assertNotNull(api.getHologramManager());
        assertInstanceOf(ApiHologramManager.class, api.getHologramManager());
    }

    @Test
    void testGetApi_differentPlugins() {
        Plugin plugin1 = mock(Plugin.class);
        Plugin plugin2 = mock(Plugin.class);

        DecentHologramsApi api1 = provider.getApi(plugin1);
        DecentHologramsApi api2 = provider.getApi(plugin2);

        assertNotNull(api1);
        assertNotNull(api2);
        assertNotEquals(api1, api2);
        assertNotEquals(api1.getHologramManager(), api2.getHologramManager());
    }

    @Test
    void testGetApi_samePlugin() {
        Plugin plugin = mock(Plugin.class);

        DecentHologramsApi api1 = provider.getApi(plugin);
        DecentHologramsApi api2 = provider.getApi(plugin);

        assertNotNull(api1);
        assertNotNull(api2);
        assertEquals(api1, api2);
    }

}