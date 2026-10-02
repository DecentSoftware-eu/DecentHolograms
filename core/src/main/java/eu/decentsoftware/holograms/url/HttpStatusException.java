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

import java.io.IOException;

/**
 * Thrown when a request completes but the server reports a failure.
 *
 * @author d0by
 * @since 2.10.2
 */
public class HttpStatusException extends IOException {

    private static final int CLIENT_ERROR_MIN = 400;
    private static final int CLIENT_ERROR_MAX = 499;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int TOO_MANY_REQUESTS = 429;

    private final int statusCode;

    public HttpStatusException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * @return The HTTP status the server responded with.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Whether the request itself was rejected, as opposed to the server being unable to serve it.
     *
     * <p>Timeouts and rate limiting are excluded: both are 4xx but will succeed if tried again,
     * so treating them as permanent would be wrong.</p>
     *
     * @return True if repeating this request unchanged would fail the same way.
     */
    public boolean isPermanentRejection() {
        return statusCode >= CLIENT_ERROR_MIN && statusCode <= CLIENT_ERROR_MAX
                && statusCode != REQUEST_TIMEOUT
                && statusCode != TOO_MANY_REQUESTS;
    }
}
