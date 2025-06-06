package eu.decentsoftware.holograms.nms.v1_20_R3;

import eu.decentsoftware.holograms.nms.api.renderer.NmsClickableHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsEntityHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHeadHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsHologramRendererFactory;
import eu.decentsoftware.holograms.nms.api.renderer.NmsIconHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsSmallHeadHologramRenderer;
import eu.decentsoftware.holograms.nms.api.renderer.NmsTextHologramRenderer;

class HologramRendererFactory implements NmsHologramRendererFactory {

    private final EntityIdGenerator entityIdGenerator;

    HologramRendererFactory(EntityIdGenerator entityIdGenerator) {
        this.entityIdGenerator = entityIdGenerator;
    }

    @Override
    public NmsTextHologramRenderer createTextRenderer() {
        return new TextHologramRenderer(entityIdGenerator);
    }

    @Override
    public NmsIconHologramRenderer createIconRenderer() {
        return new IconHologramRenderer(entityIdGenerator);
    }

    @Override
    public NmsHeadHologramRenderer createHeadRenderer() {
        return new HeadHologramRenderer(entityIdGenerator);
    }

    @Override
    public NmsSmallHeadHologramRenderer createSmallHeadRenderer() {
        return new SmallHeadHologramRenderer(entityIdGenerator);
    }

    @Override
    public NmsEntityHologramRenderer createEntityRenderer() {
        return new EntityHologramRenderer(entityIdGenerator);
    }

    @Override
    public NmsClickableHologramRenderer createClickableRenderer() {
        return new ClickableHologramRenderer(entityIdGenerator);
    }

}
