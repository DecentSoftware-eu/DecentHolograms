package eu.decentsoftware.holograms.nms.api;

import eu.decentsoftware.holograms.shared.DecentPosition;

import java.util.function.Supplier;

/**
 * A class that holds the data for a hologram part.
 *
 * @param <T> the type of content in the hologram part
 * @author d0by
 * @since 2.9.0
 */
public class NmsHologramPartData<T> {

    private final Supplier<DecentPosition> positionSupplier;
    private final Supplier<T> contentSupplier;

    /**
     * Constructs a new NmsHologramPartData object.
     *
     * @param positionSupplier a supplier that provides the position of the hologram part
     * @param contentSupplier  a supplier that provides the content of the hologram part
     */
    public NmsHologramPartData(Supplier<DecentPosition> positionSupplier, Supplier<T> contentSupplier) {
        this.positionSupplier = positionSupplier;
        this.contentSupplier = contentSupplier;
    }

    /**
     * Gets the position of the hologram part.
     *
     * @return the position of the hologram part
     */
    public DecentPosition getPosition() {
        return positionSupplier.get();
    }

    /**
     * Gets the content of the hologram part.
     *
     * @return the content of the hologram part
     */
    public T getContent() {
        return contentSupplier.get();
    }

}
