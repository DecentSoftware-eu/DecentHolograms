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

package eu.decentsoftware.holograms.nms.api.display.data;

import java.util.List;

public class TextDisplayData extends DisplayData {

    private List<String> text;
    private int lineWidth = 200;
    private DisplayColor backgroundColor;
    private byte textOpacity = -1;
    private boolean textShadow = false;
    private boolean seeThrough = false;
    private TextDisplayAlignment textAlignment = TextDisplayAlignment.CENTER;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidth = lineWidth;
    }

    public DisplayColor getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(DisplayColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public byte getTextOpacity() {
        return textOpacity;
    }

    public void setTextOpacity(byte textOpacity) {
        this.textOpacity = textOpacity;
    }

    public boolean isTextShadow() {
        return textShadow;
    }

    public void setTextShadow(boolean textShadow) {
        this.textShadow = textShadow;
    }

    public boolean isSeeThrough() {
        return seeThrough;
    }

    public void setSeeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
    }

    public TextDisplayAlignment getTextAlignment() {
        return textAlignment;
    }

    public void setTextAlignment(TextDisplayAlignment textAlignment) {
        this.textAlignment = textAlignment;
    }
}
