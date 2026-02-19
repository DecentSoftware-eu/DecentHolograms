package eu.decentsoftware.holograms.nms.v1_10_R1;

import eu.decentsoftware.holograms.nms.api.renderer.NmsClickableHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.server.v1_10_R1.DataWatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class ClickableHologramRenderer implements NmsClickableHologramRenderer {

    private final int entityId;
    private final DataWatcher dataWatcher;

    ClickableHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityId = entityIdGenerator.getFreeEntityId();
        this.dataWatcher = DataWatcherBuilder.create()
                .withInvisible()
                .withNoGravity()
                .withArmorStandProperties(false, false)
                .toDataWatcher();
    }

    @Override
    public void display(Player player, DecentPosition position) {
        EntityPacketsBuilder.create()
                .withSpawnEntityLiving(entityId, EntityType.ARMOR_STAND, position, dataWatcher)
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
