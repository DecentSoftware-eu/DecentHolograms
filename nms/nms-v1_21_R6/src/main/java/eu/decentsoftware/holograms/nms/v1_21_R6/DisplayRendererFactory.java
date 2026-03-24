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

package eu.decentsoftware.holograms.nms.v1_21_R6;

import eu.decentsoftware.holograms.nms.api.display.NmsBlockDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsDisplayRendererFactory;
import eu.decentsoftware.holograms.nms.api.display.NmsItemDisplayRenderer;
import eu.decentsoftware.holograms.nms.api.display.NmsTextDisplayRenderer;

class DisplayRendererFactory implements NmsDisplayRendererFactory {

    private final EntityIdGenerator entityIdGenerator;

    DisplayRendererFactory(EntityIdGenerator entityIdGenerator) {
        this.entityIdGenerator = entityIdGenerator;
    }

    @Override
    public NmsTextDisplayRenderer createTextDisplayRenderer() {
        return new TextDisplayRenderer(entityIdGenerator.getFreeEntityId());
    }

    @Override
    public NmsItemDisplayRenderer createItemDisplayRenderer() {
        return new ItemDisplayRenderer(entityIdGenerator.getFreeEntityId());
    }

    @Override
    public NmsBlockDisplayRenderer createBlockDisplayRenderer() {
        return new BlockDisplayRenderer(entityIdGenerator.getFreeEntityId());
    }
}
