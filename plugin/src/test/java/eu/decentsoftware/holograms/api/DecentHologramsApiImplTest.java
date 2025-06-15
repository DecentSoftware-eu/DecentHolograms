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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DecentHologramsApiImplTest {

    @Mock
    private ApiHologramManager hologramManager;
    private DecentHologramsApiImpl api;

    @BeforeEach
    void setUp() {
        api = new DecentHologramsApiImpl(hologramManager);
    }

    @Test
    void testConstruction_nullHologramService() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new DecentHologramsApiImpl(null));

        assertEquals("hologramManager cannot be null", exception.getMessage());
    }

    @Test
    void testGetHologramService() {
        assertEquals(hologramManager, api.getHologramManager());
    }

    @Test
    void testDestroy() {
        api.destroy();

        verify(hologramManager).destroy();
    }

}