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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UrlReaderTest {

    @Nested
    class SuccessTests {

        @Test
        void readsTheResponseBody() throws IOException {
            String content = "Test content\n for URL reading.";

            assertEquals(content, UrlReader.readString(urlReturning(responding(200, content))));
        }

        @ParameterizedTest
        @ValueSource(ints = {200, 201, 204, 299})
        void acceptsAnySuccessStatus(int status) throws IOException {
            assertEquals("ok", UrlReader.readString(urlReturning(responding(status, "ok"))));
        }

        @Test
        void readsFromANonHttpConnection() throws IOException {
            URLConnection connection = mock(URLConnection.class);
            when(connection.getInputStream()).thenReturn(stream("from disk"));
            URL url = mock(URL.class);
            when(url.openConnection()).thenReturn(connection);

            assertEquals("from disk", UrlReader.readString(url));
        }

        @Test
        void rejectsANullUrl() {
            Exception exception = assertThrows(NullPointerException.class, () -> UrlReader.readString(null));

            assertEquals("url cannot be null", exception.getMessage());
        }
    }

    @Nested
    class FailureStatusTests {

        @Test
        void reportsTheStatusRatherThanAPlainIoException() throws IOException {
            HttpURLConnection connection = responding(400, "");
            when(connection.getErrorStream()).thenReturn(null);

            HttpStatusException exception = assertThrows(HttpStatusException.class,
                    () -> UrlReader.readString(urlReturning(connection)));

            assertEquals(400, exception.getStatusCode());
        }

        @Test
        void includesWhateverTheServerSaid() throws IOException {
            HttpURLConnection connection = responding(400, "");
            when(connection.getErrorStream()).thenReturn(stream("{\"error\":\"CONSTRAINT_VIOLATION\"}"));

            HttpStatusException exception = assertThrows(HttpStatusException.class,
                    () -> UrlReader.readString(urlReturning(connection)));

            assertTrue(exception.getMessage().contains("CONSTRAINT_VIOLATION"),
                    "the body usually carries the only explanation: " + exception.getMessage());
        }

        @Test
        void truncatesAnOverlongErrorBody() throws IOException {
            StringBuilder body = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                body.append("0123456789");
            }
            HttpURLConnection connection = responding(500, "");
            when(connection.getErrorStream()).thenReturn(stream(body.toString()));

            HttpStatusException exception = assertThrows(HttpStatusException.class,
                    () -> UrlReader.readString(urlReturning(connection)));

            assertTrue(exception.getMessage().length() < 400, "an HTML error page must not fill the log");
            assertTrue(exception.getMessage().endsWith("..."));
        }

        @Test
        void survivesAnUnreadableErrorBody() throws IOException {
            InputStream broken = mock(InputStream.class);
            when(broken.read()).thenThrow(new IOException("connection reset"));
            HttpURLConnection connection = responding(503, "");
            when(connection.getErrorStream()).thenReturn(broken);

            HttpStatusException exception = assertThrows(HttpStatusException.class,
                    () -> UrlReader.readString(urlReturning(connection)));

            assertEquals(503, exception.getStatusCode());
        }
    }

    @Nested
    class PermanenceTests {

        @ParameterizedTest
        @ValueSource(ints = {400, 401, 403, 404, 410, 422})
        void aRejectedRequestIsPermanent(int status) {
            assertTrue(new HttpStatusException(status, "").isPermanentRejection(),
                    status + " will fail the same way if repeated, so it is an answer");
        }

        @ParameterizedTest
        @ValueSource(ints = {408, 429, 500, 502, 503, 504})
        void anOverloadedOrSlowServerIsNot(int status) {
            assertFalse(new HttpStatusException(status, "").isPermanentRejection(),
                    status + " may succeed later, so it must not be cached as an answer");
        }
    }

    private static InputStream stream(String content) {
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }

    private static URL urlReturning(HttpURLConnection connection) throws IOException {
        URL url = mock(URL.class);
        when(url.openConnection()).thenReturn(connection);
        return url;
    }

    private static HttpURLConnection responding(int status, String body) throws IOException {
        HttpURLConnection connection = mock(HttpURLConnection.class);
        when(connection.getResponseCode()).thenReturn(status);
        when(connection.getInputStream()).thenReturn(stream(body));
        return connection;
    }
}
