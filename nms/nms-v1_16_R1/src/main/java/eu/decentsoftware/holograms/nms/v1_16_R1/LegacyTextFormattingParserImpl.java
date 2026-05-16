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

package eu.decentsoftware.holograms.nms.v1_16_R1;

import eu.decentsoftware.holograms.nms.api.text.ComponentFormat;
import eu.decentsoftware.holograms.nms.api.text.LegacyTextFormattingParser;
import net.minecraft.server.v1_16_R1.ChatComponentText;
import net.minecraft.server.v1_16_R1.ChatHexColor;
import net.minecraft.server.v1_16_R1.ChatModifier;
import net.minecraft.server.v1_16_R1.EnumChatFormat;
import net.minecraft.server.v1_16_R1.IChatMutableComponent;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.ArrayList;
import java.util.List;

final class LegacyTextFormattingParserImpl extends LegacyTextFormattingParser<IChatMutableComponent, EnumChatFormat> {

    private static final EnumChatFormat[] FORMATS_BY_CHAR = new EnumChatFormat[128];
    private static final EnumChatFormat[][] FORMATS_BY_FLAGS = new EnumChatFormat[32][];
    private final Int2ObjectMap<ChatModifier> modifierCache = new Int2ObjectOpenHashMap<>(1024);

    static {
        String formattingChars = "0123456789ABCDEFKLMNORabcdefklmnor";
        for (char c : formattingChars.toCharArray()) {
            FORMATS_BY_CHAR[c] = EnumChatFormat.a(c);
        }

        // Just cache all combinations of special format flags for O(1) lookup
        for (int flags = 0; flags < 32; flags++) {
            List<EnumChatFormat> list = new ArrayList<>(5);
            if ((flags & ComponentFormat.OBFUSCATED_MASK) != 0) {
                list.add(EnumChatFormat.OBFUSCATED);
            }
            if ((flags & ComponentFormat.BOLD_MASK) != 0) {
                list.add(EnumChatFormat.BOLD);
            }
            if ((flags & ComponentFormat.STRIKETHROUGH_MASK) != 0) {
                list.add(EnumChatFormat.STRIKETHROUGH);
            }
            if ((flags & ComponentFormat.UNDERLINED_MASK) != 0) {
                list.add(EnumChatFormat.UNDERLINE);
            }
            if ((flags & ComponentFormat.ITALIC_MASK) != 0) {
                list.add(EnumChatFormat.ITALIC);
            }
            FORMATS_BY_FLAGS[flags] = list.toArray(new EnumChatFormat[0]);
        }
    }

    @Override
    protected IChatMutableComponent createEmptyComponent() {
        return new ChatComponentText("");
    }

    @Override
    protected IChatMutableComponent createTextComponent(String text) {
        return new ChatComponentText(text);
    }

    @Override
    protected IChatMutableComponent createFormattedComponent(String text, ComponentFormat format) {
        IChatMutableComponent component = new ChatComponentText(text);

        ChatModifier modifier = getChatModifier(format);
        component.c(modifier);

        return component;
    }

    private ChatModifier getChatModifier(ComponentFormat format) {
        ChatModifier modifier = modifierCache.get(format.getKey());
        if (modifier == null) {
            modifier = createChatModifier(format);
            modifierCache.put(format.getKey(), modifier);
        }
        return modifier;
    }

    private ChatModifier createChatModifier(ComponentFormat format) {
        EnumChatFormat[] formats = FORMATS_BY_FLAGS[format.getFlags()];
        ChatModifier modifier = ChatModifier.b.a(formats);
        if (format.getColor() != -1) {
            modifier = modifier.setColor(ChatHexColor.a(format.getColor()));
        }
        return modifier;
    }

    @Override
    protected void addSibling(IChatMutableComponent parent, IChatMutableComponent child) {
        parent.addSibling(child);
    }

    @Override
    protected EnumChatFormat parseFormat(char c) {
        if (c >= FORMATS_BY_CHAR.length) {
            return null;
        }
        return FORMATS_BY_CHAR[c];
    }

    @Override
    protected void applyFormat(ComponentFormat currentFormat, EnumChatFormat format) {
        if (format.e() != null) {
            currentFormat.setColor(format.e());
            return;
        }
        if (format == EnumChatFormat.RESET) {
            currentFormat.reset();
            return;
        }
        switch (format) {
            case OBFUSCATED:
                currentFormat.setObfuscated();
                break;
            case BOLD:
                currentFormat.setBold();
                break;
            case STRIKETHROUGH:
                currentFormat.setStrikethrough();
                break;
            case UNDERLINE:
                currentFormat.setUnderlined();
                break;
            case ITALIC:
                currentFormat.setItalic();
                break;
            default:
                break;
        }
    }
}
