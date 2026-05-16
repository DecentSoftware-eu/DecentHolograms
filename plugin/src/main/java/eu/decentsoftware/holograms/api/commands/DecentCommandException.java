package eu.decentsoftware.holograms.api.commands;

/**
 * This class represents an exception that may be thrown while executing a command.
 */
public class DecentCommandException extends RuntimeException {

    private final transient Object[] messageArgs;

    public DecentCommandException(String message) {
        super(message);
        this.messageArgs = new Object[0];
    }

    public DecentCommandException(String message, Object... messageArgs) {
        super(message);
        this.messageArgs = messageArgs;
    }

    public Object[] getMessageArgs() {
        return messageArgs;
    }
}
