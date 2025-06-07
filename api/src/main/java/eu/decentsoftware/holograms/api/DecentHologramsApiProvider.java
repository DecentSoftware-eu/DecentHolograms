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

package eu.decentsoftware.holograms.api;

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

    private static DecentHologramsApiProvider implementation;

    @Contract(pure = true)
    @ApiStatus.Internal
    static DecentHologramsApiProvider getImplementation() {
        if (DecentHologramsApiProvider.implementation == null) {
            throw new IllegalStateException("DecentHologramsApiProvider implementation is not set.");
        }
        return DecentHologramsApiProvider.implementation;
    }

    @ApiStatus.Internal
    static void setImplementation(@NotNull DecentHologramsApiProvider implementation) {
        if (DecentHologramsApiProvider.implementation != null) {
            throw new IllegalStateException("DecentHologramsApiProvider implementation is already set.");
        }
        DecentHologramsApiProvider.implementation = implementation;
    }

    abstract DecentHologramsApi getApi(@NotNull Plugin plugin);

}