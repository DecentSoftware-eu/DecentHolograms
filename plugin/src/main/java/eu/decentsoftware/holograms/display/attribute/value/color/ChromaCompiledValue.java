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

package eu.decentsoftware.holograms.display.attribute.value.color;

import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;

public final class ChromaCompiledValue implements CompiledAttributeValue<DecentColor> {

    public static final int DEFAULT_PERIOD = 20;
    public static final int DEFAULT_SATURATION = 100;
    public static final int DEFAULT_VALUE = 100;

    private final int period;
    private final int alpha;
    private final int saturation;
    private final int value;

    public ChromaCompiledValue(int period, int alpha, int saturation, int value) {
        this.period = period;
        this.alpha = alpha;
        this.saturation = saturation;
        this.value = value;
    }

    @Override
    public DecentColor evaluate() {
        long elapsedTime = System.currentTimeMillis();
        long hue = ((elapsedTime / period) % 360L);

        return DecentColor.fromHsva(hue, saturation, value, alpha);
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}
