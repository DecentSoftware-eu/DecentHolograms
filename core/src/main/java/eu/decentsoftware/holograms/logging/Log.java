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
import org.jetbrains.annotations.TestOnly;

import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for logging.
 *
 * @author d0by
 * @since 2.8.9
 */
public final class Log {

    private static Logger logger;

    private Log() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Sets the logger to be used by this utility class.
     *
     * @param logger The logger to set.
     * @throws NullPointerException  If the provided logger is null.
     * @throws IllegalStateException If the logger has already been set.
     * @since 2.9.6
     */
    public static void setLogger(@NotNull Logger logger) {
        Objects.requireNonNull(logger, "logger cannot be null");
        if (Log.logger != null) {
            throw new IllegalStateException("Logger is already set. Cannot set it again.");
        }
        Log.logger = logger;
    }

    public static void info(String message) {
        checkLoggerAvailable();
        logger.info(message);
    }

    public static void info(String message, Object... args) {
        checkLoggerAvailable();
        logger.info(() -> String.format(message, args));
    }

    public static void info(String message, Throwable throwable) {
        checkLoggerAvailable();
        logger.log(Level.INFO, message, throwable);
    }

    public static void info(String message, Throwable throwable, Object... args) {
        checkLoggerAvailable();
        logger.log(Level.INFO, throwable, () -> String.format(message, args));
    }

    public static void warn(String message) {
        checkLoggerAvailable();
        logger.warning(message);
    }

    public static void warn(String message, Object... args) {
        checkLoggerAvailable();
        logger.warning(() -> String.format(message, args));
    }

    public static void warn(String message, Throwable throwable) {
        checkLoggerAvailable();
        logger.log(Level.WARNING, message, throwable);
    }

    public static void warn(String message, Throwable throwable, Object... args) {
        checkLoggerAvailable();
        logger.log(Level.WARNING, throwable, () -> String.format(message, args));
    }

    public static void error(String message) {
        checkLoggerAvailable();
        logger.severe(message);
    }

    public static void error(String message, Object... args) {
        checkLoggerAvailable();
        logger.severe(() -> String.format(message, args));
    }

    public static void error(String message, Throwable throwable) {
        checkLoggerAvailable();
        logger.log(Level.SEVERE, message, throwable);
    }

    public static void error(String message, Throwable throwable, Object... args) {
        checkLoggerAvailable();
        logger.log(Level.SEVERE, throwable, () -> String.format(message, args));
    }

    /**
     * Checks if the logger is available and throws an exception if it is not set.
     *
     * @throws IllegalStateException If the logger is not set.
     * @since 2.9.6
     */
    private static void checkLoggerAvailable() {
        if (logger == null) {
            throw new IllegalStateException("Logger is not set. Please set a logger using Log#setLogger before using logging methods.");
        }
    }

    /**
     * Initializes the logger for testing purposes.
     * This method should only be used in test environments to set up the logger.
     * It should not be used in production code.
     *
     * @throws IllegalStateException If the logger has already been set.
     * @since 2.9.6
     */
    @TestOnly
    public static void initializeForTests() {
        // This is hopefully just a temporary solution.
        // Eventually, we should just do logging properly, maybe using SLF4J or similar,
        // and get rid of this class.
        if (logger == null) {
            logger = Logger.getLogger("DecentHolograms");
        }
    }
}