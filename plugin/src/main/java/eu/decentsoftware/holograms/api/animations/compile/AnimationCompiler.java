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

package eu.decentsoftware.holograms.api.animations.compile;

import eu.decentsoftware.holograms.api.animations.AnimationManager;
import eu.decentsoftware.holograms.api.animations.TextAnimation;
import eu.decentsoftware.holograms.platform.api.data.display.CompiledAnimation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

public class AnimationCompiler {

    private final AnimationManager animationManager;

    public AnimationCompiler(AnimationManager animationManager) {
        this.animationManager = animationManager;
    }

    public CompiledAnimationsOutput compileAnimations(String input) {
        input = input.replace("&u", "<#ANIM:colors></#ANIM>");

        StringBuilder cleanedString = new StringBuilder(input);
        List<CompiledAnimation> compiledAnimations = new ArrayList<>(compileStandardAnimations(input, cleanedString));
        compiledAnimations.sort(Comparator.comparing(CompiledAnimation::getPosition).reversed());
        return new CompiledAnimationsOutput(cleanedString.toString(), compiledAnimations);
    }

    private List<CompiledAnimation> compileStandardAnimations(String string, StringBuilder cleanedString) {
        if (string.indexOf('<') == -1 && string.indexOf('{') == -1) {
            return Collections.emptyList();
        }

        List<CompiledAnimation> animations = new ArrayList<>();
        Matcher matcher = AnimationManager.ANIMATION_PATTERN.matcher(string);
        int cleanedOffset = 0;
        while (matcher.find()) {
            String animationName = matcher.group(1);
            TextAnimation animation = animationManager.getAnimation(animationName);
            if (animation == null) {
                continue;
            }
            String argsGroup = matcher.group(2);
            String body = matcher.group(3);

            int originalStart = matcher.start();
            int originalEnd = matcher.end();
            int tagLength = originalEnd - originalStart;

            int cleanedPosition = originalStart - cleanedOffset;
            cleanedString.delete(cleanedPosition, cleanedPosition + tagLength);

            animations.add(new CompiledAnimation(animation.getName(), parseArgs(argsGroup), body, cleanedPosition));

            cleanedOffset += tagLength;
        }

        return animations;
    }

    private String[] parseArgs(String argsGroup) {
        if (argsGroup == null) {
            return null;
        }
        String args = argsGroup.substring(1);
        return args.split(",");
    }
}
