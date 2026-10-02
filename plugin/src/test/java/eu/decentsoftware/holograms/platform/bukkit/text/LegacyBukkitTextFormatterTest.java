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

package eu.decentsoftware.holograms.platform.bukkit.text;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.color.IridiumColorAPI;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;

class LegacyBukkitTextFormatterTest {

    private final LegacyBukkitTextFormatter formatter = new LegacyBukkitTextFormatter();

    @BeforeAll
    static void beforeAll() throws ClassNotFoundException {
        // IridiumColorAPI's static initializer reads Settings, whose own initializer
        // requires a running plugin. Force initialization while the API is mocked.
        try (MockedStatic<DecentHologramsAPI> mockedApi = mockStatic(DecentHologramsAPI.class)) {
            mockedApi.when(DecentHologramsAPI::get).thenReturn(mock(DecentHolograms.class));
            Class.forName(IridiumColorAPI.class.getName(), true,
                    LegacyBukkitTextFormatterTest.class.getClassLoader());
        }
    }

    @Test
    void testTranslatesLegacyColorCodes() {
        assertEquals("§aHello §bWorld!", formatter.format("&aHello &bWorld!"));
    }

    @Test
    void testTranslatesFormattingCodes() {
        assertEquals("§lBold §oItalic§r", formatter.format("&lBold &oItalic&r"));
    }

    @Test
    void testTranslatesSectionCodesUnchanged() {
        assertEquals("§aHello", formatter.format("§aHello"));
    }

    @Test
    void testLeavesTextWithoutCodesUnchanged() {
        assertEquals("Hello World!", formatter.format("Hello World!"));
    }

    @Test
    void testLeavesInvalidCodesUnchanged() {
        assertEquals("&zNot a color", formatter.format("&zNot a color"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "&aHello", "&lBold &oItalic&r", "no codes at all", "&zInvalid"})
    void testIsPureFunctionOfItsInput(String input) {
        // The TextFormatter contract requires purity; CachingTextFormatter depends on it.
        String first = formatter.format(input);
        assertEquals(first, formatter.format(input));
        assertEquals(first, new LegacyBukkitTextFormatter().format(input));
    }
}
