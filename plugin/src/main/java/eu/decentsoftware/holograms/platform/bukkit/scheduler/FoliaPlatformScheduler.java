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
import eu.decentsoftware.holograms.platform.bukkit.player.BukkitPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * A {@link PlatformScheduler} backed by Folia's region schedulers.
 *
 * @author d0by
 * @since 2.10.2
 */
public class FoliaPlatformScheduler implements PlatformScheduler {

    private static final String GLOBAL_SCHEDULER = "io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler";
    private static final String ASYNC_SCHEDULER = "io.papermc.paper.threadedregions.scheduler.AsyncScheduler";
    private static final String ENTITY_SCHEDULER = "io.papermc.paper.threadedregions.scheduler.EntityScheduler";
    private static final String SCHEDULED_TASK = "io.papermc.paper.threadedregions.scheduler.ScheduledTask";

    private static final long MILLIS_PER_TICK = 50L;

    private final Plugin plugin;

    private final Object globalScheduler;
    private final Object asyncScheduler;

    private final Method globalRun;
    private final Method globalRunDelayed;
    private final Method globalRunAtFixedRate;
    private final Method entitySchedulerOf;
    private final Method entityRun;
    private final Method entityRunDelayed;
    private final Method asyncRunNow;
    private final Method asyncRunDelayed;
    private final Method asyncRunAtFixedRate;
    private final Method taskCancel;

