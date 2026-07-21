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

import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.CompiledAttributeValue;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.data.DecentColor;

public final class ChromaValue implements AttributeValue<DecentColor> {

    private final int period;
    private final Integer alpha;
    private final Integer saturation;
    private final Integer value;

    public ChromaValue() {
        this(ChromaCompiledValue.DEFAULT_PERIOD, null, null, null);
    }

    public ChromaValue(Integer period) {
        this(period, null, null, null);
    }

    public ChromaValue(Integer period, Integer alpha) {
        this(period, alpha, null, null);
    }

    public ChromaValue(Integer period, Integer alpha, Integer saturation) {
        this(period, alpha, saturation, null);
    }

    public ChromaValue(Integer period, Integer alpha, Integer saturation, Integer value) {
        this.period = period;
        this.alpha = alpha;
        this.saturation = saturation;
        this.value = value;
    }

    @Override
    public CompiledAttributeValue<DecentColor> compile(DisplayRenderContext context) {
        int compiledAlpha = this.alpha == null ? 255 : this.alpha;
        int compiledSaturation = this.saturation == null ? ChromaCompiledValue.DEFAULT_SATURATION : this.saturation;
        int compiledValue = this.value == null ? ChromaCompiledValue.DEFAULT_VALUE : this.value;
        return new ChromaCompiledValue(this.period, compiledAlpha, compiledSaturation, compiledValue);
    }

    @Override
    public String getTypeKey() {
        return ChromaValueType.TYPE_ID;
    }

    @Override
    public String toHumanReadableString() {
        StringBuilder builder = new StringBuilder("CHROMA ").append(period);
        if (alpha != null) {
            builder.append(" ").append(alpha);
        }
        if (saturation != null) {
            builder.append(" ").append(saturation);
        }
        if (value != null) {
            builder.append(" ").append(value);
        }
        return builder.toString();
    }

    public Integer getPeriod() {
        return period;
    }

    public Integer getAlpha() {
        return alpha;
    }

    public Integer getSaturation() {
        return saturation;
    }

    public Integer getValue() {
        return value;
    }
}
