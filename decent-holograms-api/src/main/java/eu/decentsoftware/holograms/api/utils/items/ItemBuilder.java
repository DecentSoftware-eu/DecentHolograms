package eu.decentsoftware.holograms.api.utils.items;

import com.google.common.collect.Lists;
import com.mojang.authlib.properties.Property;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectConstructor;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author D0bby_
 */
@SuppressWarnings("deprecation")
public class ItemBuilder implements Cloneable {

	private static Field profileField;
	private ItemStack itemStack;

	/*
	 *	Constructors
	 */

	public ItemBuilder(Material material) {
		this(material, 1);
	}

	public ItemBuilder(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	public ItemBuilder(Material material, int amount) {
		this(new ItemStack(material, amount));
	}

	public ItemBuilder(Material material, int amount, short durability) {
		this(new ItemStack(material, amount, durability));
	}

	/*
	 *	General Methods
	 */

	@Override
	public ItemBuilder clone() {
		return new ItemBuilder(itemStack);
	}

	public ItemStack toItemStack() {
		return itemStack;
	}

	public ItemStack build() {
		return itemStack;
	}

	public ItemBuilder withItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
		return this;
	}

	/*
	 *	Item Methods
	 */

	public ItemBuilder withMaterial(Material material) {
		itemStack.setType(material);
		return this;
	}

	public ItemBuilder withAmount(int amount) {
		itemStack.setAmount(amount);
		return this;
	}

	public ItemBuilder withDurability(short durability) {
		itemStack.setDurability(durability);
		return this;
	}

	public ItemBuilder withInfiniteDurability() {
		itemStack.setDurability(Short.MAX_VALUE);
		return this;
	}

