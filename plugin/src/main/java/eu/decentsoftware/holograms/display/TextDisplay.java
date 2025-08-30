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

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.display.attributes.DisplayAttribute;
import eu.decentsoftware.holograms.display.attributes.FixedDisplayAttribute;
import eu.decentsoftware.holograms.nms.api.display.data.DisplayColor;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayAlignment;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayData;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsTextDisplayRenderer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TextDisplay extends DisplayBase<TextDisplayData> {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private final List<String> lines;
    private final NmsTextDisplayRenderer renderer;
    private DisplayAttribute<Integer> lineWidthAttribute;
    private DisplayAttribute<DisplayColor> backgroundColorAttribute;
    private DisplayAttribute<Byte> textOpacityAttribute;
    private DisplayAttribute<Boolean> textShadowAttribute;
    private DisplayAttribute<Boolean> seeThroughAttribute;
    private DisplayAttribute<TextDisplayAlignment> alignmentAttribute;

    public TextDisplay(String name, DecentLocation location) {
        super(name, location);
        this.lines = new ArrayList<>();
        this.renderer = DECENT_HOLOGRAMS.getNmsAdapter().getDisplayRendererFactory().createTextDisplayRenderer();
    }

    public List<String> getText(Player player) {
        return lines.stream()
                .map(line -> Common.colorize(PAPI.setPlaceholders(player, line)))
                .collect(Collectors.toList());
    }

    @Override
    public NmsDisplayRenderer<TextDisplayData> getDisplayRenderer() {
        return renderer;
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

    public void clearLines() {
        this.lines.clear();
    }

    public void setLines(List<String> lines) {
        this.lines.clear();
        this.lines.addAll(lines);
    }

    public List<String> getLines() {
        return lines;
    }

    public int getLineWidth() {
        return lineWidthAttribute.getValue();
    }

    public void setLineWidth(int lineWidth) {
        this.lineWidthAttribute = new FixedDisplayAttribute<>(lineWidth);
    }

    public DisplayColor getBackgroundColor() {
        return backgroundColorAttribute.getValue();
    }

    public void setBackgroundColor(DisplayColor backgroundColor) {
        this.backgroundColorAttribute = new FixedDisplayAttribute<>(backgroundColor);
    }

    public byte getTextOpacity() {
        return textOpacityAttribute.getValue();
    }

    public void setTextOpacity(byte textOpacity) {
        this.textOpacityAttribute = new FixedDisplayAttribute<>(textOpacity);
    }

    public boolean isTextShadow() {
        return textShadowAttribute.getValue();
    }

    public void setTextShadow(boolean textShadow) {
        this.textShadowAttribute = new FixedDisplayAttribute<>(textShadow);
    }

    public boolean isSeeThrough() {
        return seeThroughAttribute.getValue();
    }

    public void setSeeThrough(boolean seeThrough) {
        this.seeThroughAttribute = new FixedDisplayAttribute<>(seeThrough);
    }

    public TextDisplayAlignment getAlignment() {
        return alignmentAttribute.getValue();
    }

    public void setAlignment(TextDisplayAlignment alignment) {
        this.alignmentAttribute = new FixedDisplayAttribute<>(alignment);
    }

    public DisplayAttribute<Integer> getLineWidthAttribute() {
        return lineWidthAttribute;
    }

    public void setLineWidthAttribute(DisplayAttribute<Integer> lineWidthAttribute) {
        this.lineWidthAttribute = lineWidthAttribute;
    }

    public DisplayAttribute<DisplayColor> getBackgroundColorAttribute() {
        return backgroundColorAttribute;
    }

    public void setBackgroundColorAttribute(DisplayAttribute<DisplayColor> backgroundColorAttribute) {
        this.backgroundColorAttribute = backgroundColorAttribute;
    }

    public DisplayAttribute<Byte> getTextOpacityAttribute() {
        return textOpacityAttribute;
    }

    public void setTextOpacityAttribute(DisplayAttribute<Byte> textOpacityAttribute) {
        this.textOpacityAttribute = textOpacityAttribute;
    }

    public DisplayAttribute<Boolean> getTextShadowAttribute() {
        return textShadowAttribute;
    }

    public void setTextShadowAttribute(DisplayAttribute<Boolean> textShadowAttribute) {
        this.textShadowAttribute = textShadowAttribute;
    }

    public DisplayAttribute<Boolean> getSeeThroughAttribute() {
        return seeThroughAttribute;
    }

    public void setSeeThroughAttribute(DisplayAttribute<Boolean> seeThroughAttribute) {
        this.seeThroughAttribute = seeThroughAttribute;
    }

    public DisplayAttribute<TextDisplayAlignment> getAlignmentAttribute() {
        return alignmentAttribute;
    }

    public void setAlignmentAttribute(DisplayAttribute<TextDisplayAlignment> alignmentAttribute) {
        this.alignmentAttribute = alignmentAttribute;
    }
}
