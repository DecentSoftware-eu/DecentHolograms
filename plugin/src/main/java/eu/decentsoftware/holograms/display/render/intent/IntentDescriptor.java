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

package eu.decentsoftware.holograms.display.render.intent;

import eu.decentsoftware.holograms.display.render.state.DisplayRenderState;
import eu.decentsoftware.holograms.platform.api.render.metadata.MetadataKey;

public abstract class IntentDescriptor {

    private final DisplayRenderState renderState;

    protected IntentDescriptor(DisplayRenderState renderState) {
        this.renderState = renderState;
    }

    public DisplayRenderState getRenderState() {
        return renderState;
    }

    public static class SpawnDisplay extends IntentDescriptor {
        public SpawnDisplay(DisplayRenderState renderState) {
            super(renderState);
        }
    }

    public static class DespawnDisplay extends IntentDescriptor {
        public DespawnDisplay(DisplayRenderState renderState) {
            super(renderState);
        }
    }

    public static class UpdateDisplayContent extends IntentDescriptor {
        public UpdateDisplayContent(DisplayRenderState renderState) {
            super(renderState);
        }
    }

    public static class UpdateMetadata<T> extends IntentDescriptor {
        private final MetadataKey<T> metadataKey;

        public UpdateMetadata(DisplayRenderState renderState, MetadataKey<T> metadataKey) {
            super(renderState);
            this.metadataKey = metadataKey;
        }

        public MetadataKey<T> getMetadataKey() {
            return metadataKey;
        }
    }

    public static class Move extends IntentDescriptor {
        public Move(DisplayRenderState renderState) {
            super(renderState);
        }
    }
}
