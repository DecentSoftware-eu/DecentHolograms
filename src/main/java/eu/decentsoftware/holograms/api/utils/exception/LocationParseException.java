package eu.decentsoftware.holograms.api.utils.exception;

public class LocationParseException extends Exception {

    private final Reason reason;

    public LocationParseException(String message) {
        this(message, Reason.FORMAT);
    }

    public LocationParseException(String message, Reason reason) {
        super(message);
        this.reason = reason;
    }

    public Reason getReason() {
        return reason;
    }

    public static enum Reason {
        WORLD, FORMAT
    }

}
