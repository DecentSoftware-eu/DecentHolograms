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

package eu.decentsoftware.holograms.display.render.postprocessing.processor;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.TextDisplayContent;

public class TextDisplayAnimationPostProcessor implements DisplayContentPostProcessor<String, DisplayContent<String>> {

    private final AnimationManager animationManager;

    public TextDisplayAnimationPostProcessor(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    @Override
    public DisplayContent<String> process(DisplayContent<String> content) {
        String animatedText = animationManager.parseTextAnimations(content.getContent());
        return new TextDisplayContent(animatedText);
    }
}
