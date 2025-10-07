package eu.decentsoftware.holograms.nms.api;

import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class NmsPacketListenerDelegate implements NmsPacketListener {
    private final List<NmsPacketListener> listeners;

    public NmsPacketListenerDelegate(List<NmsPacketListener> listeners) {
        this.listeners = new ArrayList<>(listeners);
    }

    /**
     * Adds a listener to the delegate.
     *
     * @param listener the listener to add
     */
    public void addListener(NmsPacketListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener from the delegate.
     *
     * @param listener the listener to remove
     */
    public void removeListener(NmsPacketListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onEntityInteract(NmsEntityInteractEvent event) {
        callDelegates(listener -> listener.onEntityInteract(event));
    }

    private void callDelegates(Consumer<NmsPacketListener> call) {
        for (NmsPacketListener listener : listeners) {
            call.accept(listener);
        }
    }
}
