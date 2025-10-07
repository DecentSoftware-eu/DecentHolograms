package eu.decentsoftware.holograms.api.expansion.context;

import eu.decentsoftware.holograms.api.expansion.config.ExpansionConfig;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;

import java.util.UUID;

/**
 * Per-expansion context to hold resources lifecycle, tied to the expansion.
 *
 * @author ZorTik
 */
public interface ExpansionContext {

    /**
     * Registers a command.
     *
     * @param registerCommandCall the command registration details
     * @return a unique identifier for the registration
     */
    UUID registerCommand(RegisterCommandCall registerCommandCall);

    /**
     * Unregisters a command.
     *
     * @param registrationId the unique identifier of the registration
     */
    void unregisterCommand(UUID registrationId);

    /**
     * Registers an NMS packet listener that will be automatically attached to all online players
     * and any players that join while the listener is registered.
     *
     * @param listener the NMS packet listener to register
     */
    void registerNmsPacketListener(NmsPacketListener listener);

    /**
     * Unregisters a previously registered NMS packet listener.
     *
     * @param listener the NMS packet listener to unregister
     */
    void unregisterNmsPacketListener(NmsPacketListener listener);

    /**
     * Adds an event handler to listen for context events.
     *
     * @param handler the event handler to add
     */
    void addContextEventHandler(ExpansionContextEventHandler handler);

    /**
     * Gets the current configuration of the expansion.
     *
     * @return the current expansion configuration
     */
    ExpansionConfig getExpansionConfig();

    /**
     * Cleans up all associated resources with the holding expansion.
     */
    void close();

    /**
     * Checks if the context is already closed.
     *
     * @return true if the context is closed, false otherwise
     */
    boolean isClosed();
}
