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

package eu.decentsoftware.holograms.logging;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for logging.
 *
 * <p>Falls back to a standalone logger until {@link #setLogger(Logger)} is called, so logging
 * is always safe to use, including before the plugin is enabled and in tests.</p>
 *
 * @author d0by
 * @since 2.8.9
 */
public final class Log {

    private static volatile Logger logger = Logger.getLogger("DecentHolograms");

    private Log() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Sets the logger to be used by this utility class.
     *
     * @param logger The logger to set.
     * @throws NullPointerException If the provided logger is null.
     * @since 2.9.6
     */
    public static void setLogger(@NotNull Logger logger) {
        Objects.requireNonNull(logger, "logger cannot be null");
        Log.logger = logger;
    }

    public static void info(String message) {
        logger.info(message);
    }

    public static void info(String message, Object... args) {
        logger.info(() -> String.format(message, args));
    }

    public static void info(String message, Throwable throwable) {
        logger.log(Level.INFO, message, throwable);
    }

    public static void info(String message, Throwable throwable, Object... args) {
        logger.log(Level.INFO, throwable, () -> String.format(message, args));
    }

    public static void warn(String message) {
        logger.warning(message);
    }

    public static void warn(String message, Object... args) {
        logger.warning(() -> String.format(message, args));
    }

    public static void warn(String message, Throwable throwable) {
        logger.log(Level.WARNING, message, throwable);
    }

    public static void warn(String message, Throwable throwable, Object... args) {
        logger.log(Level.WARNING, throwable, () -> String.format(message, args));
    }

    public static void error(String message) {
        logger.severe(message);
    }

    public static void error(String message, Object... args) {
        logger.severe(() -> String.format(message, args));
    }

    public static void error(String message, Throwable throwable) {
        logger.log(Level.SEVERE, message, throwable);
    }

    public static void error(String message, Throwable throwable, Object... args) {
        logger.log(Level.SEVERE, throwable, () -> String.format(message, args));
    }
}
