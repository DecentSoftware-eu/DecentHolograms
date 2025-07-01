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

package eu.decentsoftware.holograms.api.v1.location;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class ApiLocationManagerTest {

    @Test
    void testConstructor_nullLocation() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new ApiLocationManager(null));

        assertEquals("location cannot be null", exception.getMessage());
    }

    @Test
    void testSetLocation_nullLocation() {
        DecentLocation initialLocation = new DecentLocation("world", 0, 0, 0);
        ApiLocationManager manager = new ApiLocationManager(initialLocation);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> manager.setLocation(null));

        assertEquals("location cannot be null", exception.getMessage());
    }

    @Test
    void testSetOffsets_nullOffsets() {
        DecentLocation initialLocation = new DecentLocation("world", 0, 0, 0);
        ApiLocationManager manager = new ApiLocationManager(initialLocation);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> manager.setOffsets(null));

        assertEquals("offsets cannot be null", exception.getMessage());
    }

    @Test
    void testResetOffsets() {
        DecentLocation initialLocation = new DecentLocation("world", 0, 0, 0);
        ApiLocationManager manager = new ApiLocationManager(initialLocation);

        DecentOffsets offsets = new DecentOffsets(10.0, 5.0, -15.0);
        manager.setOffsets(offsets);

        assertEquals(offsets, manager.getOffsets());

        manager.resetOffsets();

        assertEquals(DecentOffsets.ZERO, manager.getOffsets());
    }

    @Test
    void testUnbindLocation() {
        DecentLocation location = new DecentLocation("world", 100.5, 64.0, 200.5);
        ApiLocationManager manager = new ApiLocationManager(location);

        LocationBinder binder = mock(LocationBinder.class);
        manager.bindLocation(binder);

        assertEquals(binder, manager.getLocationBinder());

        manager.unbindLocation();

        assertNull(manager.getLocationBinder());
    }

    @Test
    void testGetOffsets_defaultOffsets() {
        DecentLocation location = new DecentLocation("world", 100.5, 64.0, 200.5);
        ApiLocationManager manager = new ApiLocationManager(location);

        DecentOffsets offsets = manager.getOffsets();

        assertEquals(0.0, offsets.getX());
        assertEquals(0.0, offsets.getY());
        assertEquals(0.0, offsets.getZ());
    }

    @Test
    void testBindLocation() {
        DecentLocation location = new DecentLocation("world", 100.5, 64.0, 200.5);
        ApiLocationManager manager = new ApiLocationManager(location);

        assertNull(manager.getLocationBinder());
        assertFalse(manager.isLocationBound());

        LocationBinder binder = mock(LocationBinder.class);
        manager.bindLocation(binder);

        assertEquals(binder, manager.getLocationBinder());
        assertTrue(manager.isLocationBound());
    }

    @Test
    void testSetLocation() {
        DecentLocation initialLocation = new DecentLocation("world", 100.5, 64.0, 200.5);
        ApiLocationManager manager = new ApiLocationManager(initialLocation);

        assertEquals(initialLocation, manager.getLocation());
        DecentLocation newLocation = new DecentLocation("newWorld", 150.0, 70.0, 250.0);
        manager.setLocation(newLocation);

        assertEquals(newLocation, manager.getLocation());
    }

    @Test
    void testGetActualLocation_noBinderAndDefaultOffsets() {
        DecentLocation location = new DecentLocation("world", 100.5, 64.0, 200.5);
        ApiLocationManager manager = new ApiLocationManager(location);

        DecentLocation actualLocation = manager.getActualLocation();

        assertEquals(location.getWorldName(), actualLocation.getWorldName());
        assertEquals(location.getX(), actualLocation.getX());
        assertEquals(location.getY(), actualLocation.getY());
        assertEquals(location.getZ(), actualLocation.getZ());
    }

    @Test
    void testGetActualLocation_noBinderAndCustomOffsets() {
        DecentLocation location = new DecentLocation("world", 100.5, 64.0, 200.5);
        ApiLocationManager manager = new ApiLocationManager(location);

        DecentOffsets offsets = new DecentOffsets(10.0, 5.0, -15.0);
        manager.setOffsets(offsets);

        DecentLocation actualLocation = manager.getActualLocation();

        assertEquals(location.getWorldName(), actualLocation.getWorldName());
        assertEquals(location.getX() + offsets.getX(), actualLocation.getX());
        assertEquals(location.getY() + offsets.getY(), actualLocation.getY());
        assertEquals(location.getZ() + offsets.getZ(), actualLocation.getZ());
    }

    @Test
    void testGetActualLocation_binderAndDefaultOffsets() {
        DecentLocation location = new DecentLocation("default", 150.0, 70.0, 250.0);
        DecentLocation binderLocation = new DecentLocation("world", 200.0, 80.0, 300.0);

        LocationBinder binder = mock(LocationBinder.class);
        Mockito.when(binder.getLocation()).thenReturn(binderLocation);

        ApiLocationManager manager = new ApiLocationManager(location);
        manager.bindLocation(binder);

        DecentLocation actualLocation = manager.getActualLocation();

        assertEquals(binderLocation.getWorldName(), actualLocation.getWorldName());
        assertEquals(binderLocation.getX(), actualLocation.getX());
        assertEquals(binderLocation.getY(), actualLocation.getY());
        assertEquals(binderLocation.getZ(), actualLocation.getZ());
    }

    @Test
    void testGetActualLocation_binderAndCustomOffsets() {
        DecentLocation location = new DecentLocation("default", 150.0, 70.0, 250.0);
        DecentLocation binderLocation = new DecentLocation("world", 200.0, 80.0, 300.0);

        LocationBinder binder = mock(LocationBinder.class);
        Mockito.when(binder.getLocation()).thenReturn(binderLocation);

        ApiLocationManager manager = new ApiLocationManager(location);
        manager.bindLocation(binder);

        DecentOffsets offsets = new DecentOffsets(-20.0, 15.0, 10.0);
        manager.setOffsets(offsets);

        DecentLocation actualLocation = manager.getActualLocation();

        assertEquals(binderLocation.getWorldName(), actualLocation.getWorldName());
        assertEquals(binderLocation.getX() + offsets.getX(), actualLocation.getX());
        assertEquals(binderLocation.getY() + offsets.getY(), actualLocation.getY());
        assertEquals(binderLocation.getZ() + offsets.getZ(), actualLocation.getZ());
    }
}