package eu.decentsoftware.holograms.api.menu;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.utils.items.ItemBuilder;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class MenuButton {

	private final List<MenuClickAction> actions = Lists.newArrayList();
	private ItemBuilder itemBuilder;

	/*
	 *	Constructors
	 */

	public MenuButton() {
		this(new ItemBuilder(Material.STONE));
	}

	public MenuButton(ItemStack itemStack) {
		this.itemBuilder = new ItemBuilder(itemStack);
	}

	public MenuButton(ItemBuilder itemBuilder) {
		this.itemBuilder = itemBuilder;
	}

	/*
	 *	Item Methods
	 */

	public MenuButton withItemBuilder(ItemBuilder itemBuilder) {
		this.itemBuilder = itemBuilder;
		return this;
	}

	public MenuButton withItemStack(ItemStack itemStack) {
		this.itemBuilder.withItemStack(itemStack);
		return this;
	}

	public MenuButton withMaterial(Material material) {
		this.itemBuilder.withMaterial(material);
		return this;
	}

	public ItemStack getItemStack() {
		return itemBuilder.toItemStack();
	}

	/*
	 *	Actions Methods
	 */

	public void executeActions(InventoryClickEvent e) {
		this.actions.forEach(a -> a.execute(e));
	}

	public MenuButton withAction(MenuClickAction action) {
		this.actions.add(action);
		return this;
	}

	public MenuButton removeAction(int index) {
		this.actions.remove(index);
		return this;
	}

	public MenuButton removeAction(MenuClickAction action) {
		this.actions.remove(action);
		return this;
	}

	public MenuButton clearActions() {
		this.actions.clear();
		return this;
	}

}
