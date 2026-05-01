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

package eu.decentsoftware.holograms.display;

import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.display.render.DisplayRenderCoordinator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Manages scheduled tasks for display updates.
 *
 * @author d0by
 * @since 2.10.0
 */
public class DisplayUpdateScheduler {

    private static final long LOGICAL_STATE_BASE_INTERVAL_MS = 50;
    private static final long VISIBILITY_BASE_INTERVAL_MS = 1000;
    private static final long POST_PROCESSING_BASE_INTERVAL_MS = 50;

    private long lastLogicalStateTick = 0;
    private long lastVisibilityTick = 0;
    private long lastPostProcessingTick = 0;

    private final ScheduledExecutorService executor;
    private final DisplayService displayService;
    private final DisplayRenderCoordinator renderCoordinator;

    public DisplayUpdateScheduler(DisplayService displayService, DisplayRenderCoordinator renderCoordinator) {
        this.displayService = displayService;
        this.renderCoordinator = renderCoordinator;
        this.executor = Executors.newSingleThreadScheduledExecutor(this::createThread);
    }

    /**
     * Starts all scheduled tasks.
     */
    public void start() {
        executor.scheduleAtFixedRate(this::updateDisplays, 0, 1, TimeUnit.MILLISECONDS);
    }

    /**
     * Stops all scheduled tasks and shuts down executors.
     */
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    Log.error("Failed to shutdown Display Update executor");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void updateDisplays() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - lastLogicalStateTick >= LOGICAL_STATE_BASE_INTERVAL_MS) {
            tickLogicalStates(currentTime);
            lastLogicalStateTick = currentTime;
        }

        if (currentTime - lastVisibilityTick >= VISIBILITY_BASE_INTERVAL_MS) {
            tickVisibility();
            lastVisibilityTick = currentTime;
        }

        if (currentTime - lastPostProcessingTick >= POST_PROCESSING_BASE_INTERVAL_MS) {
            tickPostProcessing();
            lastPostProcessingTick = currentTime;
        }
    }

    private void tickLogicalStates(long currentTime) {
        for (DisplayBase display : displayService.getRegisteredDisplays()) {
            if (shouldUpdateLogicalState(display, currentTime)) {
                renderCoordinator.update(display);
                display.setLastLogicalUpdateMs(System.currentTimeMillis());
            }
        }
    }

    private void tickVisibility() {
        for (DisplayBase display : displayService.getRegisteredDisplays()) {
            renderCoordinator.updateVisibility(display);
        }
    }

    private void tickPostProcessing() {
        for (DisplayBase display : displayService.getRegisteredDisplays()) {
            renderCoordinator.postProcess(display);
        }
    }

    private boolean shouldUpdateLogicalState(DisplayBase display, long currentTime) {
        long lastUpdate = display.getLastLogicalUpdateMs();
        long intervalInTicks = display.getSettings().getUpdateInterval();

        return (currentTime - lastUpdate) >= ticksToMs(intervalInTicks);
    }

    private long ticksToMs(long intervalInTicks) {
        return intervalInTicks * 50L;
    }

    private Thread createThread(Runnable runnable) {
        Thread thread = new Thread(runnable, "DecentHolograms-Display-Updates");
        thread.setDaemon(true);
        thread.setUncaughtExceptionHandler((t, e) -> Log.error("Uncaught exception in thread " + t.getName(), e));
        return thread;
    }
}