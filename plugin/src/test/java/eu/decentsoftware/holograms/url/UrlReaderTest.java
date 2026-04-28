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

package eu.decentsoftware.holograms.url;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UrlReaderTest {

    @Test
    void testReadStringWithNullUrl() {
        Exception exception = assertThrows(NullPointerException.class, () -> UrlReader.readString(null));

        assertEquals("url cannot be null", exception.getMessage());
    }

    @Test
    void testReadString() throws IOException {
        String testContent = "Test content\n for URL reading.";
        InputStream inputStream = IOUtils.toInputStream(testContent, "UTF-8");
        URL url = mock(URL.class);
        when(url.openStream()).thenReturn(inputStream);

        String result = UrlReader.readString(url);

        assertNotNull(result);
        assertEquals(testContent, result);
    }
}