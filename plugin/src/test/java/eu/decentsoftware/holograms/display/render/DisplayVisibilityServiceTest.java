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

package eu.decentsoftware.holograms.display.render;

import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.DisplaySettings;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DisplayVisibilityServiceTest {

    private DisplayVisibilityService visibilityService;

    @BeforeEach
    void setUp() {
        visibilityService = new DisplayVisibilityService();
    }

    @Test
    void shouldBeShownToPlayer_withoutPermission() {
        DisplayBase display = createDisplay(null);
        PlatformPlayer player = createPlayer("world", 0, 0, 0, false);

        assertTrue(visibilityService.shouldBeShownToPlayer(display, player));
    }

    @Test
    void shouldBeShownToPlayer_withPermissionGranted() {
        DisplayBase display = createDisplay("lang.en");
        PlatformPlayer player = createPlayer("world", 0, 0, 0, true);

        assertTrue(visibilityService.shouldBeShownToPlayer(display, player));
    }

    @Test
    void shouldBeShownToPlayer_withPermissionDenied() {
        DisplayBase display = createDisplay("lang.en");
        PlatformPlayer player = createPlayer("world", 0, 0, 0, false);

        assertFalse(visibilityService.shouldBeShownToPlayer(display, player));
    }

    @Test
    void shouldBeShownToPlayer_withBlankPermission() {
        DisplayBase display = createDisplay("   ");
        PlatformPlayer player = createPlayer("world", 0, 0, 0, false);

        assertTrue(visibilityService.shouldBeShownToPlayer(display, player));
    }

    private DisplayBase createDisplay(String permission) {
        DisplaySettings settings = new DisplaySettings();
        settings.setPermission(permission);
        return new TextDisplay("test", new DecentLocation("world", 0, 0, 0, 0f, 0f), settings);
    }

    private PlatformPlayer createPlayer(String world, double x, double y, double z, boolean hasPermission) {
        PlatformPlayer player = mock(PlatformPlayer.class);
        when(player.getUniqueId()).thenReturn(UUID.randomUUID());
        when(player.getName()).thenReturn("Steve");
        when(player.getLocation()).thenReturn(new DecentLocation(world, x, y, z, 0f, 0f));
        when(player.hasPermission("lang.en")).thenReturn(hasPermission);
        return player;
    }
}
