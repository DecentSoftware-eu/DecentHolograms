package eu.decentsoftware.holograms.shared.reflect;

import eu.decentsoftware.holograms.shared.DecentHologramsException;

public class DecentHologramsReflectException extends DecentHologramsException {
    public DecentHologramsReflectException(String message) {
        super(message);
    }

    public DecentHologramsReflectException(String message, Throwable cause) {
        super(message, cause);
    }
}
