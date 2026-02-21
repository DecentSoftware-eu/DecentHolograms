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

package eu.decentsoftware.holograms.platform.bukkit;

import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.platform.api.capability.MinecraftFeature;
import eu.decentsoftware.holograms.platform.api.capability.PlatformCapabilities;
import eu.decentsoftware.holograms.platform.api.capability.PlatformCapability;
import eu.decentsoftware.holograms.platform.api.text.TextFormat;
import org.jetbrains.annotations.NotNull;

public class BukkitPlatformCapabilities implements PlatformCapabilities {

    @Override
    public boolean supports(@NotNull PlatformCapability capability) {
        if (capability instanceof MinecraftFeature) {
            return supportsMinecraftFeature((MinecraftFeature) capability);
        } else if (capability instanceof TextFormat) {
            return capability == TextFormat.LEGACY;
        }
        return false;
    }

    private boolean supportsMinecraftFeature(MinecraftFeature minecraftFeature) {
        switch (minecraftFeature) {
            case DISPLAY_ENTITIES:
                return Version.afterOrEqual(Version.v1_19_R3);
        }
        return false;
    }
}
