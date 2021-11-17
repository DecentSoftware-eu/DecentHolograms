package eu.decentsoftware.holograms.api.menu;

import org.bukkit.event.inventory.InventoryClickEvent;

@FunctionalInterface
public interface MenuClickAction {
	void execute(InventoryClickEvent e);
}
