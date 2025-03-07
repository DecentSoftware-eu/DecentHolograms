package eu.decentsoftware.holograms.nms.v1_8_R3;

import eu.decentsoftware.holograms.nms.api.renderer.NmsHeadHologramRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class HeadHologramRenderer implements NmsHeadHologramRenderer {

    private final int entityId;
    private final boolean small;

    HeadHologramRenderer(EntityIdGenerator entityIdGenerator) {
        this(entityIdGenerator, false);
    }

    protected HeadHologramRenderer(EntityIdGenerator entityIdGenerator, boolean small) {
        this.entityId = entityIdGenerator.getFreeEntityId();
        this.small = small;
    }

    @Override
    public void display(Player player, DecentPosition position, ItemStack content) {
        EntityPacketsBuilder.create()
                .withSpawnEntityLiving(entityId, EntityType.ARMOR_STAND, offsetPosition(position))
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withInvisible()
                        .withArmorStandProperties(small, true)
                        .toWatchableObjects())
                .withHelmet(entityId, content)
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, DecentPosition position, ItemStack content) {
        EntityPacketsBuilder.create()
                .withHelmet(entityId, content)
                .sendTo(player);
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
    public double getHeight(ItemStack content) {
        return small ? 0.5d : 0.7d;
    }

    @Override
    public int[] getEntityIds() {
        return new int[]{entityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        double offsetY = small ? 1.1875d : 2.0d;
        return position.subtractY(offsetY);
    }
}
