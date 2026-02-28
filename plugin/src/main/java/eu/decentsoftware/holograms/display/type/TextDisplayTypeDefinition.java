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

package eu.decentsoftware.holograms.display.type;

import eu.decentsoftware.holograms.api.animations.compile.AnimationCompiler;
import eu.decentsoftware.holograms.api.animations.compile.CompiledAnimationsOutput;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.TextDisplay;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.display.render.content.CompiledDisplayContent;
import eu.decentsoftware.holograms.display.render.content.CompiledTextDisplayContent;
import eu.decentsoftware.holograms.display.render.content.TextDisplayLine;
import eu.decentsoftware.holograms.display.render.placeholder.DisplayPlaceholderService;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;

import java.util.ArrayList;
import java.util.List;

public class TextDisplayTypeDefinition implements DisplayTypeDefinition<List<TextDisplayLine>> {

    private final DisplayPlaceholderService displayPlaceholderService;
    private final AnimationCompiler animationCompiler;

    public TextDisplayTypeDefinition(DisplayPlaceholderService displayPlaceholderService, AnimationCompiler animationCompiler) {
        this.displayPlaceholderService = displayPlaceholderService;
        this.animationCompiler = animationCompiler;
    }

    @Override
    public DisplayType getType() {
        return DisplayType.TEXT;
    }

    @Override
    public CompiledDisplayContent<List<TextDisplayLine>> resolveContent(DisplayBase display, DisplayRenderContext context) {
        TextDisplay textDisplay = getTextDisplay(display);

        List<TextDisplayLine> resolvedLines = new ArrayList<>();
        boolean anyLineAnimated = false;
        boolean anyLineHasPlaceholders = false;
        for (String line : textDisplay.getLines()) {
            String resolvedLine;
            if (displayPlaceholderService.containsPlaceholders(line)) {
                resolvedLine = displayPlaceholderService.replacePlaceholders(line, context);
                anyLineHasPlaceholders = true;
            } else {
                resolvedLine = line;
            }

            CompiledAnimationsOutput compiledAnimationsOutput = animationCompiler.compileAnimations(resolvedLine);
            resolvedLine = compiledAnimationsOutput.getStrippedString();

            TextDisplayLine displayLine = new TextDisplayLine(resolvedLine, compiledAnimationsOutput.getAnimations());

            anyLineAnimated |= displayLine.isAnimated();

            resolvedLines.add(displayLine);
        }
        return new CompiledTextDisplayContent(resolvedLines, anyLineAnimated, anyLineHasPlaceholders);
    }

    private TextDisplay getTextDisplay(DisplayBase displayBase) {
        if (!(displayBase instanceof TextDisplay)) {
            throw new IllegalArgumentException("Display is not a text display");
        }
        return (TextDisplay) displayBase;
    }
}
