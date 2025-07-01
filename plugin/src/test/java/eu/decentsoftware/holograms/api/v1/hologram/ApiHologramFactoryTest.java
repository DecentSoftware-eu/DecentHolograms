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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ApiHologramFactoryTest {

    private ApiHologramFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ApiHologramFactory();
    }

    @Test
    void testCreateHologram() {
        DecentLocation location = new DecentLocation("world", 100, 50, 100);
        ApiHologram hologram = factory.createHologram(location);

        assertNotNull(hologram);
        assertNotNull(hologram.getLocationManager());
        assertEquals(location, hologram.getLocationManager().getLocation());
        assertNotNull(hologram.getVisibilityManager());
    }
}