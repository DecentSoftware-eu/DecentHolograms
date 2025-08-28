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
    private int lineWidth = 200;
    private DisplayColor backgroundColor;
    private byte textOpacity = -1;
    private boolean textShadow = false;
    private boolean seeThrough = false;
    private TextDisplayAlignment alignment = TextDisplayAlignment.CENTER;

    public TextDisplay(String name, DecentLocation location) {
        super(name, location);
        this.lines = new ArrayList<>();
        this.renderer = DECENT_HOLOGRAMS.getNmsAdapter().getDisplayRendererFactory().createTextDisplayRenderer();
    }

    @Override
    public TextDisplayData createDisplayData(Player player) {
        TextDisplayData data = new TextDisplayData();
        data.setText(getText(player));
        if (lineWidth <= 0) {
            data.setLineWidth(lineWidth);
        }
        if (backgroundColor != null) {
            data.setBackgroundColor(backgroundColor);
        }
        data.setTextOpacity(textOpacity);
        data.setTextShadow(textShadow);
        data.setSeeThrough(seeThrough);
        if (alignment != null) {
            data.setTextAlignment(alignment);
        }

        if (translation != null) {
            data.setTranslation(translation);
        }
        if (scale != null) {
            data.setScale(scale);
        }
        if (billboardConstraints != null) {
            data.setBillboardConstraints(billboardConstraints);
        }
        if (brightnessOverride != null) {
            data.setBrightnessOverride(brightnessOverride);
        }
        data.setViewRange(viewRange);
        data.setShadowRadius(shadowRadius);
        data.setShadowStrength(shadowStrength);
        return data;
    }

    private List<String> getText(Player player) {
        return lines.stream()
                .map(line -> Common.colorize(PAPI.setPlaceholders(player, line)))
                .collect(Collectors.toList());
    }

    @Override
    public NmsDisplayRenderer<TextDisplayData> getDisplayRenderer() {
        return renderer;
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

    public TextDisplayAlignment getAlignment() {
        return alignment;
    }

    public void setAlignment(TextDisplayAlignment alignment) {
        this.alignment = alignment;
    }
}
