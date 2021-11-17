package eu.decentsoftware.holograms.api.menu;

import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.utils.items.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuUtils {

	private static ItemStack borderItem, closeItem, backItem;

	public static ItemStack getBorderItem() {
		if (borderItem == null) {
			borderItem = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE)
					.withDurability((short) 7)
					.withName("&bDecent&fHolograms")
					.toItemStack();
		}
		return borderItem;
	}

	public static ItemStack getBorderItem(Material material) {
		if (material == null) return getBorderItem();
		ItemStack customBorderItem = getBorderItem().clone();
		customBorderItem.setType(material);
		return customBorderItem;
	}

	public static ItemStack getCloseItem() {
		if (closeItem == null) {
			closeItem = new ItemBuilder(Material.BARRIER)
					.withName("&cClose")
					.toItemStack();
		}
		return closeItem;
	}

	public static ItemStack getBackItem(String prevName) {
		if (backItem == null) {
			backItem = new ItemBuilder(Material.BARRIER)
					.withName("&b&lBack")
					.withLore(
							"&7Go back to &b" + prevName + "&7."
					).toItemStack();
		}
		return backItem;
	}

	public static Set<Integer> getBorderSlots(int size) {
		if (size < 27 || size % 9 != 0) return Sets.newHashSet();
		Set<Integer> set = Sets.newHashSet(0,1,2,3,4,5,6,7,8,size-2,size-3,size-4,size-5,size-6,size-7,size-8);
		for (int i = 1; i < size / 9; i++) {
			set.add(i * 9);
			set.add(i * 9 + 8);
		}
		return set;
	}

	public static Set<Integer> getBorderSlotsNoSides(int size) {
		if (size < 27 || size % 9 != 0) return Sets.newHashSet();
		return Sets.newHashSet(0,1,2,3,4,5,6,7,8,size-1,size-2,size-3,size-4,size-5,size-6,size-7,size-8,size-9);
	}

	public static Set<Integer> getBorderSlotsFull(int size) {
		if (size < 27 || size % 9 != 0) return Sets.newHashSet();
		return IntStream.range(0, size).boxed().collect(Collectors.toSet());
	}

	public static Set<Integer> getBorderSlotsPaginated(int size) {
		if (size < 27 || size % 9 != 0) return Sets.newHashSet();
		Set<Integer> set = Sets.newHashSet(0,1,2,3,4,5,6,7,8,size-2-9,size-3-9,size-4-9,size-5-9,size-6-9,size-7-9,size-8-9);
		for (int i = 1; i < size / 9; i++) {
			set.add(i * 9);
			set.add(i * 9 + 8);
		}
		return set;
	}

}
