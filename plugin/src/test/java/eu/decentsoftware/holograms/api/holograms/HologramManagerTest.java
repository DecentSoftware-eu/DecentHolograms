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

package eu.decentsoftware.holograms.api.holograms;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HologramManagerTest {

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private DecentHolograms decentHolograms;
    @Mock
    private Player player;
    private Hologram hologram;
    private HologramManager manager;

    @BeforeEach
    void setUp() {
        // The manager registers itself to the ticker and schedules a reload when created,
        // and the Hologram class looks up the running plugin when it gets loaded.
        try (MockedStatic<DecentHologramsAPI> mockedDecentHologramsAPI = mockStatic(DecentHologramsAPI.class);
             MockedStatic<S> ignored = mockStatic(S.class)) {
            mockedDecentHologramsAPI.when(DecentHologramsAPI::get).thenReturn(decentHolograms);

            manager = new HologramManager(decentHolograms);
            hologram = mock(Hologram.class);
        }
    }

    @Test
    void testUpdateVisibility_visibleAndAllowed() {
        when(hologram.isDefaultVisibleState()).thenReturn(true);
        when(hologram.isVisible(player)).thenReturn(true);
        when(hologram.canShow(player)).thenReturn(true);
        when(hologram.isInDisplayRange(player)).thenReturn(true);

        manager.updateVisibility(player, hologram);

        verify(hologram).updateLineVisibility(player);
        verify(hologram, never()).show(any(), anyInt());
        verify(hologram, never()).hide(any());
    }

    @Test
    void testUpdateVisibility_notVisibleAndAllowed() {
        when(hologram.isDefaultVisibleState()).thenReturn(true);
        when(hologram.isVisible(player)).thenReturn(false);
        when(hologram.canShow(player)).thenReturn(true);
        when(hologram.isInDisplayRange(player)).thenReturn(true);
        when(hologram.getPlayerPage(player)).thenReturn(1);

        manager.updateVisibility(player, hologram);

        verify(hologram).show(player, 1);
        verify(hologram, never()).updateLineVisibility(any());
    }

    @Test
    void testUpdateVisibility_visibleAndMissingPermission() {
        when(hologram.isDefaultVisibleState()).thenReturn(true);
        when(hologram.isVisible(player)).thenReturn(true);
        when(hologram.canShow(player)).thenReturn(false);

        manager.updateVisibility(player, hologram);

        verify(hologram).hide(player);
        verify(hologram, never()).updateLineVisibility(any());
    }

    @Test
    void testUpdateVisibility_visibleAndOutOfDisplayRange() {
        when(hologram.isDefaultVisibleState()).thenReturn(true);
        when(hologram.isVisible(player)).thenReturn(true);
        when(hologram.canShow(player)).thenReturn(true);
        when(hologram.isInDisplayRange(player)).thenReturn(false);

        manager.updateVisibility(player, hologram);

        verify(hologram).hide(player);
        verify(hologram, never()).updateLineVisibility(any());
    }

    @Test
    void testUpdateVisibility_hideState() {
        when(hologram.isHideState(player)).thenReturn(true);
        when(hologram.isVisible(player)).thenReturn(true);

        manager.updateVisibility(player, hologram);

        verify(hologram).hide(player);
        verify(hologram, never()).updateLineVisibility(any());
    }

    @Test
    void testUpdateVisibility_disabled() {
        when(hologram.isDisabled()).thenReturn(true);

        manager.updateVisibility(player, hologram);

        verify(hologram, never()).show(any(), anyInt());
        verify(hologram, never()).hide(any());
        verify(hologram, never()).updateLineVisibility(any());
    }

}
