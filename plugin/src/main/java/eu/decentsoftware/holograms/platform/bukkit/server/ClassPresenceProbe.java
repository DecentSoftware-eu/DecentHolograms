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

package eu.decentsoftware.holograms.platform.bukkit.server;

import eu.decentsoftware.holograms.platform.api.server.ServerPlatformProbe;
import eu.decentsoftware.holograms.platform.api.server.ServerPlatformType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Detects a platform by the presence of a marker class.
 *
 * <p>Several markers may be given, and any one of them matching is enough. Platforms move their
 * internals between versions, so a single class name is not always reliable across the whole range
 * of versions the plugin supports.</p>
 *
 * @author d0by
 * @since 2.10.2
 */
public class ClassPresenceProbe implements ServerPlatformProbe {

    private final ServerPlatformType type;
    private final List<String> markerClasses;

    /**
     * Creates a new probe that detects the given platform by the presence of the given marker classes.
     *
     * @param type          The platform this probe detects.
     * @param markerClasses Class names, any one of which identifies the platform.
     * @throws NullPointerException     If any argument is null.
     * @throws IllegalArgumentException If no marker class is given.
     */
    public ClassPresenceProbe(@NotNull ServerPlatformType type, @NotNull String... markerClasses) {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(markerClasses, "markerClasses cannot be null");
        if (markerClasses.length == 0) {
            throw new IllegalArgumentException("At least one marker class is required");
        }
        this.type = type;
        this.markerClasses = Collections.unmodifiableList(Arrays.asList(markerClasses));
    }

    @NotNull
    @Override
    public ServerPlatformType getType() {
        return type;
    }

    @Override
    public boolean matches() {
        for (String markerClass : markerClasses) {
            if (isPresent(markerClass)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isPresent(String className) {
        try {
            // Resolve without initializing: these are server internals and running their static
            // initializers during detection would be a side effect we do not want.
            Class.forName(className, false, ClassPresenceProbe.class.getClassLoader());
            return true;
        } catch (ClassNotFoundException | LinkageError e) {
            return false;
        }
    }
}
