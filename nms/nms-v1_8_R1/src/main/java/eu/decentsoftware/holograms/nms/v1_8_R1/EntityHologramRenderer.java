package eu.decentsoftware.holograms.nms.v1_8_R1;

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.renderer.NmsEntityHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.server.v1_8_R1.DataWatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

// Some methods are synchronized to make sure no two threads generate a new entity ID at the same time,
// causing one of them to be lost.
// That could possibly result in an entity being displayed for a client and never destroyed.
class EntityHologramRenderer implements NmsEntityHologramRenderer {

    private final EntityIdGenerator entityIdGenerator;
    private int entityId;
    private final int armorStandEntityId;
    private final DataWatcher armorStandDataWatcher;

    EntityHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityIdGenerator = entityIdGenerator;
        this.entityId = entityIdGenerator.getFreeEntityId();
        this.armorStandEntityId = entityIdGenerator.getFreeEntityId();
        this.armorStandDataWatcher = DataWatcherBuilder.create()
                .withInvisible()
                .withArmorStandProperties(true, true)
                .toDataWatcher();
    }

    @Override
    public synchronized void display(Player player, NmsHologramPartData<EntityType> data) {
        DecentPosition position = data.getPosition();
        EntityType content = data.getContent();
        EntityPacketsBuilder.create()
                .withSpawnEntityLiving(armorStandEntityId, EntityType.ARMOR_STAND, offsetPosition(position), armorStandDataWatcher)
                .withSpawnEntityLivingOrObject(entityId, content, position)
                .withTeleportEntity(entityId, position)
                .attachEntity(entityId, armorStandEntityId)
                .sendTo(player);
    }

    @Override
    public synchronized void updateContent(Player player, NmsHologramPartData<EntityType> data) {
        // To work around a client-side issue where despawning and immediately respawning an entity
        // with the same ID in a single game tick causes it to not display properly,
        // we generate a new entity ID on update.
        int oldEntityId = entityId;
        entityId = entityIdGenerator.getFreeEntityId();

        DecentPosition position = data.getPosition();
        EntityType content = data.getContent();
        EntityPacketsBuilder.create()
                .unattachEntity(entityId)
                .withRemoveEntity(oldEntityId)
                .withSpawnEntityLivingOrObject(entityId, content, position)
                .withTeleportEntity(entityId, position)
                .attachEntity(entityId, armorStandEntityId)
                .sendTo(player);
    }

    @Override
    public synchronized void move(Player player, NmsHologramPartData<EntityType> data) {
        DecentPosition position = data.getPosition();
        EntityPacketsBuilder.create()
                .withTeleportEntity(armorStandEntityId, offsetPosition(position))
                .withEntityHeadLook(entityId, position.getYaw())
                .sendTo(player);
    }

    @Override
    public synchronized void hide(Player player) {
        EntityPacketsBuilder.create()
                .unattachEntity(entityId)
                .withRemoveEntity(entityId)
                .withRemoveEntity(armorStandEntityId)
                .sendTo(player);
    }

    @Override
    public double getHeight(NmsHologramPartData<EntityType> data) {
        return EntityTypeRegistry.getEntityTypeHeight(data.getContent());
    }

    @Override
    public synchronized int[] getEntityIds() {
        return new int[]{armorStandEntityId, entityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position.subtractY(1.65);
    }
}
