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

package eu.decentsoftware.holograms.text;

import eu.decentsoftware.holograms.platform.api.text.TextFormatter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CachingTextFormatterTest {

    private static final long TIMEOUT_MS = 1_000;
    /**
     * How long a parked delegate call waits to be released. Comfortably longer than
     * {@link #TIMEOUT_MS} so that a thread waiting on the cache lock always gives up first,
     * making the join assertions decisive rather than a race between two timeouts.
     */
    private static final long PARK_TIMEOUT_MS = 2 * TIMEOUT_MS;

    private final CountingFormatter delegate = new CountingFormatter();
    private final List<Throwable> threadFailures = new CopyOnWriteArrayList<>();

    @Test
    void testDelegatesOnCacheMiss() {
        CachingTextFormatter formatter = new CachingTextFormatter(delegate);

        assertEquals("formatted:&aHello", formatter.format("&aHello"));
        assertEquals(1, delegate.countFor("&aHello"));
    }

    @Test
    void testCachesRepeatedInput() {
        CachingTextFormatter formatter = new CachingTextFormatter(delegate);

        for (int i = 0; i < 10; i++) {
            assertEquals("formatted:&aHello", formatter.format("&aHello"));
        }
        assertEquals(1, delegate.countFor("&aHello"));
    }

    @Test
    void testCachesEachDistinctInputSeparately() {
        CachingTextFormatter formatter = new CachingTextFormatter(delegate);

        assertEquals("formatted:one", formatter.format("one"));
        assertEquals("formatted:two", formatter.format("two"));
        assertEquals("formatted:one", formatter.format("one"));
        assertEquals("formatted:two", formatter.format("two"));

        assertEquals(1, delegate.countFor("one"));
        assertEquals(1, delegate.countFor("two"));
    }

    @Test
    void testInvalidateClearsCache() {
        CachingTextFormatter formatter = new CachingTextFormatter(delegate);

        formatter.format("&aHello");
        formatter.invalidate();
        assertEquals("formatted:&aHello", formatter.format("&aHello"));

        assertEquals(2, delegate.countFor("&aHello"));
    }

    @Test
    void testEvictsLeastRecentlyUsedEntry() {
        CachingTextFormatter formatter = new CachingTextFormatter(delegate, 2);

        formatter.format("a");
        formatter.format("b");
        formatter.format("a"); // "b" is now the least recently used entry
        formatter.format("c"); // evicts "b"

        formatter.format("a");
        assertEquals(1, delegate.countFor("a"), "\"a\" was recently used and should still be cached");
        formatter.format("b");
        assertEquals(2, delegate.countFor("b"), "\"b\" should have been evicted and recomputed");
    }

    @Test
    void testRejectsNonPositiveMaxSize() {
        assertThrows(IllegalArgumentException.class, () -> new CachingTextFormatter(delegate, 0));
        assertThrows(IllegalArgumentException.class, () -> new CachingTextFormatter(delegate, -1));
    }

    /**
     * Two threads format more distinct keys than the cache can hold, so it evicts constantly.
     * Afterward the cache must still be within its bound.
     *
     * <p>The bound is what makes this test meaningful. An unguarded {@link java.util.LinkedHashMap}
     * keeps returning correct results under this load — a lost entry merely costs a recomputation —
     * but its eviction bookkeeping breaks, so it retains every key and grows without limit.</p>
     */
    @Test
    void testConcurrentFormattingHonoursCacheSizeBound() throws Exception {
        int cacheSize = 8;
        int distinctKeys = 50;
        int iterations = 5000;
        CachingTextFormatter formatter = new CachingTextFormatter(delegate, cacheSize);
        CountDownLatch start = new CountDownLatch(1);

        Thread first = startThread(() -> hammer(formatter, start, 0, distinctKeys, iterations));
        Thread second = startThread(() -> hammer(formatter, start, distinctKeys / 2, distinctKeys, iterations));
        start.countDown();
        join(first, "first formatting thread");
        join(second, "second formatting thread");
        assertNoThreadFailures();

        int retained = countCachedKeys(formatter, distinctKeys);
        assertTrue(retained <= cacheSize,
                "Cache retained " + retained + " entries, exceeding its bound of " + cacheSize);
    }

    /**
     * Parks a thread inside the delegate and checks that another thread can still format.
     *
     * <p>This pins down the decision to call the delegate outside the lock: the delegate is
     * arbitrary platform code, so holding the lock across it would let an unrelated caller be
     * blocked by it. Widening the lock to cover the delegate call makes this test time out.</p>
     */
    @Test
    void testDoesNotHoldLockWhileDelegateRuns() throws Exception {
        CachingTextFormatter formatter = new CachingTextFormatter(delegate);
        delegate.blockCallsTo("slow");

        Thread parked = startThread(() -> assertEquals("formatted:slow", formatter.format("slow")));
        assertTrue(delegate.awaitCall("slow"), "the delegate was never called");

        // "slow" is parked mid-delegate. Formatting anything else must not wait for it.
        Thread other = startThread(() -> assertEquals("formatted:other", formatter.format("other")));
        join(other, "second formatting thread");

        delegate.releaseCall("slow");
        join(parked, "parked formatting thread");
        assertNoThreadFailures();
    }

    /**
     * Invalidates the cache while a call to the delegate is still in flight. The invalidation must
     * not wait for it, and the in-flight call must still produce a correct result afterward.
     */
    @Test
    void testInvalidateDuringInFlightFormat() throws Exception {
        CachingTextFormatter formatter = new CachingTextFormatter(delegate);
        delegate.blockCallsTo("test");

        Thread parked = startThread(() -> assertEquals("formatted:test", formatter.format("test")));
        assertTrue(delegate.awaitCall("test"), "the delegate was never called");

        Thread invalidator = startThread(formatter::invalidate);
        join(invalidator, "invalidating thread");

        delegate.releaseCall("test");
        join(parked, "parked formatting thread");
        assertNoThreadFailures();
    }

    private void hammer(CachingTextFormatter formatter, CountDownLatch start, int offset, int distinctKeys, int iterations) throws InterruptedException {
        start.await();
        for (int i = 0; i < iterations; i++) {
            String key = "line-" + ((offset + i) % distinctKeys);
            assertEquals("formatted:" + key, formatter.format(key));
        }
    }

    /**
     * Counts how many keys are still cached, by formatting each one and observing which ones
     * never reach the delegate.
     *
     * <p>Probing a missing key inserts it and may evict a key not yet probed, so this can
     * undercount. That only ever makes the bound assertion more lenient, never falsely strict.</p>
     */
    private int countCachedKeys(CachingTextFormatter formatter, int distinctKeys) {
        int retained = 0;
        for (int i = 0; i < distinctKeys; i++) {
            String key = "line-" + i;
            int callsBefore = delegate.countFor(key);
            formatter.format(key);
            if (delegate.countFor(key) == callsBefore) {
                retained++;
            }
        }
        return retained;
    }

    private Thread startThread(ThrowingRunnable body) {
        Thread thread = new Thread(() -> {
            try {
                body.run();
            } catch (Throwable failure) {
                threadFailures.add(failure);
            }
        });
        thread.setDaemon(true);
        thread.start();
        return thread;
    }

    private void join(Thread thread, String description) throws InterruptedException {
        thread.join(TIMEOUT_MS);
        assertFalse(thread.isAlive(), description + " did not finish within " + TIMEOUT_MS + "ms");
    }

    private void assertNoThreadFailures() {
        if (!threadFailures.isEmpty()) {
            throw new AssertionError("A test thread failed", threadFailures.get(0));
        }
    }

    @FunctionalInterface
    private interface ThrowingRunnable {
        void run() throws Exception;
    }

    /**
     * A pure {@link TextFormatter} that records how often it was asked to format each input, and
     * can be made to park inside a call so that an interleaving can be arranged deterministically.
     */
    private static final class CountingFormatter implements TextFormatter {

        private final Map<String, AtomicInteger> counts = new ConcurrentHashMap<>();
        private final Map<String, CountDownLatch> entered = new ConcurrentHashMap<>();
        private final Map<String, CountDownLatch> released = new ConcurrentHashMap<>();

        @NotNull
        @Override
        public String format(@NotNull String text) {
            counts.computeIfAbsent(text, key -> new AtomicInteger()).incrementAndGet();
            enteredLatch(text).countDown();
            CountDownLatch release = released.get(text);
            if (release != null) {
                await(release);
            }
            return "formatted:" + text;
        }

        int countFor(String text) {
            AtomicInteger count = counts.get(text);
            return count == null ? 0 : count.get();
        }

        /**
         * Makes calls for the given text park inside the delegate until {@link #releaseCall(String)}.
         * Must be called before the formatting thread starts, otherwise it may run straight through.
         */
        void blockCallsTo(String text) {
            released.putIfAbsent(text, new CountDownLatch(1));
        }

        /**
         * Waits for a call for the given text to reach the delegate.
         *
         * @return whether the delegate was called before the timeout elapsed.
         */
        boolean awaitCall(String text) throws InterruptedException {
            return enteredLatch(text).await(TIMEOUT_MS, TimeUnit.MILLISECONDS);
        }

        void releaseCall(String text) {
            CountDownLatch release = released.remove(text);
            if (release != null) {
                release.countDown();
            }
        }

        private CountDownLatch enteredLatch(String text) {
            return entered.computeIfAbsent(text, key -> new CountDownLatch(1));
        }

        private void await(CountDownLatch latch) {
            try {
                assertTrue(latch.await(PARK_TIMEOUT_MS, TimeUnit.MILLISECONDS), "delegate call did not release within " + PARK_TIMEOUT_MS + "ms");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
