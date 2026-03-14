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

package eu.decentsoftware.holograms.nms.api.text;

import org.bukkit.ChatColor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A fast implementation of String -> IChatBaseComponent conversion.
 *
 * <p>Since click/hover actions are impossible in holograms, we don't need to parse them.</p>
 *
 * @param <C> The type of component to return (IChatMutableComponent)
 * @param <F> The type of format to apply (EnumChatFormat)
 * @author d0by
 * @since 2.10.0
 */
public abstract class LegacyTextFormattingParser<C, F> extends TextFormattingParser<C, F> {

    private static final int MAX_CACHE_SIZE = 2048;
    private final ThreadLocal<ComponentFormat> formatThreadLocal = ThreadLocal.withInitial(ComponentFormat::new);
    private final ThreadLocal<StringBuilder> stringBuilderThreadLocal = ThreadLocal.withInitial(StringBuilder::new);
    private final Map<String, C> cache;
    private C newLineComponent;

    protected LegacyTextFormattingParser() {
        cache = new LinkedHashMap<String, C>(MAX_CACHE_SIZE, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
    }

    @Override
    public C parseLine(String text) {
        if (text == null || text.isEmpty()) {
            return null;
        }
        return parseNotNull(text);
    }

    @Override
    public C parse(List<String> text) {
        if (text == null || text.isEmpty()) {
            return createEmptyComponent();
        }
        return parseNotNull(text);
    }

    private C parseNotNull(List<String> lines) {
        C root = createEmptyComponent();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            C component = cachedParseNotNull(line);

            if (i > 0) {
                addSibling(root, getNewLineComponent());
            }
            addSibling(root, component);
        }
        return root;
    }

    private C getNewLineComponent() {
        if (newLineComponent == null) {
            newLineComponent = parseNotNull(ChatColor.RESET + "\n");
        }
        return newLineComponent;
    }

    private C cachedParseNotNull(String text) {
        C component = cache.get(text);
        if (component != null) {
            return component;
        }
        component = parseNotNull(text);
        cache.put(text, component);
        return component;
    }

    private C parseNotNull(String text) {
        if (text.indexOf('§') == -1) {
            return createTextComponent(text);
        }

        StringBuilder currentText = stringBuilderThreadLocal.get();
        ComponentFormat currentFormat = formatThreadLocal.get();
        C root = createEmptyComponent();

        char[] chars = text.toCharArray();
        int length = chars.length;
        int i = 0;
        while (i < length) {
            char c = chars[i];
            if (c != '§' || i + 1 >= length) {
                // Regular character - add to content
                currentText.append(c);
                i++;
                continue;
            }

            char nextChar = chars[i + 1];
            if (nextChar != '#' || i + 7 >= length) {
                i = tryParseLegacyFormat(nextChar, root, currentText, currentFormat, i);
            } else {
                i = tryParseHexColor(chars, root, currentText, currentFormat, i);
            }
        }

        flushText(root, currentText, currentFormat);
        currentFormat.reset();
        return root;
    }

    private int tryParseLegacyFormat(
            char nextChar, C root, StringBuilder currentText, ComponentFormat currentFormat, int i) {
        // Try to parse legacy color/format code (§a, §l, etc.)
        F format = parseFormat(nextChar);
        if (format != null) {
            flushText(root, currentText, currentFormat);
            applyFormat(currentFormat, format);
            i += 2;
        } else {
            i++;
        }
        return i;
    }

    private int tryParseHexColor(
            char[] chars, C root, StringBuilder currentText, ComponentFormat currentFormat, int i) {
        // Try to parse hex color (§#RRGGBB)
        int rgb = getRgb(chars, i + 2);
        if (rgb >= 0 && rgb <= 0xffffff) {
            flushText(root, currentText, currentFormat);
            currentFormat.setColor(rgb);
            i += 8;
        } else {
            i++;
        }
        return i;
    }

    private static int getRgb(char[] chars, int startIndex) {
        return (Character.digit(chars[startIndex], 16) << 20)
                | (Character.digit(chars[startIndex + 1], 16) << 16)
                | (Character.digit(chars[startIndex + 2], 16) << 12)
                | (Character.digit(chars[startIndex + 3], 16) << 8)
                | (Character.digit(chars[startIndex + 4], 16) << 4)
                | Character.digit(chars[startIndex + 5], 16);
    }

    private void flushText(C root, StringBuilder text, ComponentFormat format) {
        if (text.length() == 0) {
            // Nothing to flush, continue accumulating format
            return;
        }

        C component = createFormattedComponent(text.toString(), format);
        addSibling(root, component);

        text.setLength(0); // Clear buffer
    }
}
