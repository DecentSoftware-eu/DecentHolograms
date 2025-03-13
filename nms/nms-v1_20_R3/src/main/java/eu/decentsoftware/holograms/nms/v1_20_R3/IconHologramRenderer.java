package eu.decentsoftware.holograms.nms.v1_20_R3;

import eu.decentsoftware.holograms.nms.api.renderer.NmsIconHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class IconHologramRenderer implements NmsIconHologramRenderer {

    private final int itemEntityId;
    private final int armorStandEntityId;

    IconHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this.itemEntityId = entityIdGenerator.getFreeEntityId();
        this.armorStandEntityId = entityIdGenerator.getFreeEntityId();
    }

    @Override
    public void display(Player player, DecentPosition position, ItemStack content) {
        EntityPacketsBuilder.create()
                .withSpawnEntity(armorStandEntityId, EntityType.ARMOR_STAND, offsetPosition(position))
                .withEntityMetadata(armorStandEntityId, EntityMetadataBuilder.create()
                        .withInvisible()
                        .withArmorStandProperties(true, true)
                        .toWatchableObjects())
                .withSpawnEntity(itemEntityId, EntityType.DROPPED_ITEM, position)
                .withEntityMetadata(itemEntityId, EntityMetadataBuilder.create()
                        .withItemStack(content)
                        .toWatchableObjects())
                .withTeleportEntity(itemEntityId, position)
                .withPassenger(armorStandEntityId, itemEntityId)
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, DecentPosition position, ItemStack content) {
        EntityPacketsBuilder.create()
                .withEntityMetadata(itemEntityId, EntityMetadataBuilder.create()
                        .withItemStack(content)
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
                .withRemovePassenger(armorStandEntityId)
                .withRemoveEntity(itemEntityId)
                .withRemoveEntity(armorStandEntityId)
                .sendTo(player);
    }

    @Override
    public double getHeight(ItemStack content) {
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
