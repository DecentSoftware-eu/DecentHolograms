package eu.decentsoftware.holograms.nms.v1_8_R2;

import eu.decentsoftware.holograms.nms.api.renderer.NmsEntityHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.server.v1_8_R2.DataWatcher;
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
    public void display(Player player, DecentPosition position, EntityType content) {
        EntityPacketsBuilder.create()
                .withSpawnEntityLiving(armorStandEntityId, EntityType.ARMOR_STAND, offsetPosition(position), armorStandDataWatcher)
                .withSpawnEntityLivingOrObject(entityId, content, position)
                .withTeleportEntity(entityId, position)
                .withPassenger(armorStandEntityId, entityId)
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, DecentPosition position, EntityType content) {
        EntityPacketsBuilder.create()
                .withRemovePassenger(armorStandEntityId)
                .withRemoveEntity(entityId)
                .withSpawnEntityLivingOrObject(entityId, content, position)
                .withTeleportEntity(entityId, position)
                .withPassenger(armorStandEntityId, entityId)
                .sendTo(player);
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
        return new int[]{armorStandEntityId, entityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position.subtractY(1.65);
    }
}
