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

/**
 * A handle to a task submitted to a {@link PlatformScheduler}.
 *
 * @author d0by
 * @see PlatformScheduler
 * @since 2.10.2
 */
public interface TaskHandle {

    /**
     * Cancels the task.
     *
     * <p>A task that has already run, or has already been canceled, is unaffected. A repeating
     * task stops after any execution currently in progress.</p>
     *
     * @since 2.10.2
     */
    void cancel();

    /**
     * Returns whether the task has been canceled.
     *
     * @return True if this task has been canceled, false otherwise.
     * @since 2.10.2
     */
    boolean isCancelled();
}
