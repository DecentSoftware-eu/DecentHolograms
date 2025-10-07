package eu.decentsoftware.holograms.api.expansion.context;

import eu.decentsoftware.holograms.api.commands.DecentCommand;
import lombok.Builder;

/**
 * Represents a command registration call passed through the expansion context.
 *
 * @author ZorTik
 */
@Builder
public class RegisterCommandCall {
    /**
     * The command to be registered.
     */
    private final DecentCommand command;

    /**
     * The parent command to which this command is a subcommand. If null, the command is registered as a main command.
     */
    @Builder.Default
    private final DecentCommand parent = null;

    /**
     * Gets the command to be registered.
     *
     * @return the command to be registered
     */
    public DecentCommand getCommand() {
        return command;
    }

    /**
     * Gets the parent command if this command is a subcommand.
     *
     * @return the parent command, or null if this is a main command
     */
    public DecentCommand getParent() {
        return parent;
    }
}
