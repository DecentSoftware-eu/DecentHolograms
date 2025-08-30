package eu.decentsoftware.holograms.nms.v1_8_R2;

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.renderer.NmsIconHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import net.minecraft.server.v1_8_R2.DataWatcher;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class IconHologramRenderer implements NmsIconHologramRenderer {

    private final int itemEntityId;
    private final int armorStandEntityId;
    private final DataWatcher armorStandDataWatcher;

    IconHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.itemEntityId = entityIdGenerator.getFreeEntityId();
        this.armorStandEntityId = entityIdGenerator.getFreeEntityId();
        this.armorStandDataWatcher = DataWatcherBuilder.create()
                .withInvisible()
                .withArmorStandProperties(true, true)
                .toDataWatcher();
    }

    @Override
    public void display(Player player, NmsHologramPartData<ItemStack> data) {
        DecentPosition position = data.getPosition();
        ItemStack content = data.getContent();
        EntityPacketsBuilder.create()
                .withSpawnEntityLiving(armorStandEntityId, EntityType.ARMOR_STAND, offsetPosition(position), armorStandDataWatcher)
                .withSpawnEntity(itemEntityId, EntityType.DROPPED_ITEM, position)
                .withEntityMetadata(itemEntityId, EntityMetadataBuilder.create()
                        .withItemStack(content)
                        .toWatchableObjects())
                .withTeleportEntity(itemEntityId, position)
                .attachEntity(itemEntityId, armorStandEntityId)
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, NmsHologramPartData<ItemStack> data) {
        EntityPacketsBuilder.create()
                .withEntityMetadata(itemEntityId, EntityMetadataBuilder.create()
                        .withItemStack(data.getContent())
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void move(Player player, NmsHologramPartData<ItemStack> data) {
        EntityPacketsBuilder.create()
                .withTeleportEntity(armorStandEntityId, offsetPosition(data.getPosition()))
                .sendTo(player);
    }

    @Override
    public void hide(Player player) {
        EntityPacketsBuilder.create()
                .unattachEntity(itemEntityId)
                .withRemoveEntity(itemEntityId)
                .withRemoveEntity(armorStandEntityId)
                .sendTo(player);
    }

    @Override
    public double getHeight(NmsHologramPartData<ItemStack> data) {
        return 0.5d;
    }

    @Override
    public int[] getEntityIds() {
        return new int[]{armorStandEntityId, itemEntityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position.subtractY(0.55);
    }
}
