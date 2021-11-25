package eu.decentsoftware.holograms.api.actions;

import org.bukkit.event.block.Action;

public enum ClickType {
    LEFT, RIGHT, SHIFT_LEFT, SHIFT_RIGHT;

    public static ClickType fromAction(Action action, boolean sneak) {
        switch (action) {
            case LEFT_CLICK_BLOCK:
            case LEFT_CLICK_AIR:
                return sneak ? ClickType.SHIFT_LEFT : ClickType.LEFT;
            case RIGHT_CLICK_BLOCK:
            case RIGHT_CLICK_AIR:
                return sneak ? ClickType.SHIFT_RIGHT : ClickType.RIGHT;
        }
        return null;
    }

    public static ClickType fromString(String string) {
        switch (string) {
            case "LEFT": return ClickType.LEFT;
            case "SHIFT_LEFT": return ClickType.SHIFT_LEFT;
            case "RIGHT": return ClickType.RIGHT;
            case "SHIFT_RIGHT": return ClickType.SHIFT_RIGHT;
        }
        return null;
    }

}
