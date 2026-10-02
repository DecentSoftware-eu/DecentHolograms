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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LogTest {

    private RecordingHandler handler;

    @BeforeEach
    void setUp() {
        handler = new RecordingHandler();
        Logger logger = Logger.getLogger("LogTest-" + System.nanoTime());
        logger.setUseParentHandlers(false);
        logger.setLevel(Level.ALL);
        logger.addHandler(handler);
        Log.setLogger(logger);
    }

    @AfterEach
    void tearDown() {
        Log.setLogger(Logger.getLogger("DecentHolograms"));
    }

    @Nested
    class LoggerBindingTests {

        @Test
        void logsBeforeAnyLoggerIsSet() {
            assertDoesNotThrow(() -> Log.info("nothing is bound yet"));
        }

        @Test
        void acceptsBeingSetMoreThanOnce() {
            Logger second = Logger.getLogger("LogTest-second");

            assertDoesNotThrow(() -> Log.setLogger(second));
        }

        @Test
        void rejectsNullLogger() {
            Exception exception = assertThrows(NullPointerException.class, () -> Log.setLogger(null));

            assertEquals("logger cannot be null", exception.getMessage());
        }
    }

    @Nested
    class LevelTests {

        @Test
        void infoLogsAtInfo() {
            Log.info("hello");

            assertEquals(Level.INFO, handler.single().getLevel());
            assertEquals("hello", handler.message());
        }

        @Test
        void warnLogsAtWarning() {
            Log.warn("careful");

            assertEquals(Level.WARNING, handler.single().getLevel());
        }

        @Test
        void errorLogsAtSevere() {
            Log.error("broken");

            assertEquals(Level.SEVERE, handler.single().getLevel());
        }
    }

    @Nested
    class FormattingTests {

        @Test
        void formatsArguments() {
            Log.info("player %s joined world %s", "d0by", "world");

            assertEquals("player d0by joined world world", handler.message());
        }

        @Test
        void attachesThrowables() {
            RuntimeException cause = new RuntimeException("boom");

            Log.error("failed", cause);

            assertSame(cause, handler.single().getThrown());
            assertEquals("failed", handler.message());
        }

        @Test
        void formatsArgumentsAlongsideAThrowable() {
            RuntimeException cause = new RuntimeException("boom");

            Log.error("failed for %s", cause, "d0by");

            assertSame(cause, handler.single().getThrown());
            assertEquals("failed for d0by", handler.message());
        }

        @Test
        void singleArgumentLoggingDoesNotFormat() {
            // Picks the non-varargs overload, so the text is passed through untouched. That makes
            // Log.info(someUserSuppliedText) safe; adding an argument would run it through
            // String.format and a stray % would then throw from the call site.
            assertDoesNotThrow(() -> Log.info("literal percent: 100% complete"));

            assertEquals("literal percent: 100% complete", handler.message());
        }
    }

    private static final class RecordingHandler extends Handler {

        private final List<LogRecord> records = new ArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
            // Nothing buffered.
        }

        @Override
        public void close() {
            // Nothing to release.
        }

        private LogRecord single() {
            assertEquals(1, records.size(), "expected exactly one record, got " + records.size());
            return records.get(0);
        }

        private String message() {
            LogRecord record = single();
            return record.getMessage();
        }
    }
}
