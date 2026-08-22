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

import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisplayClickableServiceTest {

    @Test
    void getClickableAmount_textDisplayUsesTwoStandsPerTenLines() throws Exception {
        DisplayClickableService service = new DisplayClickableService(null, new DisplayEntityRegistry());

        assertEquals(2, invokeClickableAmount(service, createTextDisplay(1)));
        assertEquals(2, invokeClickableAmount(service, createTextDisplay(5)));
        assertEquals(2, invokeClickableAmount(service, createTextDisplay(10)));
        assertEquals(4, invokeClickableAmount(service, createTextDisplay(11)));
        assertEquals(4, invokeClickableAmount(service, createTextDisplay(20)));
        assertEquals(6, invokeClickableAmount(service, createTextDisplay(21)));
    }

    @Test
    void getClickableAmount_itemDisplayUsesPair() throws Exception {
        DisplayClickableService service = new DisplayClickableService(null, new DisplayEntityRegistry());
        ItemDisplay display = new ItemDisplay("item", new DecentLocation("world", 0, 64, 0, 0f, 0f), new DisplaySettings());
        display.setMaterial("minecraft:stone");

        assertEquals(2, invokeClickableAmount(service, display));
    }

    private TextDisplay createTextDisplay(int lineCount) {
        TextDisplay display = new TextDisplay("test", new DecentLocation("world", 0, 64, 0, 0f, 0f), new DisplaySettings());
        for (int i = 0; i < lineCount; i++) {
            display.addLine("line " + i);
        }
        return display;
    }

    private int invokeClickableAmount(DisplayClickableService service, DisplayBase display) throws Exception {
        Method method = DisplayClickableService.class.getDeclaredMethod("getClickableAmount", DisplayBase.class);
        method.setAccessible(true);
        return (int) method.invoke(service, display);
    }
}
