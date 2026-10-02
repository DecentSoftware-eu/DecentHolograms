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

package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.convertor.IConvertor;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.plugin.convertors.impl.CMIConverter;
import eu.decentsoftware.holograms.plugin.convertors.impl.FutureHologramsConverter;
import eu.decentsoftware.holograms.plugin.convertors.impl.GHoloConverter;
import eu.decentsoftware.holograms.plugin.convertors.impl.HologramsConvertor;
import eu.decentsoftware.holograms.plugin.convertors.impl.HolographicDisplaysConvertor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Factory class for creating instances of {@link IConvertor} based on the specified {@link ConvertorType}.
 *
 * @author d0by
 * @see ConvertorType
 * @since 2.10.2
 */
public class ConvertorFactory {

    private final JavaPlugin plugin;
    private final HologramManager hologramManager;

    public ConvertorFactory(JavaPlugin plugin, HologramManager hologramManager) {
        this.plugin = plugin;
        this.hologramManager = hologramManager;
    }

    @Nullable
    public IConvertor getConvertor(ConvertorType type) {
        Objects.requireNonNull(type, "type cannot be null");
        switch (type) {
            case CMI:
                return new CMIConverter(plugin, hologramManager);
            case FUTURE_HOLOGRAMS:
                return new FutureHologramsConverter(plugin, hologramManager);
            case GHOLO:
                return new GHoloConverter(plugin, hologramManager);
            case HOLOGRAPHIC_DISPLAYS:
                return new HolographicDisplaysConvertor(plugin, hologramManager);
            case HOLOGRAMS:
                return new HologramsConvertor(plugin, hologramManager);
            default:
                return null;
        }
    }
}
