package eu.decentsoftware.holograms.nms.v1_14_R1;

import eu.decentsoftware.holograms.nms.api.renderer.NmsEntityHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class EntityHologramRenderer implements NmsEntityHologramRenderer {

    private final int entityId;

    EntityHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityId = entityIdGenerator.getFreeEntityId();
    }

    @Override
    public void display(Player player, DecentPosition position, EntityType content) {
        EntityPacketsBuilder.create()
                .withSpawnEntityLivingOrObject(entityId, content, offsetPosition(position))
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withSilent()
                        .withNoGravity()
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, DecentPosition position, EntityType content) {
        hide(player);
        display(player, position, content);
    }

    @Override
    public void move(Player player, DecentPosition position) {
        EntityPacketsBuilder.create()
                .withTeleportEntity(entityId, offsetPosition(position))
                .sendTo(player);
    }

    @Override
    public void hide(Player player) {
        EntityPacketsBuilder.create()
                .withRemoveEntity(entityId)
                .sendTo(player);
    }

    @Override
    public double getHeight(EntityType content) {
        return EntityTypeRegistry.getEntityTypeHeight(content);
    }

    @Override
    public int[] getEntityIds() {
        return new int[]{entityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position.subtractY(0.25d);
    }
}
