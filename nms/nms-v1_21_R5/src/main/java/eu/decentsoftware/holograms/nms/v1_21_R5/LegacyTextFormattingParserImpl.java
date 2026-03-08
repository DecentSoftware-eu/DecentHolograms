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

package eu.decentsoftware.holograms.nms.v1_21_R5;

import eu.decentsoftware.holograms.nms.api.text.ComponentFormat;
import eu.decentsoftware.holograms.nms.api.text.LegacyTextFormattingParser;
import net.minecraft.EnumChatFormat;
import net.minecraft.network.chat.ChatHexColor;
import net.minecraft.network.chat.ChatModifier;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.IChatMutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;

public final class LegacyTextFormattingParserImpl
        extends LegacyTextFormattingParser<IChatMutableComponent, EnumChatFormat, ChatHexColor> {

    private static final EnumChatFormat[] FORMATS_BY_CHAR = new EnumChatFormat[128];

    static {
        String formattingChars = "0123456789ABCDEFKLMNORabcdefklmnor";
        for (char c : formattingChars.toCharArray()) {
            FORMATS_BY_CHAR[c] = EnumChatFormat.a(c);
        }
    }

    @Override
    protected ComponentFormat<EnumChatFormat, ChatHexColor> createComponentFormat() {
        return new ComponentFormatImpl();
    }

    private static class ComponentFormatImpl extends ComponentFormat<EnumChatFormat, ChatHexColor> {
        @Override
        protected boolean isColor(EnumChatFormat format) {
            return !format.d();
        }

        @Override
        protected boolean isResetFormat(EnumChatFormat format) {
            return format == EnumChatFormat.v;
        }
    }

    @Override
    protected IChatMutableComponent createEmptyComponent() {
        ComponentContents contents = LiteralContents.c;
        return IChatMutableComponent.a(contents);
    }

    @Override
    protected IChatMutableComponent createTextComponent(String text) {
        ComponentContents contents = LiteralContents.a(text);
        return IChatMutableComponent.a(contents);
    }

    @Override
    protected IChatMutableComponent createFormattedComponent(
            String text, ComponentFormat<EnumChatFormat, ChatHexColor> format) {
        ComponentContents contents = LiteralContents.a(text);
        IChatMutableComponent component = IChatMutableComponent.a(contents);

        EnumChatFormat[] formatsArray = format.getFormats().toArray(new EnumChatFormat[0]);
        ChatModifier modifier = ChatModifier.a.a(formatsArray);
        if (format.getColor() != null) {
            modifier = modifier.a(format.getColor());
        }
        component.c(modifier);

        return component;
    }

    @Override
    protected void addSibling(IChatMutableComponent parent, IChatMutableComponent child) {
        parent.b(child);
    }

    @Override
    protected EnumChatFormat parseFormat(char c) {
        if (c >= FORMATS_BY_CHAR.length) {
            return null;
        }
        return FORMATS_BY_CHAR[c];
    }

    @Override
    protected ChatHexColor getColor(int rgb) {
        return ChatHexColor.a(rgb);
    }
}
