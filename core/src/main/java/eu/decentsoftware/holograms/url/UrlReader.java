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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Utility class for reading data from a URL.
 * This class provides methods to read the content of a URL as a String.
 *
 * @author d0by
 * @since 2.9.6
 */
public class UrlReader {

    private static final int MAX_ERROR_BODY_LENGTH = 256;

    private UrlReader() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Reads the content of the specified URL and returns it as a String.
     *
     * @param url The URL to read from.
     * @return The content of the URL as a String.
     * @throws HttpStatusException  If the server responded with a failing status.
     * @throws IOException          If an I/O error occurs while reading from the URL.
     * @throws NullPointerException If the provided URL is null.
     * @since 2.9.6
     */
    public static String readString(@NotNull URL url) throws IOException {
        Objects.requireNonNull(url, "url cannot be null");

        URLConnection connection = url.openConnection();
        if (!(connection instanceof HttpURLConnection)) {
            try (InputStream input = connection.getInputStream()) {
                return read(input);
            }
        }

        HttpURLConnection httpConnection = (HttpURLConnection) connection;
        int status = httpConnection.getResponseCode();
        if (status < HttpURLConnection.HTTP_OK || status > 299) {
            throw new HttpStatusException(status, describe(url, status, httpConnection.getErrorStream()));
        }
        try (InputStream input = httpConnection.getInputStream()) {
            return read(input);
        }
    }

    private static String read(InputStream input) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(reader)) {
            StringBuilder result = new StringBuilder();
            int character;
            while ((character = bufferedReader.read()) != -1) {
                result.append((char) character);
            }
            return result.toString();
        }
    }

    private static String describe(URL url, int status, @Nullable InputStream errorStream) {
        StringBuilder message = new StringBuilder("Server returned HTTP ")
                .append(status)
                .append(" for ")
                .append(url);
        if (errorStream == null) {
            return message.toString();
        }
        try {
            String body = read(errorStream).trim();
            if (!body.isEmpty()) {
                message.append(": ").append(body.length() > MAX_ERROR_BODY_LENGTH
                        ? body.substring(0, MAX_ERROR_BODY_LENGTH) + "..."
                        : body);
            }
        } catch (IOException ignored) {
            // The status is the useful part; a body that cannot be read adds nothing.
        }
        return message.toString();
    }
}
