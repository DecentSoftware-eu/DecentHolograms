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

package eu.decentsoftware.holograms.display.render.content;

import java.util.List;

public final class CompiledTextDisplayContent implements CompiledDisplayContent<List<CompiledTextDisplayLine>> {

    private final List<String> resolvedContent;
    private final List<CompiledTextDisplayLine> content;
    private final boolean animated;
    private final boolean hasPlaceholders;
    private boolean dirty;

    public CompiledTextDisplayContent(List<String> resolvedContent,
                                      List<CompiledTextDisplayLine> content,
                                      boolean animated,
                                      boolean hasPlaceholders) {
        this.resolvedContent = resolvedContent;
        this.content = content;
        this.animated = animated;
        this.hasPlaceholders = hasPlaceholders;
        this.dirty = true;
    }

    @Override
    public List<CompiledTextDisplayLine> getContent() {
        if (dirty) {
            dirty = false;
        }
        return content;
    }

    @Override
    public boolean isDirty() {
        return animated || dirty;
    }

    @Override
    public boolean isDynamic() {
        return hasPlaceholders;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof CompiledTextDisplayContent)) {
            return false;
        }
        return resolvedContent.equals(((CompiledTextDisplayContent) other).resolvedContent);
    }

    @Override
    public int hashCode() {
        return resolvedContent.hashCode();
    }
}
