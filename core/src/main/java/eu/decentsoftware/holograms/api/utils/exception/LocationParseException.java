package eu.decentsoftware.holograms.api.utils.exception;

public class LocationParseException extends Exception {

    private final Reason reason;
    private String worldName;

    public LocationParseException(String message) {
        this(message, Reason.FORMAT);
    }

    public LocationParseException(String message, Reason reason) {
        super(message);
        this.reason = reason;
    }

    public LocationParseException(String message, Reason reason, String worldName) {
        super(message);
        this.reason = reason;
        this.worldName = worldName;
    }

    public Reason getReason() {
        return reason;
    }

    /**
     * Get the world name, if the reason is {@link Reason#WORLD}.
     *
     * @return The world name.
     */
    public String getWorldName() {
        return worldName;
    }

    public enum Reason {
        WORLD, FORMAT
    }

}
