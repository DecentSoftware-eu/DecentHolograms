package eu.decentsoftware.holograms.nms.v1_9_R1;

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.renderer.NmsEntityHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.server.v1_9_R1.DataWatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class EntityHologramRenderer implements NmsEntityHologramRenderer {

    private final int entityId;
    private final int armorStandEntityId;
    private final DataWatcher armorStandDataWatcher;

    EntityHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityId = entityIdGenerator.getFreeEntityId();
        this.armorStandEntityId = entityIdGenerator.getFreeEntityId();
        this.armorStandDataWatcher = DataWatcherBuilder.create()
                .withInvisible()
                .withArmorStandProperties(true, true)
                .toDataWatcher();
    }

    @Override
    public void display(Player player, NmsHologramPartData<EntityType> data) {
        DecentPosition position = data.getPosition();
        EntityType content = data.getContent();
        DecentPosition offsetPosition = offsetPosition(position);
        EntityPacketsBuilder.create()
                .withSpawnEntityLiving(armorStandEntityId, EntityType.ARMOR_STAND, offsetPosition, armorStandDataWatcher)
                .withSpawnEntityLivingOrObject(entityId, content, offsetPosition)
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withSilent()
                        .toWatchableObjects())
                .withPassenger(armorStandEntityId, entityId)
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, NmsHologramPartData<EntityType> data) {
        DecentPosition position = data.getPosition();
        EntityType content = data.getContent();
        EntityPacketsBuilder.create()
                .withRemovePassenger(armorStandEntityId)
                .withRemoveEntity(entityId)
                .withSpawnEntityLivingOrObject(entityId, content, position)
                .withTeleportEntity(entityId, position)
                .withPassenger(armorStandEntityId, entityId)
                .sendTo(player);
    }

    @Override
    public void move(Player player, NmsHologramPartData<EntityType> data) {
        hide(player);
        display(player, data);
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
    public double getHeight(NmsHologramPartData<EntityType> data) {
        return EntityTypeRegistry.getEntityTypeHeight(data.getContent());
    }

    @Override
    public int[] getEntityIds() {
        return new int[]{entityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position.subtractY(0.25d);
    }
}
