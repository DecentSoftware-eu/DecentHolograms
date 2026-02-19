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

import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IntegrationAvailabilityListenerTest {

    @Mock
    private IntegrationAvailabilityService integrationAvailabilityService;
    @InjectMocks
    private IntegrationAvailabilityListener listener;

    @Test
    void testOnPluginEnable() {
        PluginEnableEvent event = new PluginEnableEvent(createPlugin(Integration.PLACEHOLDER_API.getPluginName()));

        listener.onPluginEnable(event);

        verify(integrationAvailabilityService).setIntegrationAvailability(Integration.PLACEHOLDER_API, true);
    }

    @Test
    void testOnPluginDisable() {
        PluginDisableEvent event = new PluginDisableEvent(createPlugin(Integration.PLACEHOLDER_API.getPluginName()));

        listener.onPluginDisable(event);

        verify(integrationAvailabilityService).setIntegrationAvailability(Integration.PLACEHOLDER_API, false);
    }

    @Test
    void testOnPluginEnable_unknownPlugin() {
        PluginEnableEvent event = new PluginEnableEvent(createPlugin("unknownPlugin"));

        listener.onPluginEnable(event);

        verify(integrationAvailabilityService, never()).setIntegrationAvailability(any(), anyBoolean());
    }

    @Test
    void testOnPluginDisable_unknownPlugin() {
        PluginDisableEvent event = new PluginDisableEvent(createPlugin("unknownPlugin"));

        listener.onPluginDisable(event);

        verify(integrationAvailabilityService, never()).setIntegrationAvailability(any(), anyBoolean());
    }

    private static Plugin createPlugin(String pluginName) {
        Plugin plugin = mock(Plugin.class);
        when(plugin.getName()).thenReturn(pluginName);
        return plugin;
    }
}