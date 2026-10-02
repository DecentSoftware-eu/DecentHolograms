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

package eu.decentsoftware.holograms.platform.bukkit.scheduler;

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import eu.decentsoftware.holograms.platform.api.scheduler.TaskHandle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BukkitPlatformSchedulerTest {

    @Mock
    private Plugin plugin;
    @Mock
    private BukkitScheduler bukkitScheduler;
    @Mock
    private BukkitTask bukkitTask;
    @Mock
    private Runnable task;
    @InjectMocks
    private BukkitPlatformScheduler scheduler;
    private MockedStatic<Bukkit> bukkitMock;

    @BeforeEach
    void setUp() {
        bukkitMock = mockStatic(Bukkit.class);
        bukkitMock.when(Bukkit::getScheduler).thenReturn(bukkitScheduler);
    }

    @AfterEach
    void tearDown() {
        bukkitMock.close();
    }

    @Nested
    class GlobalTaskTests {

        @Test
        void runGlobalUsesRunTask() {
            when(bukkitScheduler.runTask(eq(plugin), any(Runnable.class))).thenReturn(bukkitTask);

            scheduler.runGlobal(task);

            verify(bukkitScheduler).runTask(plugin, task);
        }

        @Test
        void runGlobalLaterPassesDelay() {
            when(bukkitScheduler.runTaskLater(eq(plugin), any(Runnable.class), eq(20L))).thenReturn(bukkitTask);

            scheduler.runGlobalLater(task, 20L);

            verify(bukkitScheduler).runTaskLater(plugin, task, 20L);
        }

        @Test
        void runGlobalTimerPassesDelayAndPeriod() {
            when(bukkitScheduler.runTaskTimer(eq(plugin), any(Runnable.class), eq(5L), eq(10L))).thenReturn(bukkitTask);

            scheduler.runGlobalTimer(task, 5L, 10L);

            verify(bukkitScheduler).runTaskTimer(plugin, task, 5L, 10L);
        }
    }

    @Nested
    class PlayerTaskTests {

        @Test
        void runForPlayerRunsOnTheMainThread() {
            when(bukkitScheduler.runTask(eq(plugin), any(Runnable.class))).thenReturn(bukkitTask);

            // Bukkit has no per-entity scheduling
            scheduler.runForPlayer(mock(PlatformPlayer.class), task);

            verify(bukkitScheduler).runTask(plugin, task);
        }

        @Test
        void runForPlayerLaterRunsOnTheMainThread() {
            when(bukkitScheduler.runTaskLater(eq(plugin), any(Runnable.class), eq(3L))).thenReturn(bukkitTask);

            scheduler.runForPlayerLater(mock(PlatformPlayer.class), task, 3L);

            verify(bukkitScheduler).runTaskLater(plugin, task, 3L);
        }
    }

    @Nested
    class AsyncTaskTests {

        @Test
        void runAsyncUsesRunTaskAsynchronously() {
            when(bukkitScheduler.runTaskAsynchronously(eq(plugin), any(Runnable.class))).thenReturn(bukkitTask);

            scheduler.runAsync(task);

            verify(bukkitScheduler).runTaskAsynchronously(plugin, task);
        }

        @Test
        void runAsyncLaterPassesDelay() {
            when(bukkitScheduler.runTaskLaterAsynchronously(eq(plugin), any(Runnable.class), eq(7L)))
                    .thenReturn(bukkitTask);

            scheduler.runAsyncLater(task, 7L);

            verify(bukkitScheduler).runTaskLaterAsynchronously(plugin, task, 7L);
        }

        @Test
        void runAsyncTimerPassesDelayAndPeriod() {
            when(bukkitScheduler.runTaskTimerAsynchronously(eq(plugin), any(Runnable.class), eq(1L), eq(5L)))
                    .thenReturn(bukkitTask);

            scheduler.runAsyncTimer(task, 1L, 5L);

            verify(bukkitScheduler).runTaskTimerAsynchronously(plugin, task, 1L, 5L);
        }
    }

    @Nested
    class TaskHandleTests {

        @Test
        void cancelDelegatesToTheBukkitTask() {
            when(bukkitScheduler.runTask(eq(plugin), any(Runnable.class))).thenReturn(bukkitTask);

            TaskHandle handle = scheduler.runGlobal(task);
            assertFalse(handle.isCancelled());

            handle.cancel();

            verify(bukkitTask).cancel();
            assertTrue(handle.isCancelled());
        }
    }

    @Nested
    class ValidationTests {

        @Test
        void rejectsNullPlugin() {
            Exception exception = assertThrows(NullPointerException.class,
                    () -> new BukkitPlatformScheduler(null));

            assertEquals("plugin cannot be null", exception.getMessage());
        }

        @Test
        void rejectsNullTask() {
            assertThrows(NullPointerException.class, () -> scheduler.runGlobal(null));
            assertThrows(NullPointerException.class, () -> scheduler.runGlobalLater(null, 1L));
            assertThrows(NullPointerException.class, () -> scheduler.runGlobalTimer(null, 1L, 1L));
            assertThrows(NullPointerException.class, () -> scheduler.runAsync(null));
            assertThrows(NullPointerException.class, () -> scheduler.runAsyncLater(null, 1L));
            assertThrows(NullPointerException.class, () -> scheduler.runAsyncTimer(null, 1L, 1L));
        }

        @Test
        void rejectsNullPlayer() {
            Exception exception = assertThrows(NullPointerException.class,
                    () -> scheduler.runForPlayer(null, task));

            assertEquals("player cannot be null", exception.getMessage());
        }
    }
}
