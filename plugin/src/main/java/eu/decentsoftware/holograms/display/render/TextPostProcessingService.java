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

package eu.decentsoftware.holograms.display.render;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.display.render.content.CompiledTextDisplayContent;
import eu.decentsoftware.holograms.display.render.content.TextDisplayLine;
import eu.decentsoftware.holograms.platform.api.text.TextFormatter;
import eu.decentsoftware.holograms.profiler.DecentProfiler;
import eu.decentsoftware.holograms.profiler.Metrics;
import eu.decentsoftware.holograms.profiler.TimerHandle;

import java.util.ArrayList;
import java.util.List;

public class TextPostProcessingService {

    private final AnimationManager animationManager;
    private final TextFormatter textFormatter;

    public TextPostProcessingService(AnimationManager animationManager, TextFormatter textFormatter) {
        this.animationManager = animationManager;
        this.textFormatter = textFormatter;
    }

    public List<String> postProcess(CompiledTextDisplayContent content) {
        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.POST_PROCESS_TEXT)) {
            List<TextDisplayLine> lines = content.getContent();
            List<String> result = new ArrayList<>(lines.size());
            for (TextDisplayLine line : lines) {
                result.add(postProcessLine(line));
            }
            return result;
        }
    }

    private String postProcessLine(TextDisplayLine line) {
        String animatedLine = animateLine(line);
        return textFormatter.format(animatedLine);
    }

    private String animateLine(TextDisplayLine line) {
        if (!line.isAnimated()) {
            return line.getText();
        }

        try (TimerHandle ignored = DecentProfiler.getInstance().startTimer(Metrics.POST_PROCESS_TEXT_ANIMATIONS_LINE)) {
            return animationManager.applyAnimations(line.getText(), line.getAnimations());
        }
    }
}
