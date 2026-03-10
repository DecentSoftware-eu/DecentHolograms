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

public abstract class TextFormattingParser<C, F> {

    public abstract C parse(String text);

    public abstract C parseNullable(String text);

    protected abstract C createEmptyComponent();

    protected abstract C createTextComponent(String text);

    protected abstract C createFormattedComponent(String text, ComponentFormat format);

    protected abstract void addSibling(C parent, C child);

    protected abstract F parseFormat(char c);

    protected abstract void applyFormat(ComponentFormat currentFormat, F format);

}
