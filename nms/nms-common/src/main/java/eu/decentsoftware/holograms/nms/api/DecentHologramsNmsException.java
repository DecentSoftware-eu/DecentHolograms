package eu.decentsoftware.holograms.nms.api;

import eu.decentsoftware.holograms.shared.DecentHologramsException;

/**
 * An exception that may be thrown by an NMS implementation.
 *
 * @author d0by
 * @since 2.9.0
 */
public class DecentHologramsNmsException extends DecentHologramsException {
    public DecentHologramsNmsException(String message) {
        super(message);
    }

    public DecentHologramsNmsException(String message, Throwable cause) {
        super(message, cause);
    }
}
