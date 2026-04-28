package eu.decentsoftware.holograms.shared;

/**
 * A generic exception that may be thrown by the internals of DecentHolograms.
 */
public class DecentHologramsException extends RuntimeException {
    public DecentHologramsException(String message) {
        super(message);
    }

    public DecentHologramsException(String message, Throwable cause) {
        super(message, cause);
    }
}
