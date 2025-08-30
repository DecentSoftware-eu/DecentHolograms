package eu.decentsoftware.holograms.nms.v1_21_R5;

import eu.decentsoftware.holograms.nms.api.NmsHologramPartData;
import eu.decentsoftware.holograms.nms.api.display.data.TextDisplayData;
import eu.decentsoftware.holograms.nms.api.display.renderer.NmsTextDisplayRenderer;
import eu.decentsoftware.holograms.shared.DecentPosition;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

class TextDisplayRenderer implements NmsTextDisplayRenderer {

    private final int entityId;

    TextDisplayRenderer(EntityIdGenerator entityIdGenerator) {
        this.entityId = entityIdGenerator.getFreeEntityId();
    }

    @Override
    public void display(Player player, NmsHologramPartData<TextDisplayData> data) {
        TextDisplayData textDisplayData = data.getContent();
        DecentPosition position = data.getPosition();
        EntityPacketsBuilder.create()
                .withSpawnEntity(entityId, EntityType.TEXT_DISPLAY, offsetPosition(position))
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withNoGravity()
                        .withTextDisplayText(textDisplayData.getText())
                        .withTextDisplayProperties(
                                textDisplayData.getTextShadowAttribute(),
                                textDisplayData.getSeeThroughAttribute(),
                                textDisplayData.getAlignmentAttribute()
                        )
                        .withTextDisplayTextOpacity(textDisplayData.getTextOpacityAttribute())
                        .withTextDisplayBackground(textDisplayData.getBackgroundColorAttribute())
                        .withTextDisplayLineWidth(textDisplayData.getLineWidthAttribute())
                        .withDisplayTranslation(textDisplayData.getTranslation())
                        .withDisplayScale(textDisplayData.getScale())
                        .withDisplayBillboardConstraints(textDisplayData.getBillboardConstraints())
                        .withDisplayBrightness(textDisplayData.getBrightnessOverride())
                        .withDisplayShadowRadius(textDisplayData.getShadowRadius())
                        .withDisplayShadowStrength(textDisplayData.getShadowStrength())
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void updateProperties(Player player, NmsHologramPartData<TextDisplayData> data) {
        TextDisplayData textDisplayData = data.getContent();
        EntityPacketsBuilder.create()
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withTextDisplayProperties(
                                textDisplayData.getTextShadowAttribute(),
                                textDisplayData.getSeeThroughAttribute(),
                                textDisplayData.getAlignmentAttribute()
                        )
                        .withTextDisplayTextOpacity(textDisplayData.getTextOpacityAttribute())
                        .withTextDisplayBackground(textDisplayData.getBackgroundColorAttribute())
                        .withTextDisplayLineWidth(textDisplayData.getLineWidthAttribute())
                        .withDisplayTranslation(textDisplayData.getTranslation())
                        .withDisplayScale(textDisplayData.getScale())
                        .withDisplayBillboardConstraints(textDisplayData.getBillboardConstraints())
                        .withDisplayBrightness(textDisplayData.getBrightnessOverride())
                        .withDisplayShadowRadius(textDisplayData.getShadowRadius())
                        .withDisplayShadowStrength(textDisplayData.getShadowStrength())
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void updateContent(Player player, NmsHologramPartData<TextDisplayData> data) {
        EntityPacketsBuilder.create()
                .withEntityMetadata(entityId, EntityMetadataBuilder.create()
                        .withTextDisplayText(data.getContent().getText())
                        .toWatchableObjects())
                .sendTo(player);
    }

    @Override
    public void move(Player player, NmsHologramPartData<TextDisplayData> data) {
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
    public double getHeight(NmsHologramPartData<TextDisplayData> data) {
        return 0d;
    }

    @Override
    public int[] getEntityIds() {
        return new int[]{entityId};
    }

    private DecentPosition offsetPosition(DecentPosition position) {
        return position;
    }
}
