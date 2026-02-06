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

package eu.decentsoftware.holograms.display.render.placeholder;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.PlatformAdapter;
import eu.decentsoftware.holograms.platform.api.placeholder.PlaceholderContext;
import eu.decentsoftware.holograms.platform.api.placeholder.PlaceholderProvider;
import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DisplayPlaceholderServiceTest {

    private PlatformAdapter platformAdapter;
    private DisplayPlaceholderService service;

    @BeforeAll
    static void beforeAll() {
        // Setup Log util for testing
        Log.initializeForTests();
    }

    @BeforeEach
    void setUp() {
        platformAdapter = mock(PlatformAdapter.class);
        service = new DisplayPlaceholderService(platformAdapter);
    }

    @Nested
    class InternalPlaceholderReplacementTests {

        @Test
        void replacePlaceholders_replacesPlayerAndPage() {
            DisplayRenderContext context = mock(DisplayRenderContext.class);
            PlatformPlayer player = createPlayer("Steve");

            when(context.getPlayer()).thenReturn(player);
            when(context.getPage()).thenReturn(3);
            when(platformAdapter.getPlaceholderProviders()).thenReturn(Collections.emptyList());

            String result = service.replacePlaceholders("Hello {player}, page {page}", context);

            assertEquals("Hello Steve, page 3", result);
        }

        @Test
        void replacePlaceholders_multipleInternalPlaceholders_allReplaced() {
            DisplayRenderContext context = mock(DisplayRenderContext.class);
            PlatformPlayer player = createPlayer("Alex");

            when(context.getPlayer()).thenReturn(player);
            when(context.getPage()).thenReturn(1);
            when(platformAdapter.getPlaceholderProviders()).thenReturn(Collections.emptyList());

            String result = service.replacePlaceholders("{player}-{player}-{page}-{page}", context);

            assertEquals("Alex-Alex-1-1", result);
        }
    }

    @Nested
    class PlatformPlaceholderReplacementTests {

        @Test
        void replacePlaceholders_singleProvider_appliesReplacement() {
            DisplayRenderContext context = mock(DisplayRenderContext.class);
            PlatformPlayer player = createPlayer("Steve");

            PlaceholderProvider provider = mock(PlaceholderProvider.class);

            when(context.getPlayer()).thenReturn(player);
            when(context.getPage()).thenReturn(0);
            when(platformAdapter.getPlaceholderProviders()).thenReturn(Collections.singletonList(provider));
            when(provider.replace(eq("test"), any(PlaceholderContext.class))).thenReturn("replaced");

            String result = service.replacePlaceholders("test", context);

            assertEquals("replaced", result);
            verify(provider).replace(eq("test"), any(PlaceholderContext.class));
        }

        @Test
        void replacePlaceholders_multipleProviders_appliedInOrder() {
            DisplayRenderContext context = mock(DisplayRenderContext.class);
            PlatformPlayer player = createPlayer("Steve");

            PlaceholderProvider provider1 = mock(PlaceholderProvider.class);
            PlaceholderProvider provider2 = mock(PlaceholderProvider.class);

            when(context.getPlayer()).thenReturn(player);
            when(context.getPage()).thenReturn(0);
            when(platformAdapter.getPlaceholderProviders()).thenReturn(Arrays.asList(provider1, provider2));

            when(provider1.replace(eq("input"), any())).thenReturn("step1");
            when(provider2.replace(eq("step1"), any())).thenReturn("step2");

            String result = service.replacePlaceholders("input", context);

            assertEquals("step2", result);
        }

        @Test
        void replacePlaceholders_providerThrows_exceptionIsSwallowed() {
            DisplayRenderContext context = mock(DisplayRenderContext.class);
            PlatformPlayer player = createPlayer("Steve");

            PlaceholderProvider failing = mock(PlaceholderProvider.class);
            PlaceholderProvider succeeding = mock(PlaceholderProvider.class);

            when(context.getPlayer()).thenReturn(player);
            when(context.getPage()).thenReturn(0);
            when(platformAdapter.getPlaceholderProviders()).thenReturn(Arrays.asList(failing, succeeding));

            when(failing.replace(any(), any())).thenThrow(new RuntimeException("boom"));
            when(succeeding.replace(eq("content"), any())).thenReturn("ok");

            String result = service.replacePlaceholders("content", context);

            assertEquals("ok", result);
            verify(failing).replace(any(), any());
            verify(succeeding).replace(eq("content"), any());
        }
    }

    @Nested
    class PlaceholderContextCreationTests {

        @Test
        void replacePlaceholders_createsNonNullPlaceholderContext() {
            DisplayRenderContext context = mock(DisplayRenderContext.class);
            PlatformPlayer player = createPlayer("Steve");

            PlaceholderProvider provider = mock(PlaceholderProvider.class);

            when(context.getPlayer()).thenReturn(player);
            when(context.getPage()).thenReturn(0);
            when(platformAdapter.getPlaceholderProviders()).thenReturn(Collections.singletonList(provider));
            when(provider.replace(any(), any())).thenAnswer(invocation -> {
                PlaceholderContext ctx = invocation.getArgument(1);
                assertNotNull(ctx);
                assertEquals(player, ctx.getPlayer());
                return invocation.getArgument(0);
            });

            service.replacePlaceholders("content", context);
        }
    }

    private PlatformPlayer createPlayer(String name) {
        PlatformPlayer player = mock(PlatformPlayer.class);
        when(player.getName()).thenReturn(name);
        return player;
    }
}
