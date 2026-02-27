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

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextDisplay extends DisplayBase {

    private final List<String> lines;

    public TextDisplay(String name, DecentLocation location, DisplaySettings settings) {
        super(name, location, settings);
        this.lines = new ArrayList<>();
    }

    @Override
    public DisplayType getType() {
        return DisplayType.TEXT;
    }

    public void addLine(String line) {
        this.lines.add(line);
    }

    public void addLine(int index, String line) {
        this.lines.add(index, line);
    }

    public void setLine(int index, String line) {
        this.lines.set(index, line);
    }

    public void removeLine(int index) {
        this.lines.remove(index);
    }

    public void setLines(List<String> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    public void swapLines(int index1, int index2) {
        Collections.swap(this.lines, index1, index2);
    }

    public List<String> getLines() {
        return lines;
    }
}
