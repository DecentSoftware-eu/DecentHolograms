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

package eu.decentsoftware.holograms.api.v1.hologram;

import eu.decentsoftware.holograms.api.v1.location.DecentLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiHologramManagerTest {

    @Mock
    private ApiHologramFactory hologramFactory;
    @InjectMocks
    private ApiHologramManager manager;

    @Test
    void testCreateHologram() {
        ApiHologram hologram = mock(ApiHologram.class);
        DecentLocation location = new DecentLocation("world", 100, 50, 100);

        when(hologramFactory.createHologram(location)).thenReturn(hologram);

        Hologram createdHologram = manager.createHologram(location);

        assertEquals(hologram, createdHologram);
        assertEquals(1, manager.getHolograms().size());
        assertTrue(manager.getHolograms().contains(hologram));
        verify(hologramFactory, times(1)).createHologram(location);
    }

    @Test
    void testDestroy() {
        ApiHologram hologram1 = mock(ApiHologram.class);
        ApiHologram hologram2 = mock(ApiHologram.class);
        DecentLocation location1 = new DecentLocation("world", 100, 50, 100);
        DecentLocation location2 = new DecentLocation("world", 200, 60, 200);

        when(hologramFactory.createHologram(location1)).thenReturn(hologram1);
        when(hologramFactory.createHologram(location2)).thenReturn(hologram2);

        manager.createHologram(location1);
        manager.createHologram(location2);

        manager.destroy();

        verify(hologram1, times(1)).destroy();
        verify(hologram2, times(1)).destroy();
        assertTrue(manager.getHolograms().isEmpty());
    }
}