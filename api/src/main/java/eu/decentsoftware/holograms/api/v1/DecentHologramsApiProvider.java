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

package eu.decentsoftware.holograms.api.v1;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * !! INTERNAL USE ONLY !!
 * <p>
 * Use {@link DecentHologramsApi#getInstance(Plugin)} to get an instance of the API.
 *
 * @author d0by
 * @see DecentHologramsApi#getInstance(Plugin)
 * @since 2.10.0
 */
@ApiStatus.Internal
abstract class DecentHologramsApiProvider {

    private static final String UNINITIALIZED_ERROR_MESSAGE = "DecentHolograms API is not initialized."
            + " Please ensure that the DecentHolograms plugin is loaded before accessing the API.";
    private static final String LOG_MESSAGE_TEMPLATE = "Caught plugin %s v%s (by %s) trying to access DecentHolograms API"
            + " when DecentHolograms is not initialized. Make sure you have DecentHolograms as a dependency in your plugin.yml"
            + " and that it is loaded before you try to access the API.";

    private static DecentHologramsApiProvider implementation;

    @Contract(pure = true)
    @ApiStatus.Internal
    static DecentHologramsApiProvider getImplementation(@NotNull Plugin plugin) {
        if (DecentHologramsApiProvider.implementation == null) {
            logUninitializedAccessError(plugin);
            throw new IllegalStateException(UNINITIALIZED_ERROR_MESSAGE);
        }
        return DecentHologramsApiProvider.implementation;
    }

    private static void logUninitializedAccessError(@NotNull Plugin plugin) {
        String logMessage = String.format(LOG_MESSAGE_TEMPLATE, plugin.getName(),
                plugin.getDescription().getVersion(), plugin.getDescription().getAuthors());
        plugin.getLogger().severe(logMessage);
    }

    @ApiStatus.Internal
    static void setImplementation(DecentHologramsApiProvider implementation) {
        if (DecentHologramsApiProvider.implementation != null) {
            throw new IllegalStateException("DecentHolograms API is already initialized.");
        }
        DecentHologramsApiProvider.implementation = implementation;
    }

    abstract DecentHologramsApi getApi(@NotNull Plugin plugin);

}