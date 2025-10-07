package eu.decentsoftware.holograms.api.expansion.context;

import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.expansion.config.ExpansionConfig;
import eu.decentsoftware.holograms.nms.NmsPacketListenerService;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of the {@link ExpansionContext} interface.
 *
 * @author ZorTik
 */
public class DefaultExpansionContext implements ExpansionContext {
    private final CommandManager commandManager;
    private final NmsPacketListenerService packetListenerService;
    private final ExpansionConfig config;
    private final Logger logger;

    private final Map<UUID, Runnable> commandUnregisterCallbacks;
    private final List<NmsPacketListener> registeredPacketListeners;
    private final List<ExpansionContextEventHandler> eventHandlers;

    private boolean closed;

    public DefaultExpansionContext(
            CommandManager commandManager,
            NmsPacketListenerService packetListenerService, ExpansionConfig config, Logger logger) {
        this.commandManager = commandManager;
        this.packetListenerService = packetListenerService;
        this.config = config;
        this.logger = logger;
        this.commandUnregisterCallbacks = new ConcurrentHashMap<>();
        this.registeredPacketListeners = new CopyOnWriteArrayList<>();
        this.eventHandlers = new CopyOnWriteArrayList<>();
        this.closed = false;
    }

    @Override
    public UUID registerCommand(RegisterCommandCall registerCommandCall) {
        Runnable unregisterCallback = doRegisterCommandReturnCallback(registerCommandCall);

        UUID registrationId = UUID.randomUUID();
        commandUnregisterCallbacks.put(registrationId, unregisterCallback);

        return registrationId;
    }

    /**
     * Registers the command specified in the given {@link RegisterCommandCall} and returns a callback
     * to unregister it.
     *
     * @param registerCommandCall the command registration details
     * @return a {@link Runnable} that, when run, will unregister the command
     */
    private @NotNull Runnable doRegisterCommandReturnCallback(RegisterCommandCall registerCommandCall) {
        DecentCommand command = registerCommandCall.getCommand();
        DecentCommand parent = registerCommandCall.getParent();

        Runnable unregisterCallback;
        if (parent == null) {
            commandManager.registerCommand(command);

            unregisterCallback = () -> commandManager.unregisterCommand(command.getName());
        } else {
            parent.addSubCommand(command);

            unregisterCallback = () -> parent.removeSubCommand(command);
        }

        return unregisterCallback;
    }

    @Override
    public void unregisterCommand(UUID registrationId) {
        Runnable unregisterCallback = commandUnregisterCallbacks.remove(registrationId);

        if (unregisterCallback != null) {
            unregisterCallback.run();
        }
    }

    @Override
    public void registerNmsPacketListener(NmsPacketListener listener) {
        packetListenerService.registerAutoListener(listener);

        registeredPacketListeners.add(listener);
    }

    @Override
    public void unregisterNmsPacketListener(NmsPacketListener listener) {
        registeredPacketListeners.remove(listener);

        packetListenerService.unregisterAutoListener(listener);
    }

    @Override
    public void addContextEventHandler(ExpansionContextEventHandler handler) {
        eventHandlers.add(handler);
    }

    @Override
    public ExpansionConfig getExpansionConfig() {
        return config;
    }

    /**
     * Calls all registered event handlers with the given call.
     *
     * @param call the call to make on each handler
     */
    private void callEventHandlers(Consumer<ExpansionContextEventHandler> call) {
        for (ExpansionContextEventHandler handler : eventHandlers) {
            try {
                call.accept(handler);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Exception while calling context event handler.", e);
            }
        }
    }

    @Override
    public void close() {
        if (isClosed()) {
            return;
        }

        for (Runnable unregisterCallback : commandUnregisterCallbacks.values()) {
            unregisterCallback.run();
        }
        commandUnregisterCallbacks.clear();

        for (NmsPacketListener listener : registeredPacketListeners) {
            packetListenerService.unregisterAutoListener(listener);
        }
        registeredPacketListeners.clear();

        closed = true;

        callEventHandlers(handler -> handler.onContextClosed(this));
    }

    @Override
    public boolean isClosed() {
        return closed;
    }
}
