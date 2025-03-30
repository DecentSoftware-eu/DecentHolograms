package eu.decentsoftware.holograms.nms.v1_8_R3;

import eu.decentsoftware.holograms.nms.api.renderer.NmsTextHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.server.v1_8_R3.DataWatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class TextHologramRenderer implements NmsTextHologramRenderer {

    private final int armorStandEntityId;
    private final DataWatcher dataWatcher;

    TextHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.armorStandEntityId = entityIdGenerator.getFreeEntityId();
        this.dataWatcher = DataWatcherBuilder.create()
                .withInvisible()
                .withArmorStandProperties(true, true)
                .toDataWatcher();
    }

    @Override
    public void display(Player player, DecentPosition position, String content) {
        EntityPacketsBuilder.create()
                .withSpawnEntityLiving(armorStandEntityId, EntityType.ARMOR_STAND, offsetPosition(position), dataWatcher)
                .withEntityMetadata(armorStandEntityId, EntityMetadataBuilder.create()
                        .withCustomName(content)
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, DecentPosition position, String content) {
        EntityPacketsBuilder.create()
                .withEntityMetadata(armorStandEntityId, EntityMetadataBuilder.create()
                        .withCustomName(content)
                        .toWatchableObjects())
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
                .withRemoveEntity(armorStandEntityId)
                .sendTo(player);
    }

    @Override
    public double getHeight(String content) {
        return 0.25d;
    }

    @Override
    public int[] getEntityIds() {
        return new int[]{armorStandEntityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position.subtractY(0.5);
    }
}
