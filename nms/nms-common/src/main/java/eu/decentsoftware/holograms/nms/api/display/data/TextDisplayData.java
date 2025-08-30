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
    private NmsDisplayAttribute<Integer> lineWidthAttribute;
    private NmsDisplayAttribute<DisplayColor> backgroundColorAttribute;
    private NmsDisplayAttribute<Byte> textOpacityAttribute;
    private NmsDisplayAttribute<Boolean> textShadowAttribute;
    private NmsDisplayAttribute<Boolean> seeThroughAttribute;
    private NmsDisplayAttribute<TextDisplayAlignment> alignmentAttribute;

    public List<String> getText() {
        return text;
    }

    public void setText(List<String> text) {
        this.text = text;
    }

    public NmsDisplayAttribute<Integer> getLineWidthAttribute() {
        return lineWidthAttribute;
    }

    public void setLineWidthAttribute(NmsDisplayAttribute<Integer> lineWidthAttribute) {
        this.lineWidthAttribute = lineWidthAttribute;
    }

    public NmsDisplayAttribute<DisplayColor> getBackgroundColorAttribute() {
        return backgroundColorAttribute;
    }

    public void setBackgroundColorAttribute(NmsDisplayAttribute<DisplayColor> backgroundColorAttribute) {
        this.backgroundColorAttribute = backgroundColorAttribute;
    }

    public NmsDisplayAttribute<Byte> getTextOpacityAttribute() {
        return textOpacityAttribute;
    }

    public void setTextOpacityAttribute(NmsDisplayAttribute<Byte> textOpacityAttribute) {
        this.textOpacityAttribute = textOpacityAttribute;
    }

    public NmsDisplayAttribute<Boolean> getTextShadowAttribute() {
        return textShadowAttribute;
    }

    public void setTextShadowAttribute(NmsDisplayAttribute<Boolean> textShadowAttribute) {
        this.textShadowAttribute = textShadowAttribute;
    }

    public NmsDisplayAttribute<Boolean> getSeeThroughAttribute() {
        return seeThroughAttribute;
    }

    public void setSeeThroughAttribute(NmsDisplayAttribute<Boolean> seeThroughAttribute) {
        this.seeThroughAttribute = seeThroughAttribute;
    }

    public NmsDisplayAttribute<TextDisplayAlignment> getAlignmentAttribute() {
        return alignmentAttribute;
    }

    public void setAlignmentAttribute(NmsDisplayAttribute<TextDisplayAlignment> alignmentAttribute) {
        this.alignmentAttribute = alignmentAttribute;
    }
}
