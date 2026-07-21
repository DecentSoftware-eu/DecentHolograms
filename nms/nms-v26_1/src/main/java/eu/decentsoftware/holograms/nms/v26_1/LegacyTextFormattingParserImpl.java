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

package eu.decentsoftware.holograms.nms.v26_1;

import eu.decentsoftware.holograms.nms.api.text.ComponentFormat;
import eu.decentsoftware.holograms.nms.api.text.LegacyTextFormattingParser;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;

import java.util.ArrayList;
import java.util.List;

final class LegacyTextFormattingParserImpl extends LegacyTextFormattingParser<MutableComponent, ChatFormatting> {

    private static final ChatFormatting[] FORMATS_BY_CHAR = new ChatFormatting[128];
    private static final ChatFormatting[][] FORMATS_BY_FLAGS = new ChatFormatting[32][];
    private final Int2ObjectMap<Style> modifierCache = new Int2ObjectOpenHashMap<>(1024);

    static {
        String formattingChars = "0123456789ABCDEFKLMNORabcdefklmnor";
        for (char c : formattingChars.toCharArray()) {
            FORMATS_BY_CHAR[c] = ChatFormatting.getByCode(c);
        }

        // Just cache all combinations of special format flags for O(1) lookup
        for (int flags = 0; flags < 32; flags++) {
            List<ChatFormatting> list = new ArrayList<>(5);
            if ((flags & ComponentFormat.OBFUSCATED_MASK) != 0) {
                list.add(ChatFormatting.OBFUSCATED);
            }
            if ((flags & ComponentFormat.BOLD_MASK) != 0) {
                list.add(ChatFormatting.BOLD);
            }
            if ((flags & ComponentFormat.STRIKETHROUGH_MASK) != 0) {
                list.add(ChatFormatting.STRIKETHROUGH);
            }
            if ((flags & ComponentFormat.UNDERLINED_MASK) != 0) {
                list.add(ChatFormatting.UNDERLINE);
            }
            if ((flags & ComponentFormat.ITALIC_MASK) != 0) {
                list.add(ChatFormatting.ITALIC);
            }
            FORMATS_BY_FLAGS[flags] = list.toArray(new ChatFormatting[0]);
        }
    }

    @Override
    protected MutableComponent createEmptyComponent() {
        ComponentContents contents = PlainTextContents.EMPTY;
        return MutableComponent.create(contents);
    }

    @Override
    protected MutableComponent createTextComponent(String text) {
        ComponentContents contents = new PlainTextContents.LiteralContents(text);
        return MutableComponent.create(contents);
    }

    @Override
    protected MutableComponent createFormattedComponent(String text, ComponentFormat format) {
        ComponentContents contents = new PlainTextContents.LiteralContents(text);
        MutableComponent component = MutableComponent.create(contents);

        Style style = getStyle(format);
        component.setStyle(style);

        return component;
    }

    private Style getStyle(ComponentFormat format) {
        return modifierCache.computeIfAbsent(format.getKey(), _ -> createStyle(format));
    }

    private Style createStyle(ComponentFormat format) {
        ChatFormatting[] formats = FORMATS_BY_FLAGS[format.getFlags()];
        Style style = Style.EMPTY.applyFormats(formats);
        if (format.getColor() != -1) {
            style = style.withColor(format.getColor());
        }
        return style;
    }

    @Override
    protected void addSibling(MutableComponent parent, MutableComponent child) {
        parent.append(child);
    }

    @Override
    protected ChatFormatting parseFormat(char c) {
        if (c >= FORMATS_BY_CHAR.length) {
            return null;
        }
        return FORMATS_BY_CHAR[c];
    }

    @Override
    protected void applyFormat(ComponentFormat currentFormat, ChatFormatting format) {
        if (format.getColor() != null) {
            currentFormat.setColor(format.getColor());
            return;
        }
        if (format == ChatFormatting.RESET) {
            currentFormat.reset();
            return;
        }
        switch (format) {
            case OBFUSCATED -> currentFormat.setObfuscated();
            case BOLD -> currentFormat.setBold();
            case STRIKETHROUGH -> currentFormat.setStrikethrough();
            case UNDERLINE -> currentFormat.setUnderlined();
            case ITALIC -> currentFormat.setItalic();
            default -> {
                // other cases handled above
            }
        }
    }
}
