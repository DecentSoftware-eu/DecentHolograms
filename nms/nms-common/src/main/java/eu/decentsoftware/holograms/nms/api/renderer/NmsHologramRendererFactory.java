package eu.decentsoftware.holograms.nms.api.renderer;

/**
 * Factory for creating hologram renderers.
 *
 * <p>Implementations of this factory provide instances of hologram renderers
 * suited for rendering different types of holographic elements.</p>
 *
 * @author d0by
 * @since 2.9.0
 */
public interface NmsHologramRendererFactory {

    /**
     * Creates a renderer for displaying text holograms.
     *
     * @return A new instance of {@link NmsTextHologramRenderer}.
     */
    NmsTextHologramRenderer createTextRenderer();


}
