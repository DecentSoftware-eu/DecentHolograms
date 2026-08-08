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
import eu.decentsoftware.holograms.platform.api.scheduler.PlatformScheduler;
import eu.decentsoftware.holograms.platform.api.scheduler.TaskHandle;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

/**
 * A {@link PlatformScheduler} backed by the Bukkit scheduler.
 *
 * @author d0by
 * @since 2.10.2
 */
public class BukkitPlatformScheduler implements PlatformScheduler {

    private final Plugin plugin;

    /**
     * Creates a new {@link BukkitPlatformScheduler} instance.
     *
     * @param plugin The plugin tasks are scheduled on behalf of.
     * @throws NullPointerException if plugin is null.
     */
    public BukkitPlatformScheduler(@NotNull Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin cannot be null");
        this.plugin = plugin;
    }

    @NotNull
    @Override
    public TaskHandle runGlobal(@NotNull Runnable task) {
        Objects.requireNonNull(task, "task cannot be null");
        return new BukkitTaskHandle(Bukkit.getScheduler().runTask(plugin, task));
    }

    @NotNull
    @Override
    public TaskHandle runGlobalLater(@NotNull Runnable task, long delayTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLater(plugin, task, delayTicks));
    }

    @NotNull
    @Override
    public TaskHandle runGlobalTimer(@NotNull Runnable task, long delayTicks, long periodTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimer(plugin, task, delayTicks, periodTicks));
    }

    @NotNull
    @Override
    public TaskHandle runForPlayer(@NotNull PlatformPlayer player, @NotNull Runnable task) {
        Objects.requireNonNull(player, "player cannot be null");
        return runGlobal(task);
    }

    @NotNull
    @Override
    public TaskHandle runForPlayerLater(@NotNull PlatformPlayer player, @NotNull Runnable task, long delayTicks) {
        Objects.requireNonNull(player, "player cannot be null");
        return runGlobalLater(task, delayTicks);
    }

    @NotNull
    @Override
    public TaskHandle runAsync(@NotNull Runnable task) {
        Objects.requireNonNull(task, "task cannot be null");
        try {
            return new BukkitTaskHandle(Bukkit.getScheduler().runTaskAsynchronously(plugin, task));
        } catch (IllegalPluginAccessException e) {
            // The plugin is being disabled and the scheduler will not accept new work.
            return new FutureTaskHandle(CompletableFuture.runAsync(task));
        }
    }

    @NotNull
    @Override
    public TaskHandle runAsyncLater(@NotNull Runnable task, long delayTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        try {
            return new BukkitTaskHandle(Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, task, delayTicks));
        } catch (IllegalPluginAccessException e) {
            return new FutureTaskHandle(CompletableFuture.runAsync(task));
        }
    }

    @NotNull
    @Override
    public TaskHandle runAsyncTimer(@NotNull Runnable task, long delayTicks, long periodTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        return new BukkitTaskHandle(Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, task, delayTicks, periodTicks));
    }

    private static final class BukkitTaskHandle implements TaskHandle {

        private final BukkitTask task;
        private volatile boolean cancelled;

        private BukkitTaskHandle(BukkitTask task) {
            this.task = task;
        }

        @Override
        public void cancel() {
            cancelled = true;
            task.cancel();
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }
    }

    private static final class FutureTaskHandle implements TaskHandle {

        private final CompletableFuture<Void> future;

        private FutureTaskHandle(CompletableFuture<Void> future) {
            this.future = future;
        }

        @Override
        public void cancel() {
            future.cancel(true);
        }

        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }
    }
}
