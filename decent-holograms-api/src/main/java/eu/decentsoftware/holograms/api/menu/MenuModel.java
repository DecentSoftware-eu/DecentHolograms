package eu.decentsoftware.holograms.api.menu;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryType;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class MenuModel {

	private final Set<Integer> borderSlots = Sets.newHashSet();
	private final Map<Integer, MenuEntry> entries = Maps.newHashMap();
	private InventoryType inventoryType;
	private String title = "Unnamed Menu";
	private int size = 27;

	/*
	 *	Entry Methods
	 */

	public MenuModel withButton(int slot, MenuEntry entry) {
		this.entries.put(slot, entry);
		this.borderSlots.remove(slot);
		return this;
	}

	public MenuModel removeButton(int slot) {
		this.entries.remove(slot);
		return this;
	}

	/*
	 *	Border Methods
	 */

	public void addBorderSlot(int slot) {
		this.borderSlots.add(slot);
	}

	public void setBorderSlots(Set<Integer> slots) {
		this.borderSlots.clear();
		this.borderSlots.addAll(slots);
	}

}
