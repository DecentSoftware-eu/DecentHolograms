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

import eu.decentsoftware.holograms.api.Settings;
import eu.decentsoftware.holograms.display.attribute.DisplayAttribute;
import eu.decentsoftware.holograms.display.attribute.definition.YawAttributeDefinition;
import eu.decentsoftware.holograms.display.attribute.value.AttributeValue;
import eu.decentsoftware.holograms.display.attribute.value.primitives.FloatValue;
import eu.decentsoftware.holograms.nms.api.renderer.NmsClickableHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRendererFactory;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import eu.decentsoftware.holograms.platform.api.data.display.DisplayType;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class DisplayClickableService {

    private static final int LINES_PER_TIER = 10;
    private static final int STANDS_PER_TIER = 2;
    private static final double SIDE_OFFSET = 0.45;

    private final NmsHologramRendererFactory hologramRendererFactory;
    private final DisplayEntityRegistry entityRegistry;
    private final Map<String, List<NmsClickableHologramRenderer>> clickableRenderersByDisplay = new ConcurrentHashMap<>();

    public DisplayClickableService(NmsHologramRendererFactory hologramRendererFactory, DisplayEntityRegistry entityRegistry) {
        this.hologramRendererFactory = hologramRendererFactory;
        this.entityRegistry = entityRegistry;
    }

    public void show(DisplayBase display, Player player) {
        if (!display.hasActions()) {
            hide(display, player);
            entityRegistry.unregister(display.getName());
            return;
        }

        int amount = getClickableAmount(display);
        List<NmsClickableHologramRenderer> renderers = getClickableRenderers(display, amount);
        registerEntityIds(display.getName(), renderers, amount);

        List<DecentPosition> positions = getClickablePositions(display);
        for (int i = 0; i < amount; i++) {
            renderers.get(i).display(player, positions.get(i));
        }
    }

    public void hide(DisplayBase display, Player player) {
        List<NmsClickableHologramRenderer> renderers = clickableRenderersByDisplay.get(display.getName());
        if (renderers == null) {
            return;
        }
        renderers.forEach(renderer -> renderer.hide(player));
    }

    public void move(DisplayBase display, Player player) {
        if (!display.hasActions()) {
            hide(display, player);
            return;
        }

        int amount = getClickableAmount(display);
        List<NmsClickableHologramRenderer> renderers = getClickableRenderers(display, amount);
        registerEntityIds(display.getName(), renderers, amount);

        List<DecentPosition> positions = getClickablePositions(display);
        for (int i = 0; i < renderers.size(); i++) {
            if (i < amount) {
                renderers.get(i).move(player, positions.get(i));
            } else {
                renderers.get(i).hide(player);
            }
        }
    }

    public void respawn(DisplayBase display, Player player) {
        hide(display, player);
        show(display, player);
    }

    public void unload(String displayName) {
        clickableRenderersByDisplay.remove(displayName);
        entityRegistry.unregister(displayName);
    }

    private List<NmsClickableHologramRenderer> getClickableRenderers(DisplayBase display, int amount) {
        return clickableRenderersByDisplay.compute(display.getName(), (name, renderers) -> {
            List<NmsClickableHologramRenderer> current = renderers == null ? new ArrayList<>() : renderers;
            while (current.size() < amount) {
                current.add(hologramRendererFactory.createClickableRenderer());
            }
            return current;
        });
    }

    private void registerEntityIds(String displayName, List<NmsClickableHologramRenderer> renderers, int amount) {
        entityRegistry.registerClickableEntities(
                displayName,
                renderers.stream()
                        .limit(amount)
                        .map(NmsClickableHologramRenderer::getEntityId)
                        .collect(Collectors.toList())
        );
    }

    private int getClickableAmount(DisplayBase display) {
        return getTierCount(display) * STANDS_PER_TIER;
    }

    private int getTierCount(DisplayBase display) {
        if (display.getType() != DisplayType.TEXT) {
            return 1;
        }

        TextDisplay textDisplay = (TextDisplay) display;
        int lineCount = Math.max(1, textDisplay.getLines().size());
        return (lineCount + LINES_PER_TIER - 1) / LINES_PER_TIER;
    }

    private List<DecentPosition> getClickablePositions(DisplayBase display) {
        DecentLocation location = display.getLocation();
        float yaw = getDisplayYaw(display);
        float pitch = location.getPitch();
        List<DecentPosition> positions = new ArrayList<>();

        if (display.getType() != DisplayType.TEXT) {
            addTierPositions(positions, location.getX(), location.getY() + 0.5, location.getZ(), yaw, pitch);
            return positions;
        }

        TextDisplay textDisplay = (TextDisplay) display;
        int lineCount = Math.max(1, textDisplay.getLines().size());
        int tiers = getTierCount(display);
        double lineHeight = Settings.DEFAULT_HEIGHT_TEXT;
        double totalHeight = lineCount * lineHeight;
        double tierHeight = LINES_PER_TIER * lineHeight;

        for (int tier = 0; tier < tiers; tier++) {
            double tierY = location.getY() - totalHeight + (tier * tierHeight) + (tierHeight / 2.0);
            addTierPositions(positions, location.getX(), tierY, location.getZ(), yaw, pitch);
        }
        return positions;
    }

    private void addTierPositions(List<DecentPosition> positions, double x, double y, double z, float yaw, float pitch) {
        DecentPosition center = new DecentPosition(x, y, z, yaw, pitch);
        positions.add(offsetSideways(center, yaw, -SIDE_OFFSET));
        positions.add(offsetSideways(center, yaw, SIDE_OFFSET));
    }

    private DecentPosition offsetSideways(DecentPosition center, float yaw, double offset) {
        double yawRadians = Math.toRadians(yaw);
        double rightX = -Math.cos(yawRadians);
        double rightZ = -Math.sin(yawRadians);
        return new DecentPosition(
                center.getX() + rightX * offset,
                center.getY(),
                center.getZ() + rightZ * offset,
                center.getYaw(),
                center.getPitch()
        );
    }

    private float getDisplayYaw(DisplayBase display) {
        DisplayAttribute<Float> yawAttribute = display.getAttribute(YawAttributeDefinition.KEY);
        if (yawAttribute != null) {
            AttributeValue<Float> value = yawAttribute.getValue();
            if (value instanceof FloatValue) {
                return ((FloatValue) value).getValue();
            }
        }
        return display.getLocation().getYaw();
    }
}
