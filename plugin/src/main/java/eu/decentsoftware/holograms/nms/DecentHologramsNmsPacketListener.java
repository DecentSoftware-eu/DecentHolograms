package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractEvent;

/**
 * This packet listener is responsible for handling player interactions with holograms.
 *
 * @author d0by
 * @since 2.9.0
 */
public class DecentHologramsNmsPacketListener implements NmsPacketListener {

    private final HologramManager hologramManager;

    public DecentHologramsNmsPacketListener(HologramManager hologramManager) {
        this.hologramManager = hologramManager;
    }

    @Override
    public void onEntityInteract(NmsEntityInteractEvent event) {
        boolean processed = hologramManager.onClick(event.getPlayer(), event.getEntityId());
        if (processed) {
            event.setHandled(true);
        }
    }

}
