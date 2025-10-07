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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

/**
 * Utility class for reading data from a URL.
 * This class provides methods to read the content of a URL as a String.
 *
 * @author d0by
 * @since 2.9.6
 */
public class UrlReader {

    private UrlReader() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated.");
    }

    /**
     * Reads the content of the specified URL and returns it as a String.
     *
     * @param url The URL to read from.
     * @return The content of the URL as a String.
     * @throws IOException          If an I/O error occurs while reading from the URL.
     * @throws NullPointerException If the provided URL is null.
     * @since 2.9.6
     */
    public static String readString(@NotNull URL url) throws IOException {
        Objects.requireNonNull(url, "url cannot be null");

        try (InputStream input = url.openStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(input);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder result = new StringBuilder();
            int character;
            while ((character = bufferedReader.read()) != -1) {
                result.append((char) character);
            }
            return result.toString();
        }
    }
}
