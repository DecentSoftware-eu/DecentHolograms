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

package eu.decentsoftware.holograms.nms.api.text;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentFormat<F, L> {

    private L color;
    private final List<F> formats = new ArrayList<>();

    protected abstract boolean isColor(F format);

    protected abstract boolean isResetFormat(F format);

    public void reset() {
        this.color = null;
        this.formats.clear();
    }

    public void setColor(L color) {
        this.color = color;
        // Color resets special formatting
        this.formats.clear();
    }

    public L getColor() {
        return color;
    }

    public void addFormat(F format) {
        if (isResetFormat(format)) {
            reset();
            return;
        }
        if (isColor(format)) {
            // Store simple colors as EnumChatFormat to be able to apply it with special formatting at once
            // ChatHexColor has to be applied separately from special formatting,
            // and it causes a new object to be allocated
            setColor(null);
        }
        formats.add(format);
    }

    public List<F> getFormats() {
        return formats;
    }
}
