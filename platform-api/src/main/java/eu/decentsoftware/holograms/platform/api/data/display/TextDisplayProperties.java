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

import java.util.Objects;

public class TextDisplayProperties {

    private boolean hasShadow;
    private boolean seeThrough;
    private TextDisplayAlignment alignment;

    public boolean hasShadow() {
        return hasShadow;
    }

    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
    }

    public boolean isSeeThrough() {
        return seeThrough;
    }

    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }

    public TextDisplayAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextDisplayAlignment alignment) {
        this.alignment = alignment;
    }

    @Override
    public String toString() {
        return "TextDisplayProperties{" +
                "hasShadow=" + hasShadow +
                ", seeThrough=" + seeThrough +
                ", alignment=" + alignment +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TextDisplayProperties)) {
            return false;
        }
        TextDisplayProperties that = (TextDisplayProperties) o;
        return hasShadow() == that.hasShadow()
                && isSeeThrough() == that.isSeeThrough()
                && getAlignment() == that.getAlignment();
    }

    @Override
    public int hashCode() {
        return Objects.hash(hasShadow(), isSeeThrough(), getAlignment());
    }
}
