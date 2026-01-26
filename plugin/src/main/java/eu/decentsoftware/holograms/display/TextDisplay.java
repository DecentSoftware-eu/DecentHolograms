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

import eu.decentsoftware.holograms.location.DecentLocation;

import java.util.ArrayList;
import java.util.List;

public class TextDisplay extends DisplayBase {

    private final List<TextDisplayPage> pages;

    public TextDisplay(String name, DecentLocation location, DisplaySettings settings) {
        super(name, location, settings);
        this.pages = new ArrayList<>();
    }

    @Override
    public DisplayType getType() {
        return DisplayType.TEXT;
    }

    public TextDisplayPage getPage(int index) {
        return this.pages.get(index);
    }

    public void addPage(TextDisplayPage page) {
        this.pages.add(page);
    }

    public void addPage(int index, TextDisplayPage page) {
        this.pages.add(index, page);
    }

    public void removePage(int index) {
        this.pages.remove(index);
    }

    public void setPages(List<TextDisplayPage> pages) {
        this.pages.clear();
        this.pages.addAll(pages);
    }

    public List<TextDisplayPage> getPages() {
        return pages;
    }
}