    /**
     * @param plugin The plugin tasks are scheduled on behalf of.
     * @throws NullPointerException  If plugin is null.
     * @throws IllegalStateException If the Folia scheduler API is not present. Only construct this
     *                               once the platform has been detected as Folia.
     */
    public FoliaPlatformScheduler(@NotNull Plugin plugin) {
        Objects.requireNonNull(plugin, "plugin cannot be null");
        this.plugin = plugin;
        try {
            Class<?> globalType = Class.forName(GLOBAL_SCHEDULER);
            Class<?> asyncType = Class.forName(ASYNC_SCHEDULER);
            Class<?> entityType = Class.forName(ENTITY_SCHEDULER);
            Class<?> taskType = Class.forName(SCHEDULED_TASK);

            this.globalScheduler = Bukkit.class.getMethod("getGlobalRegionScheduler").invoke(null);
            this.asyncScheduler = Bukkit.class.getMethod("getAsyncScheduler").invoke(null);

            this.globalRun = globalType.getMethod("run", Plugin.class, Consumer.class);
            this.globalRunDelayed = globalType.getMethod("runDelayed", Plugin.class, Consumer.class, long.class);
            this.globalRunAtFixedRate =
                    globalType.getMethod("runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class);

            this.entitySchedulerOf = Entity.class.getMethod("getScheduler");
            this.entityRun = entityType.getMethod("run", Plugin.class, Consumer.class, Runnable.class);
            this.entityRunDelayed =
                    entityType.getMethod("runDelayed", Plugin.class, Consumer.class, Runnable.class, long.class);

            this.asyncRunNow = asyncType.getMethod("runNow", Plugin.class, Consumer.class);
            this.asyncRunDelayed =
                    asyncType.getMethod("runDelayed", Plugin.class, Consumer.class, long.class, TimeUnit.class);
            this.asyncRunAtFixedRate = asyncType.getMethod(
                    "runAtFixedRate", Plugin.class, Consumer.class, long.class, long.class, TimeUnit.class);

            this.taskCancel = taskType.getMethod("cancel");
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Folia scheduler API is not available on this server.", e);
        }
    }

    @NotNull
    @Override
    public TaskHandle runGlobal(@NotNull Runnable task) {
        Objects.requireNonNull(task, "task cannot be null");
        return schedule(globalRun, globalScheduler, plugin, consume(task));
    }

    @NotNull
    @Override
    public TaskHandle runGlobalLater(@NotNull Runnable task, long delayTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        return schedule(globalRunDelayed, globalScheduler, plugin, consume(task), atLeastOneTick(delayTicks));
    }

    @NotNull
    @Override
    public TaskHandle runGlobalTimer(@NotNull Runnable task, long delayTicks, long periodTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        return schedule(
                globalRunAtFixedRate, globalScheduler, plugin, consume(task), atLeastOneTick(delayTicks), atLeastOneTick(periodTicks));
    }

    @NotNull
    @Override
    public TaskHandle runForPlayer(@NotNull PlatformPlayer player, @NotNull Runnable task) {
        Objects.requireNonNull(task, "task cannot be null");
        // A null "retired" callback means: if the player is gone before this runs, drop it.
        return schedule(entityRun, schedulerOf(player), plugin, consume(task), null);
    }

    @NotNull
    @Override
    public TaskHandle runForPlayerLater(@NotNull PlatformPlayer player, @NotNull Runnable task, long delayTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        return schedule(entityRunDelayed, schedulerOf(player), plugin, consume(task), null, atLeastOneTick(delayTicks));
    }

    @NotNull
    @Override
    public TaskHandle runAsync(@NotNull Runnable task) {
        Objects.requireNonNull(task, "task cannot be null");
        return schedule(asyncRunNow, asyncScheduler, plugin, consume(task));
    }

    @NotNull
    @Override
    public TaskHandle runAsyncLater(@NotNull Runnable task, long delayTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        // The async scheduler measures real time rather than ticks.
        return schedule(asyncRunDelayed, asyncScheduler, plugin, consume(task), toMillis(delayTicks), TimeUnit.MILLISECONDS);
    }

    @NotNull
    @Override
    public TaskHandle runAsyncTimer(@NotNull Runnable task, long delayTicks, long periodTicks) {
        Objects.requireNonNull(task, "task cannot be null");
        return schedule(asyncRunAtFixedRate, asyncScheduler, plugin, consume(task),
                toMillis(delayTicks), toMillis(periodTicks), TimeUnit.MILLISECONDS);
    }

    private Object schedulerOf(PlatformPlayer player) {
        Objects.requireNonNull(player, "player cannot be null");
        if (!(player instanceof BukkitPlayer)) {
            throw new IllegalArgumentException("Player must be of type " + BukkitPlayer.class.getName());
        }
        Player bukkitPlayer = ((BukkitPlayer) player).getBukkitPlayer();
        try {
            return entitySchedulerOf.invoke(bukkitPlayer);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to obtain the entity scheduler for " + bukkitPlayer.getName(), e);
        }
    }

    private TaskHandle schedule(Method method, Object scheduler, Object... args) {
        try {
            return new FoliaTaskHandle(method.invoke(scheduler, args), taskCancel);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Failed to schedule a task through Folia.", e);
        }
    }

    private static Consumer<Object> consume(Runnable task) {
        return scheduledTask -> task.run();
    }

    /**
     * Folia rejects a delay or period below one tick, where Bukkit silently treats it as "next tick".
     * Raising it keeps the two platforms behaving the same for callers.
     */
    private static long atLeastOneTick(long ticks) {
        return Math.max(1L, ticks);
    }

    private static long toMillis(long ticks) {
        return atLeastOneTick(ticks) * MILLIS_PER_TICK;
    }

    private static final class FoliaTaskHandle implements TaskHandle {

        private final Object scheduledTask;
        private final Method cancel;
        private volatile boolean cancelled;

        private FoliaTaskHandle(@Nullable Object scheduledTask, Method cancel) {
            this.scheduledTask = scheduledTask;
            this.cancel = cancel;
            // The entity schedulers return null when the player has already been removed, which
            // means the task will never run - indistinguishable from having been canceled.
            this.cancelled = scheduledTask == null;
        }

        @Override
        public void cancel() {
            cancelled = true;
            if (scheduledTask == null) {
                return;
            }
            try {
                cancel.invoke(scheduledTask);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Failed to cancel a Folia task.", e);
            }
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }
    }
}
