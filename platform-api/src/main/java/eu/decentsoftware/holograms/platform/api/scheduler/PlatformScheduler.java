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

package eu.decentsoftware.holograms.platform.api.scheduler;

import eu.decentsoftware.holograms.platform.api.player.PlatformPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Schedules work on the platform.
 *
 * <p>Tasks are described by <i>what they touch</i> rather than by which thread they run on.
 * On a single-threaded platform every non-async variant ends up on the main thread, but on a
 * platform that owns state per region - Folia, for example - the distinction decides which
 * thread may legally run the task. A scheduler that only offered "sync" could not express
 * this, so the split is part of the contract rather than an implementation detail.</p>
 *
 * <p>Delays and periods are given in ticks, where one tick is 50ms.</p>
 *
 * @author d0by
 * @see TaskHandle
 * @since 2.10.2
 */
public interface PlatformScheduler {

    /**
     * Runs a task that touches only server-wide state.
     *
     * @param task The task.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runGlobal(@NotNull Runnable task);

    /**
     * Runs a task that touches only server-wide state, after a delay.
     *
     * @param task       The task.
     * @param delayTicks Ticks to wait before running the task.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runGlobalLater(@NotNull Runnable task, long delayTicks);

    /**
     * Repeatedly runs a task that touches only server-wide state.
     *
     * @param task        The task.
     * @param delayTicks  Ticks to wait before the first execution.
     * @param periodTicks Ticks between executions.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runGlobalTimer(@NotNull Runnable task, long delayTicks, long periodTicks);

    /**
     * Runs a task that touches the given player, or the world around them.
     *
     * <p>If the player leaves before the task runs, the task is silently dropped.</p>
     *
     * @param player The player the task acts on.
     * @param task   The task.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runForPlayer(@NotNull PlatformPlayer player, @NotNull Runnable task);

    /**
     * Runs a task that touches the given player, or the world around them, after a delay.
     *
     * <p>If the player leaves before the task runs, the task is silently dropped.</p>
     *
     * @param player     The player the task acts on.
     * @param task       The task.
     * @param delayTicks Ticks to wait before running the task.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runForPlayerLater(@NotNull PlatformPlayer player, @NotNull Runnable task, long delayTicks);

    /**
     * Runs a task that touches no platform state.
     *
     * @param task The task.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runAsync(@NotNull Runnable task);

    /**
     * Runs a task that touches no platform state, after a delay.
     *
     * @param task       The task.
     * @param delayTicks Ticks to wait before running the task.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runAsyncLater(@NotNull Runnable task, long delayTicks);

    /**
     * Repeatedly runs a task that touches no platform state.
     *
     * @param task        The task.
     * @param delayTicks  Ticks to wait before the first execution.
     * @param periodTicks Ticks between executions.
     * @return A handle to the scheduled task.
     * @since 2.10.2
     */
    @NotNull
    TaskHandle runAsyncTimer(@NotNull Runnable task, long delayTicks, long periodTicks);
}
