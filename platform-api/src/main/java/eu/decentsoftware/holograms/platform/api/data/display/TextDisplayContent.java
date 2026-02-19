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

package eu.decentsoftware.holograms.platform.api.data.display;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class TextDisplayContent implements DisplayContent<List<TextDisplayLine>> {

    private final List<TextDisplayLine> text;
    private final boolean animated;

    public TextDisplayContent(List<TextDisplayLine> text, boolean animated) {
        this.text = text;
        this.animated = animated;
    }

    @Override
    public List<TextDisplayLine> getContent() {
        return text;
    }

    @Override
    public boolean isAnimated() {
        return animated;
    }

    @Override
    public DisplayContent<List<TextDisplayLine>> copy() {
        List<TextDisplayLine> copiedLines = new ArrayList<>(text.size());
        for (TextDisplayLine textDisplayLine : text) {
            copiedLines.add(textDisplayLine.copy());
        }
        return new TextDisplayContent(copiedLines, animated);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TextDisplayContent)) {
            return false;
        }
        TextDisplayContent that = (TextDisplayContent) o;
        return Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }
}