	public ItemBuilder withName(String displayName) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
		}
		itemStack.setItemMeta(meta);
		return this;
	}

	public String getName() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			return meta.getDisplayName();
		}
		return null;
	}

	public ItemBuilder withEmptyName() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(" ");
		}
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder withLore(List<String> lore) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setLore(Common.colorize(lore));
		}
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder withLore(String... lore) {
		return withLore(Arrays.asList(lore));
	}

	public ItemBuilder withLoreLine(String line) {
		List<String> lore = getLore();
		if (lore != null) {
			lore.add(line);
			withLore(lore);
		} else {
			withLore(line);
		}
		return this;
	}

	public ItemBuilder withLoreLines(String... lines) {
		return withLoreLines(Arrays.asList(lines));
	}

	public ItemBuilder withLoreLines(List<String> lines) {
		List<String> lore = getLore();
		if (lore != null) {
			lore.addAll(lines);
			withLore(lore);
		} else {
			withLore(lines);
		}
		return this;
	}

	public List<String> getLore() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			return meta.getLore();
		}
		return Lists.newArrayList();
	}

	public ItemBuilder removeLore() {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.setLore(null);
		}
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder withItemFlags(ItemFlag... flags) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.addItemFlags(flags);
		}
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder removeItemFlags(ItemFlag... flags) {
		ItemMeta meta = itemStack.getItemMeta();
		if (meta != null) {
			meta.removeItemFlags(flags);
		}
		itemStack.setItemMeta(meta);
		return this;
	}

	public ItemBuilder withEnchantment(Enchantment ench, int level) {
		itemStack.removeEnchantment(ench);
		itemStack.addEnchantment(ench, level);
		return this;
	}

	public ItemBuilder withUnsafeEnchantment(Enchantment ench, int level) {
		itemStack.addUnsafeEnchantment(ench, level);
		return this;
	}

	public ItemBuilder removeEnchantment(Enchantment ench) {
		itemStack.removeEnchantment(ench);
		return this;
	}

	public ItemBuilder withDyeColor(DyeColor color) {
		itemStack.setDurability(color.getDyeData());
		return this;
	}

	/*
	 *	Item Meta Methods
	 */

	public ItemBuilder withLeatherArmorColor(Color color) {
		try {
			LeatherArmorMeta meta = (LeatherArmorMeta) itemStack.getItemMeta();
			if (meta != null) {
				meta.setColor(color);
			}
			itemStack.setItemMeta(meta);
		} catch (ClassCastException ignored) {
		}
		return this;
	}

	public ItemBuilder withPotionType(PotionEffectType type) {
		try {
			PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
			if (meta != null) {
				meta.setMainEffect(type);
			}
			itemStack.setItemMeta(meta);
		} catch (ClassCastException ignored) {
		}
		return this;
	}

	public ItemBuilder withCustomPotionEffect(PotionEffect effect, boolean overwrite) {
		try {
			PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
			if (meta != null) {
				meta.addCustomEffect(effect, overwrite);
			}
			itemStack.setItemMeta(meta);
		} catch (ClassCastException ignored) {
		}
		return this;
	}

	public ItemBuilder removeCustomPotionEffect(PotionEffectType type) {
		try {
			PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
			if (meta != null) {
				meta.removeCustomEffect(type);
			}
			itemStack.setItemMeta(meta);
		} catch (ClassCastException ignored) {
		}
		return this;
	}

	public ItemBuilder clearCustomPotionEffects() {
		try {
			PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
			if (meta != null) {
				meta.clearCustomEffects();
			}
			itemStack.setItemMeta(meta);
		} catch (ClassCastException ignored) {
		}
		return this;
	}

	public ItemBuilder withSkullOwner(String playerName) {
		try {
			SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
			if (meta != null) meta.setOwner(playerName);
			itemStack.setItemMeta(meta);
			if (Common.SERVER_VERSION.isBefore(Version.v1_13_R1)) {
				this.withDurability((short) SkullType.PLAYER.ordinal());
			}
		} catch (ClassCastException ignored) {}
		return this;
	}

	public String getSkullOwner() {
		try {
			SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
			if (meta != null) return meta.getOwner();
		} catch (ClassCastException ignored) {}
		return null;
	}

	private static final ReflectConstructor GAME_PROFILE_CONSTRUCTOR, PROPERTY_CONSTRUCTOR;
	private static final ReflectMethod GAME_PROFILE_GET_PROPERTIES_METHOD, PROPERTY_GET_VALUE_METHOD;

	static {
		Class<?> gameProfileClass = ReflectionUtil.getClass("com.mojang.authlib.GameProfile");
		Class<?> propertyClass = ReflectionUtil.getClass("com.mojang.authlib.properties.Property");
		GAME_PROFILE_CONSTRUCTOR = new ReflectConstructor(gameProfileClass, UUID.class, Object.class);
		PROPERTY_CONSTRUCTOR = new ReflectConstructor(propertyClass, String.class, String.class);
		GAME_PROFILE_GET_PROPERTIES_METHOD = new ReflectMethod(gameProfileClass, "getProperties");
		PROPERTY_GET_VALUE_METHOD = new ReflectMethod(propertyClass, "getValue");
	}

	public ItemBuilder withSkullTexture(String texture) {
		try {
			SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
			if (meta != null) {
				Object gameProfile = GAME_PROFILE_CONSTRUCTOR.newInstance(UUID.randomUUID(), null);
				Map<String, ?> propertiesMap = GAME_PROFILE_GET_PROPERTIES_METHOD.invoke(gameProfile);
				propertiesMap.put("textures", PROPERTY_CONSTRUCTOR.newInstance("texture", texture));
				if (profileField == null) {
					profileField = meta.getClass().getDeclaredField("profile");
					profileField.setAccessible(true);
				}
				profileField.set(meta, gameProfile);
			}
			itemStack.setItemMeta(meta);
			if (Common.SERVER_VERSION.isBefore(Version.v1_13_R1)) {
				this.withDurability((short) SkullType.PLAYER.ordinal());
			}
		} catch (ClassCastException | NoSuchFieldException | IllegalAccessException ignored) {}
		return this;
	}

	public String getSkullTexture() {
		try {
			SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
			if (meta != null) {
				Object gameProfile = GAME_PROFILE_CONSTRUCTOR.newInstance(UUID.randomUUID(), null);
				Map<String, ?> propertiesMap = GAME_PROFILE_GET_PROPERTIES_METHOD.invoke(gameProfile);
				Object property = propertiesMap.get("textures");
				if (property != null) {
					return PROPERTY_GET_VALUE_METHOD.invoke(property);
				}
			}
		} catch (ClassCastException ignored) {}
		return null;
	}

	public ItemBuilder withData(MaterialData data) {
		itemStack.setData(data);
		return this;
	}

	public ItemBuilder withMeta(ItemMeta meta) {
		itemStack.setItemMeta(meta);
		return this;
	}

}
