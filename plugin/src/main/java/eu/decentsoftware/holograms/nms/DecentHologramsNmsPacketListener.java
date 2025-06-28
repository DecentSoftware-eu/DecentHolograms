package eu.decentsoftware.holograms.nms;

import eu.decentsoftware.holograms.api.actions.ClickType;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.nms.api.NmsPacketListener;
import eu.decentsoftware.holograms.nms.api.event.NmsEntityInteractAction;
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
        ClickType clickType = mapEntityInteractActionToClickType(event.getAction());
        boolean processed = hologramManager.onClick(event.getPlayer(), event.getEntityId(), clickType);
        if (processed) {
            event.setHandled(true);
        }
    }

    private ClickType mapEntityInteractActionToClickType(NmsEntityInteractAction action) {
        if (action != null) {
            switch (action) {
                case LEFT_CLICK:
                    return ClickType.LEFT;
                case RIGHT_CLICK:
                    return ClickType.RIGHT;
                case SHIFT_LEFT_CLICK:
                    return ClickType.SHIFT_LEFT;
                case SHIFT_RIGHT_CLICK:
                    return ClickType.SHIFT_RIGHT;
            }
        }
        throw new IllegalArgumentException("未知操作: " + action);
    }

}
