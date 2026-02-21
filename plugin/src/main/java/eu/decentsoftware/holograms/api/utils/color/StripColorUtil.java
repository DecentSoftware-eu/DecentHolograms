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

package eu.decentsoftware.holograms.api.utils.color;

/**
 * Utility class for removing color formatting from strings.
 *
 * @author d0by
 * @since 2.10.0
 */
public class StripColorUtil {

    private static final boolean[] IS_COLOR_CHAR = new boolean[128];

    static {
        String colorChars = "0123456789ABCDEFKLMNORabcdefklmnor";
        for (char c : colorChars.toCharArray()) {
            IS_COLOR_CHAR[c] = true;
        }
    }

    private StripColorUtil() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Removes legacy color codes, represented by '&' or '§' followed by a valid color character, from the given string.
     *
     * <p><b>Valid color characters include:</b>
     * <ul>
     *   <li>Color codes: 0-9, a-f (or A-F)</li>
     *   <li>Special format codes: k, l, m, n, o, r (or K, L, M, N, O, R)</li>
     * </ul>
     * </p>
     *
     * <p><b>Examples:</b>
     * <ul>
     *   <li>{@code stripLegacyColorCodes("&cRed Text") -> "Red Text"}</li>
     *   <li>{@code stripLegacyColorCodes("§aGreen &lBold") -> "Green Bold"}</li>
     *   <li>{@code stripLegacyColorCodes("No colors here") -> "No colors here"}</li>
     *   <li>{@code stripLegacyColorCodes("&gInvalid &cValid") -> "&gInvalid Valid"}</li>
     *   <li>{@code stripLegacyColorCodes("&a&b&cText") -> "Text"}</li>
     * </ul>
     * </p>
     *
     * @param string the input string potentially containing color codes
     * @return the input string with color codes removed
     * @author d0by
     * @since 2.10.0
     */
    public static String stripLegacyColorCodes(String string) {
        if (string.indexOf('&') == -1 && string.indexOf('§') == -1) {
            return string;
        }

        int length = string.length();
        StringBuilder result = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);

            if ((c == '&' || c == '§') && i + 1 < length) {
                char next = string.charAt(i + 1);
                if (next < 128 && IS_COLOR_CHAR[next]) {
                    i++;
                    continue;
                }
            }

            result.append(c);
        }

        return result.toString();
    }

    /**
     * Extracts special color codes, like "&l" or "§O", from the given string and returns them as a string
     * along with the cleaned input string.
     *
     * <p>Special format codes: k, l, m, n, o (or K, L, M, N, O)</p>
     *
     * @param string the input string potentially containing special color codes
     * @return {@link SpecialColorFormattingExtractionResult} containing the cleaned string and the special color codes
     * @since 2.10.0
     */
    public static SpecialColorFormattingExtractionResult extractSpecialColorsFormatting(String string) {
        if (string.indexOf('&') == -1 && string.indexOf('§') == -1) {
            return new SpecialColorFormattingExtractionResult(string, "");
        }

        StringBuilder specialColors = new StringBuilder();
        int length = string.length();
        StringBuilder cleanedString = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = string.charAt(i);

            if ((c == '&' || c == '§') && i + 1 < length) {
                char next = string.charAt(i + 1);
                if (isSpecialColorChar(next)) {
                    i++;
                    specialColors.append(c).append(next);
                    continue;
                }
            }

            cleanedString.append(c);
        }
        return new SpecialColorFormattingExtractionResult(cleanedString.toString(), specialColors.toString());
    }

    private static boolean isSpecialColorChar(char c) {
        c |= 32; // force lowercase for ASCII letters
        return c == 'k' || c == 'l' || c == 'm' || c == 'n' || c == 'o';
    }
}
