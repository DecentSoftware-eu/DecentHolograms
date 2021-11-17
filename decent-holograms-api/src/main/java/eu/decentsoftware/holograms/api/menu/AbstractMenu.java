package eu.decentsoftware.holograms.api.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import eu.decentsoftware.holograms.api.utils.Common;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public abstract class AbstractMenu implements InventoryHolder {

	protected static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

	// Actions set on slots, item is not needed.
	protected final Map<Integer, List<MenuClickAction>> actions = Maps.newHashMap();

	protected final DecentPlayer parent;
	protected Inventory inventory;
	protected AbstractMenu previousMenu;
	protected BukkitTask updateTask = null;
	protected String lastTitle = null;
	protected boolean updating = false;

	/*
	 *	Constuctors
	 */

	public AbstractMenu(DecentPlayer parent) {
		this(parent, null);
	}

	public AbstractMenu(DecentPlayer parent, AbstractMenu previousMenu) {
		this.parent = parent;
		this.previousMenu = previousMenu;
	}

	/*
	 *	Abstract Methods
	 */

	public abstract void construct(MenuModel menuModel);

	/*
	 *	General Methods
	 */

	public void open() {
		if (!parent.isOnline()) return;

		Player player = parent.getPlayer();
		MenuModel menuModel = new MenuModel();
		construct(menuModel);

		if (inventory == null || !player.getOpenInventory().getTitle().equals(menuModel.getTitle()) || inventory.getSize() != menuModel.getSize()) {
			if (menuModel.getInventoryType() != null) {
				inventory = Bukkit.createInventory(this, menuModel.getInventoryType(), Common.colorize(menuModel.getTitle()));
			} else {
				inventory = Bukkit.createInventory(this, menuModel.getSize(), Common.colorize(menuModel.getTitle()));
			}
			lastTitle = menuModel.getTitle();
			player.openInventory(inventory);
		}

		menuModel.getBorderSlots().forEach((slot) -> {
			if (inventory.getSize() > slot) {
				inventory.setItem(slot, MenuUtils.getBorderItem());
			}
		});


		menuModel.getEntries().forEach((slot, entry) -> {
			if (inventory.getSize() > slot) {
				MenuButton button = new MenuButton();
				entry.setMenuButton(button);
				inventory.setItem(slot, button.getItemStack());
				addMenuClickActions(slot, button.getActions());
			}
		});

		if (updating && updateTask == null) {
			this.updateTask = Bukkit.getScheduler().runTaskTimer(DECENT_HOLOGRAMS.getPlugin(), () -> {
				if (!updating) {
					this.updateTask.cancel();
					this.updateTask = null;
					return;
				}
				this.open();
			}, 0L, 1L);
		}
	}

	public void close() {
		if (!parent.isOnline()) return;

		Player player = parent.getPlayer();
		player.closeInventory();
	}

	public void onClick(InventoryClickEvent e) {
		if (e == null) return;

		e.setCancelled(true);
		if (e.getClick().isShiftClick() || e.getClick().isKeyboardClick()) {
			return;
		}

		int slot = e.getSlot();
		if (actions.containsKey(slot)) {
			for (MenuClickAction action : new ArrayList<>(actions.get(slot))) {
				action.execute(e);
			}
		}
	}

	public void onClose(InventoryCloseEvent e) {
		if (updateTask != null) {
			updateTask.cancel();
			updateTask = null;
		}
	}

	public void onDrag(InventoryDragEvent e) {
		e.setCancelled(true);
	}

	protected int getFirstFreeSlot() {
		ItemStack[] contents = inventory.getContents();
		for (int i = 0; i < contents.length; i++)
			if (contents[i] == null || contents[i].getType().equals(Material.AIR))
				return i;
		return -1;
	}

	/*
	 *	Menu Click Actions Methods
	 */

	protected void addMenuClickAction(int slot, MenuClickAction action) {
		if (!actions.containsKey(slot)) {
			actions.put(slot, Lists.newArrayList());
		}
		actions.get(slot).add(action);
	}

	protected void addMenuClickActions(int slot, List<MenuClickAction> actionList) {
		if (!actions.containsKey(slot)) {
			actions.put(slot, Lists.newArrayList());
		}
		actions.get(slot).addAll(actionList);
	}

	/*
	 *	Preset Buttons
	 */

	protected void CloseButton(MenuButton button) {
		button.withItemStack(MenuUtils.getCloseItem());
		button.withAction((e) -> e.getWhoClicked().closeInventory());
	}

	protected void BackButton(MenuButton button) {
		button.withItemStack(MenuUtils.getBackItem(previousMenu.getLastTitle()));
		button.withAction((e) -> previousMenu.open());
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

}
