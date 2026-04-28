package eu.decentsoftware.holograms.nms.v1_20_R3;

import eu.decentsoftware.holograms.nms.api.renderer.NmsClickableHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class ClickableHologramRenderer implements NmsClickableHologramRenderer {

    private final int entityId;

    ClickableHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityId = entityIdGenerator.getFreeEntityId();
    }

    @Override
    public void display(Player player, DecentPosition position) {
        EntityPacketsBuilder.create()
                .withSpawnEntity(entityId, EntityType.ARMOR_STAND, position)
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withInvisible()
                        .withNoGravity()
                        .withArmorStandProperties(false, false)
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void move(Player player, DecentPosition position) {
        EntityPacketsBuilder.create()
                .withTeleportEntity(entityId, position)
                .sendTo(player);
    }

    @Override
    public void hide(Player player) {
        EntityPacketsBuilder.create()
                .withRemoveEntity(entityId)
                .sendTo(player);
    }

    @Override
    public int getEntityId() {
        return entityId;
    }
}
