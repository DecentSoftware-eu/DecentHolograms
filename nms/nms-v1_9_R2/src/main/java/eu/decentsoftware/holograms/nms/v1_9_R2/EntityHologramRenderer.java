package eu.decentsoftware.holograms.nms.v1_9_R2;

import eu.decentsoftware.holograms.nms.api.renderer.NmsEntityHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class EntityHologramRenderer implements NmsEntityHologramRenderer {

    private final int entityId;
    private final int armorStandEntityId;

    EntityHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityId = entityIdGenerator.getFreeEntityId();
        this.armorStandEntityId = entityIdGenerator.getFreeEntityId();
    }

    @Override
    public void display(Player player, DecentPosition position, EntityType content) {
        if (content.isAlive()) {
            EntityPacketsBuilder.create()
                    .withSpawnEntityLiving(entityId, content, offsetPosition(position))
                    .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                            .withSilent()
                            .toWatchableObjects())
                    .sendTo(player);
        } else {
            // Non-living entities must be sitting on an armor stand, otherwise they fall
            EntityPacketsBuilder.create()
                    .withSpawnEntityLiving(armorStandEntityId, EntityType.ARMOR_STAND, offsetPosition(position))
                    .withEntityMetadata(armorStandEntityId, EntityMetadataBuilder.create()
                            .withInvisible()
                            .withArmorStandProperties(true, true)
                            .toWatchableObjects())
                    .withSpawnEntity(entityId, content, offsetPosition(position))
                    .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                            .withSilent()
                            .toWatchableObjects())
                    .withPassenger(armorStandEntityId, entityId)
                    .sendTo(player);
        }
    }

    @Override
    public void updateContent(Player player, DecentPosition position, EntityType content) {
        hide(player);
        display(player, position, content);
    }

    @Override
    public void move(Player player, DecentPosition position) {
        EntityPacketsBuilder.create()
                .withTeleportEntity(armorStandEntityId, offsetPosition(position))
                .sendTo(player);
    }

    @Override
    public void hide(Player player) {
        EntityPacketsBuilder.create()
                .withRemovePassenger(armorStandEntityId)
                .withRemoveEntity(entityId)
                .withRemoveEntity(armorStandEntityId)
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
