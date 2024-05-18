package eu.decentsoftware.holograms.api.utils.items;

import de.tr7zw.changeme.nbtapi.NBTItem;
import eu.decentsoftware.holograms.api.utils.HeadDatabaseUtils;
import eu.decentsoftware.holograms.api.utils.PAPI;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

@Data
@AllArgsConstructor
public class HologramItem {

    private final String content;
    private String nbt;
    private String extras;
    private Material material;
    private short durability = 0;
    private boolean enchanted = false;

    public HologramItem(String string) {
        this.content = string;
        this.parseContent();
    }

    public ItemStack parse() {
        return this.parse(null);
    }

    public ItemStack parse(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(material);
        if (durability > 0) itemBuilder.withDurability(durability);
        if (material.name().contains("SKULL") || material.name().contains("HEAD")) {
            if (extras != null) {
                String extrasFinal = player == null ? extras.trim() : PAPI.setPlaceholders(player, extras).trim();
                extrasFinal = extrasFinal.replace("{player}", player == null ? "" : player.getName());
                if (!extrasFinal.isEmpty()) {
                    if (extrasFinal.startsWith("HEADDATABASE_") && Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
                        String headDatabaseId = extrasFinal.substring("HEADDATABASE_".length());
                        itemBuilder.withItemStack(HeadDatabaseUtils.getHeadItemStackById(headDatabaseId));
                    } else if (extrasFinal.length() <= 16) {
                        itemBuilder.withSkullOwner(extrasFinal);
                    } else {
                        itemBuilder.withSkullTexture(extrasFinal);
                    }
                    //noinspection deprecation
                    itemBuilder.withDurability((short) SkullType.PLAYER.ordinal());
                }
            }
        }
        if (enchanted) itemBuilder.withUnsafeEnchantment(Enchantment.DURABILITY, 0);

        ItemStack itemStack = itemBuilder.toItemStack();
        if (nbt != null) {
            try {
                // noinspection deprecation
                Bukkit.getUnsafe().modifyItemStack(itemStack, player == null ? nbt : PAPI.setPlaceholders(player, nbt));
            } catch (Throwable ignored) {
            }
        }
        return itemStack;
    }

    private void parseContent() {
        String string = this.content;

        // Find extras
        if (string.contains("(") && string.contains(")")) {
            int extrasStart = string.indexOf('(');
            int extrasEnd = string.lastIndexOf(')');
            if (extrasStart > 0 && extrasEnd > 0 && extrasEnd > extrasStart) {
                this.extras = string.substring(extrasStart + 1, extrasEnd);
                string = string.substring(0, extrasStart) + string.substring(extrasEnd + 1);
            }
        }

        // Find NBT tag
        if (string.contains("{") && string.contains("}")) {
            int nbtStart = string.indexOf('{');
            int nbtEnd = string.lastIndexOf('}');
            if (nbtStart > 0 && nbtEnd > 0 && nbtEnd > nbtStart) {
                this.nbt = string.substring(nbtStart, nbtEnd + 1);
                string = string.substring(0, nbtStart) + string.substring(nbtEnd + 1);
            }
        }

        if (string.contains("!ENCHANTED")) {
            string = string.replace("!ENCHANTED", "");
            this.enchanted = true;
        }

        // Parse material
        String materialString = string.trim().split(" ", 2)[0];
        String materialName = materialString;
        if (materialString.contains(":")) {
            String[] materialStringSpl = materialString.split(":", 2);
            materialName = materialStringSpl[0];
            try {
                this.durability = Short.parseShort(materialStringSpl[1]);
            } catch (Throwable t) {
                this.durability = 0;
            }
        }
        this.material = DecentMaterial.parseMaterial(materialName);

        // Material couldn't be parsed, set it to stone.
        if (this.material == null) {
            this.material = Material.STONE;
        }
    }

    @SuppressWarnings("deprecation")
    public static HologramItem fromItemStack(ItemStack itemStack) {
        Validate.notNull(itemStack);

        StringBuilder stringBuilder = new StringBuilder();
        ItemBuilder itemBuilder = new ItemBuilder(itemStack);
        Material material = itemStack.getType();
        stringBuilder.append(material.name());
        int durability = itemStack.getDurability();
        if (durability > 0) {
            stringBuilder.append(":").append(durability);
        }
        stringBuilder.append(" ");
        Map<Enchantment, Integer> enchants = itemStack.getEnchantments();
        if (enchants != null && !enchants.isEmpty()) {
            stringBuilder.append("!ENCHANTED").append(" ");
        }
        if (material.name().contains("HEAD") || material.name().contains("SKULL")) {
            String owner = itemBuilder.getSkullOwner();
            String texture = itemBuilder.getSkullTexture();
            if (texture != null) {
                stringBuilder.append("(").append(texture).append(")");
            } else if (owner != null && !owner.isEmpty()) {
                stringBuilder.append("(").append(owner).append(")");
            }
        }
        NBTItem nbtItem = new NBTItem(itemStack);
        if (nbtItem.hasTag("CustomModelData")) {
            int customModelData = nbtItem.getInteger("CustomModelData");
            stringBuilder.append(" {CustomModelData:").append(customModelData).append("}");
        }
        return new HologramItem(stringBuilder.toString());
    }

    public static ItemStack parseItemStack(String string, Player player) {
        string = PAPI.setPlaceholders(player, string);
        return new HologramItem(string).parse(player);
    }

}
