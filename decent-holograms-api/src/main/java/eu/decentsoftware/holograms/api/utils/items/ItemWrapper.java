package eu.decentsoftware.holograms.api.utils.items;

import eu.decentsoftware.holograms.api.utils.Common;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ItemWrapper {

	protected Material material;
	protected String name;
	protected String skullOwner;
	protected String skullTexture;
	protected int amount;
	protected short durability;
	protected List<String> lore;
	protected Map<Enchantment, Integer> enchantments;
	protected ItemFlag[] flags;

	public boolean canParse() {
		return material != null;
	}

	public ItemStack parse() {
		ItemBuilder itemBuilder = new ItemBuilder(material == null ? Material.STONE : material);
		if (name != null) {
			itemBuilder.withName(Common.colorize(name));
		}
		itemBuilder.withAmount(amount);
		itemBuilder.withDurability(durability);
		if (lore != null && !lore.isEmpty()) {
			itemBuilder.withLore(Common.colorize(lore));
		}
		if (enchantments != null && !enchantments.isEmpty()) {
			enchantments.forEach(itemBuilder::withEnchantment);
		}
		if (skullOwner != null) {
			itemBuilder.withSkullOwner(skullOwner);
		} else if (skullTexture != null) {
			itemBuilder.withSkullTexture(skullTexture);
		}
		itemBuilder.withItemFlags(flags);
		return itemBuilder.build();
	}

	public ItemStack parse(Player player) {
		return this.parseBuilder(player).build();
	}

	public ItemBuilder parseBuilder(Player player) {
		ItemBuilder itemBuilder = new ItemBuilder(material == null ? Material.STONE : material);
		if (name != null) {
			itemBuilder.withName(Common.colorize(name));
		}
		itemBuilder.withAmount(amount);
		itemBuilder.withDurability(durability);
		if (lore != null && !lore.isEmpty()) {
			itemBuilder.withLore(Common.colorize(lore));
		}
		if (enchantments != null && !enchantments.isEmpty()) {
			enchantments.forEach(itemBuilder::withEnchantment);
		}
		if (skullOwner != null) {
			itemBuilder.withSkullOwner(skullOwner.equals("@") ? player.getName() : skullOwner);
		} else if (skullTexture != null) {
			itemBuilder.withSkullTexture(skullTexture);
		}
		itemBuilder.withItemFlags(flags);
		return itemBuilder;
	}

}
