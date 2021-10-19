package eu.decentsoftware.holograms.utils.items;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@Data
@AllArgsConstructor
public class HologramItem {

	private ItemStack itemStack;
	private Material material;
	private String nbt = null;
	private short durability = 0;
	private boolean enchanted = false;

	public HologramItem(String string) {
		string = string.trim();

		if (string.contains("{") && string.contains("}")) {
			int nbtStart = string.indexOf('{');
			int nbtEnd = string.lastIndexOf('}');
			if (nbtStart > 0 && nbtEnd > 0 && nbtEnd > nbtStart) {
				this.nbt = string.substring(nbtStart, nbtEnd + 1);
				string = string.substring(0, nbtStart) + string.substring(nbtEnd + 1);
			}
		}

		if (string.contains("!ENCHANTED")) {
			string = string.replace("!ENCHANTED", "").trim();
			this.enchanted = true;
		}

		String materialString = string.trim();
		if (materialString.contains(":")) {
			String[] materialStringSpl = materialString.split(":", 2);
			String materialName = materialStringSpl[0];
			try {
				this.durability = Short.parseShort(materialStringSpl[1]);
			} catch (Throwable t) {
				this.durability = 0;
			}
			this.material = DecentMaterial.parseMaterial(materialName);
		} else {
			this.material = DecentMaterial.parseMaterial(materialString);
		}
		if (this.material == null) {
			material = Material.STONE;
		}
	}

	@SuppressWarnings("deprecation")
	public ItemStack parse() {
		if (itemStack != null) return itemStack;
		ItemBuilder itemBuilder = new ItemBuilder(material);
		if (durability > 0) itemBuilder.withDurability(durability);
		if (enchanted) itemBuilder.withUnsafeEnchantment(Enchantment.DURABILITY, 0);
		if (material.name().contains("SKULL") || material.name().contains("HEAD")) {
			itemBuilder.withDurability((short) SkullType.PLAYER.ordinal());
		}

		itemStack = itemBuilder.toItemStack();
		if (nbt != null) {
			try {
				Bukkit.getUnsafe().modifyItemStack(itemStack, nbt);
			} catch (Throwable ignored) {}
		}
		return itemStack;
	}

}
