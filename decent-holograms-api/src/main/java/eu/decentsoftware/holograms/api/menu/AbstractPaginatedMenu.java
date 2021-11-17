package eu.decentsoftware.holograms.api.menu;

import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.player.DecentPlayer;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.items.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class AbstractPaginatedMenu<T> extends AbstractMenu {

	protected final int totalPages;
	protected int[] objectSlots;
	protected int currentPage;
	protected String searchTerm;

	/*
	 *	Constructors
	 */

	public AbstractPaginatedMenu(DecentPlayer parent, String searchTerm) {
		this(parent, searchTerm, null);
	}

	public AbstractPaginatedMenu(DecentPlayer parent, String searchTerm, AbstractMenu previousMenu) {
		super(parent, previousMenu);
		this.searchTerm = searchTerm;
		this.currentPage = 1;
		this.totalPages = (getMatchingButtons().size() / getItemsPerPage()) +
				(getMatchingButtons().size() % getItemsPerPage() == 0 ? 0 : 1);
	}

	/*
	 *	Abstract Methods
	 */

	protected abstract int getItemsPerPage();
	protected abstract List<T> getObjects();
	protected abstract MenuSearchMatcher getMenuSearchMatcher();

	protected abstract void constructButton(MenuButton button, T object);

	/*
	 *	General Methods
	 */

	@Override
	public void open() {
		this.open(currentPage);
	}

	public void open(int page) {
		final Player player = parent.getPlayer();
		final MenuModel menuModel = new MenuModel();

		this.currentPage = page;
		construct(menuModel);

		if (inventory == null || !player.getOpenInventory().getTitle().equals(menuModel.getTitle()) || inventory.getSize() != menuModel.getSize()) {
			inventory = Bukkit.createInventory(this, menuModel.getSize(), Common.colorize(menuModel.getTitle()));
			lastTitle = menuModel.getTitle();
			player.openInventory(inventory);
		}
		inventory.clear();

		menuModel.getBorderSlots().forEach((slot) ->
				inventory.setItem(slot, MenuUtils.getBorderItem())
		);

		menuModel.getEntries().forEach((slot, entry) -> {
			MenuButton button = new MenuButton();
			entry.setMenuButton(button);
			inventory.setItem(slot, button.getItemStack());
			addMenuClickActions(slot, button.getActions());
		});

		List<MenuButton> matchingButtons = getMatchingButtons();
		MenuButton button;
		int start = (currentPage - 1) * getItemsPerPage();
		int freeSlot;

		for (int i = start; i < (start + getItemsPerPage()); i++) {
			if (matchingButtons.size() <= i)
				break;

			button = matchingButtons.get(i);
			if (objectSlots != null) {
				freeSlot = objectSlots[i - start];
			} else {
				freeSlot = getFirstFreeSlot();
			}

			if (freeSlot == -1) break;
			inventory.setItem(freeSlot, button.getItemStack());
			actions.remove(freeSlot);
			addMenuClickActions(freeSlot, button.getActions());
		}

		if (updating) {
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

	public void nextPage() {
		if (!canOpenNextPage()) return;
		this.open(currentPage + 1);
	}

	public void prevPage() {
		if (!canOpenPrevPage()) return;
		this.open(currentPage - 1);
	}

	protected boolean canOpenNextPage() {
		return currentPage < totalPages;
	}

	protected boolean canOpenPrevPage() {
		return currentPage > 1;
	}

	private List<MenuButton> getMatchingButtons() {
		MenuSearchMatcher menuSearchMatcher = getMenuSearchMatcher();
		List<MenuButton> buttons = Lists.newArrayList();
		List<T> objects = getObjects();
		for (T object : objects) {
			MenuButton button = new MenuButton();
			constructButton(button, object);
			if (searchTerm == null || searchTerm.isEmpty() || menuSearchMatcher == null || menuSearchMatcher.matches(searchTerm, button)) {
				buttons.add(button);
			}
		}
		return buttons;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}

	/*
	 *	Preset Buttons
	 */

	protected void NextPageButton(MenuButton button) {
		button.withItemBuilder(new ItemBuilder(Material.ARROW)
				.withName("&b&lNext Page")
		);
		button.withAction((e) -> this.nextPage());
	}

	protected void PrevPageButton(MenuButton button) {
		button.withItemBuilder(new ItemBuilder(Material.ARROW)
				.withName("&b&lPrevious Page")
		);
		button.withAction((e) -> this.prevPage());
	}

}
