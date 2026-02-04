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

import eu.decentsoftware.holograms.display.BlockDisplay;
import eu.decentsoftware.holograms.display.DisplayBase;
import eu.decentsoftware.holograms.display.render.DisplayRenderContext;
import eu.decentsoftware.holograms.platform.api.data.BlockDescriptor;
import eu.decentsoftware.holograms.platform.api.data.display.BlockDisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayContent;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;

public class BlockDisplayTypeDefinition implements DisplayTypeDefinition<BlockDescriptor> {

    @Override
    public DisplayType getType() {
        return DisplayType.BLOCK;
    }

    @Override
    public DisplayContent<BlockDescriptor> resolveContent(DisplayBase display, DisplayRenderContext context) {
        BlockDisplay blockDisplay = getBlockDisplay(display);

        BlockDescriptor blockDescriptor = new BlockDescriptor(blockDisplay.getMaterial());
        return new BlockDisplayContent(blockDescriptor);
    }

    private BlockDisplay getBlockDisplay(DisplayBase displayBase) {
        if (!(displayBase instanceof BlockDisplay)) {
            throw new IllegalArgumentException("Display is not a text display");
        }
        return (BlockDisplay) displayBase;
    }
}
