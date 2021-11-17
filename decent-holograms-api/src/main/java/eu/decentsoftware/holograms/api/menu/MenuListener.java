package eu.decentsoftware.holograms.api.menu;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getClickedInventory() == null) return;
		InventoryHolder holder = e.getClickedInventory().getHolder();
		if (holder instanceof AbstractMenu) {
			((AbstractMenu) holder).onClick(e);
		}
	}

	@EventHandler
	public void onDrag(InventoryDragEvent e) {
		if (e.getInventory() == null) return;
		InventoryHolder holder = e.getInventory().getHolder();
		if (holder instanceof AbstractMenu) {
			((AbstractMenu) holder).onDrag(e);
		}
	}

	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		if (e.getInventory() == null) return;
		InventoryHolder holder = e.getInventory().getHolder();
		if (holder instanceof AbstractMenu) {
			((AbstractMenu) holder).onClose(e);
		}
	}

}
