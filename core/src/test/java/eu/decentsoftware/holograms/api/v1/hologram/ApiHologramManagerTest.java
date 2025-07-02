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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApiHologramManagerTest {

    private ApiHologramManager manager;

    @BeforeEach
    void setUp() {
        manager = new ApiHologramManager();
    }

    @Test
    void testNewHologram() {
        ApiHologram hologram = mock(ApiHologram.class);

        try (MockedConstruction<ApiHologramBuilder> builderMockedConstruction = mockConstruction(ApiHologramBuilder.class,
                (mock, context) -> when(mock.build()).thenReturn(hologram))) {
            ApiHologramBuilder builder = manager.newHologram();
            Hologram createdHologram = builder.build();

            assertEquals(1, builderMockedConstruction.constructed().size());
            ApiHologramBuilder expectedBuilder = builderMockedConstruction.constructed().get(0);
            assertEquals(expectedBuilder, builder);
            assertEquals(hologram, createdHologram);
        }
    }

    @Test
    void testNewHologram_addedToManager() {
        ApiHologram createdHologram = manager.newHologram()
                .withLocation(new DecentLocation("world", 0, 0, 0))
                .withPage()
                .withTextLine("text")
                .and()
                .build();

        assertNotNull(createdHologram);
        assertEquals(1, manager.getHolograms().size());
        assertTrue(manager.getHolograms().contains(createdHologram));
    }

    @Test
    void testDestroy() {
        try (MockedConstruction<ApiHologram> mockedHologramConstruction = mockConstruction(ApiHologram.class)) {
            ApiHologram createdHologram = manager.newHologram()
                    .withLocation(new DecentLocation("world", 100, 50, 100))
                    .withPage()
                    .withTextLine("text")
                    .and()
                    .build();
            ApiHologram constructedHologram = mockedHologramConstruction.constructed().get(0);

            assertEquals(constructedHologram, createdHologram);
            assertEquals(1, manager.getHolograms().size());

            manager.destroy();

            verify(createdHologram, times(1)).destroy();
            assertEquals(0, manager.getHolograms().size());
        }
    }
}