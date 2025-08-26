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

import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import lombok.experimental.UtilityClass;
import org.bukkit.Color;

@UtilityClass
public final class DisplayColorUtil {

    private static final ReflectMethod FROM_ARGB_METHOD = new ReflectMethod(Color.class, "fromARGB", int.class);
    private static final ReflectMethod TO_ARGB_METHOD = new ReflectMethod(Color.class, "asARGB");

    public static Color getColorFromARGB(int argb) {
        return FROM_ARGB_METHOD.invoke(null, argb);
    }

    public static int getARGBFromColor(Color color) {
        return TO_ARGB_METHOD.invoke(color);
    }

}
