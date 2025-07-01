package eu.decentsoftware.holograms.nms.v1_21_R5;

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
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
    public void display(Player player, NmsHologramPartData<ItemStack> data) {
        DecentPosition position = data.getPosition();
        ItemStack content = data.getContent();
        DecentPosition offsetPosition = offsetPosition(position);
        EntityPacketsBuilder.create()
                .withSpawnEntity(entityId, EntityType.ARMOR_STAND, offsetPosition)
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withInvisible()
                        .withNoGravity()
                        .withArmorStandProperties(small, true)
                        .toWatchableObjects())
                .withHelmet(entityId, content)
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, NmsHologramPartData<ItemStack> data) {
        EntityPacketsBuilder.create()
                .withHelmet(entityId, data.getContent())
                .sendTo(player);
    }

    @Override
    public void move(Player player, NmsHologramPartData<ItemStack> data) {
        EntityPacketsBuilder.create()
                .withTeleportEntity(entityId, offsetPosition(data.getPosition()))
                .sendTo(player);
    }

    @Override
    public void hide(Player player) {
        EntityPacketsBuilder.create()
                .withRemoveEntity(entityId)
                .sendTo(player);
    }

    @Override
    public double getHeight(NmsHologramPartData<ItemStack> data) {
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
