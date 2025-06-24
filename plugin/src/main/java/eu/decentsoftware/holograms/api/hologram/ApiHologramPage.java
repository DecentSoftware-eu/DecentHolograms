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

package eu.decentsoftware.holograms.api.hologram;

import com.google.common.collect.ImmutableList;
import eu.decentsoftware.holograms.api.hologram.line.ApiHologramLine;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.List;

public class ApiHologramPage implements HologramPage {

    private final ApiHologram hologram;
    private final List<ApiHologramLine> lines = new ArrayList<>();

    public ApiHologramPage(ApiHologram hologram) {
        this.hologram = hologram;
    }

    @Nullable
    @Override
    public ApiHologramLine getLine(int index) {
        hologram.checkDestroyed();
        return lines.get(index);
    }

    @Override
    public void removeLine(int index) {
        hologram.checkDestroyed();
        lines.remove(index);
    }

    @Override
    public void appendLine(@NotNull HologramLine line) {
        hologram.checkDestroyed();
        lines.add((ApiHologramLine) line);
    }

    @Override
    public void insertLine(int index, @NotNull HologramLine line) {
        hologram.checkDestroyed();
        lines.add(index, (ApiHologramLine) line);
    }

    @NotNull
    @Unmodifiable
    @Override
    public List<HologramLine> getLines() {
        hologram.checkDestroyed();
        return ImmutableList.copyOf(lines);
    }

    @Override
    public void clearLines() {
        hologram.checkDestroyed();
        lines.clear();
    }

    @Override
    public double getHeight() {
        hologram.checkDestroyed();
        return lines.stream()
                .map(HologramLine::getHeight)
                .reduce(Double::sum)
                .orElse(0.0);
    }

    @Override
    public int size() {
        hologram.checkDestroyed();
        return lines.size();
    }
}
