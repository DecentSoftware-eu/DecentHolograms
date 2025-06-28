package eu.decentsoftware.holograms.api.utils.items;

import eu.decentsoftware.holograms.api.utils.HeadDatabaseUtils;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import eu.decentsoftware.holograms.hook.NbtApiHook;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
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

    private static final String ENCHANTED_INDICATOR = "!ENCHANTED";
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

    @SuppressWarnings("deprecation")
    public ItemStack parse(Player player) {
        try {
            ItemBuilder itemBuilder = new ItemBuilder(material);
            if (durability > 0) {
                itemBuilder.withDurability(durability);
            }

            if (material.name().contains("SKULL") || material.name().contains("HEAD")) {
                String extrasFinal = parseExtras(player);
                if (StringUtils.isNotEmpty(extrasFinal)) {
                    if (extrasFinal.startsWith("HEADDATABASE_") && Bukkit.getPluginManager().isPluginEnabled("HeadDatabase")) {
                        String headDatabaseId = extrasFinal.substring("HEADDATABASE_".length());
                        itemBuilder.withItemStack(HeadDatabaseUtils.getHeadItemStackById(headDatabaseId));
                    } else if (extrasFinal.length() <= 16) {
                        itemBuilder.withSkullOwner(extrasFinal);
                    } else {
                        itemBuilder.withSkullTexture(extrasFinal);
                    }
                    itemBuilder.withDurability((short) SkullType.PLAYER.ordinal());
                }
            }

            if (enchanted) {
                itemBuilder.withUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }

            ItemStack itemStack = itemBuilder.toItemStack();

            if (nbt != null) {
                itemStack = applyNBT(player, itemStack);
            }

            return itemStack;
        } catch (Exception e) {
            Log.warn("解析物品时出错: %s", e, content);
            return new ItemStack(Material.STONE);
        }
    }

    private String parseExtras(Player player) {
        if (extras == null) {
            return null;
        }
        String extrasFinal = player == null ? extras.trim() : PAPI.setPlaceholders(player, extras).trim();
        extrasFinal = extrasFinal.replace("{player}", player == null ? "" : player.getName());
        return extrasFinal;
    }

    @SuppressWarnings("deprecation")
    private ItemStack applyNBT(Player player, ItemStack itemStack){
        if (Version.afterOrEqual(Version.v1_20_R4)) {
            return NbtApiHook.applyNbtDataToItemStack(itemStack, nbt, player);
        } else {
            try {
                Bukkit.getUnsafe().modifyItemStack(itemStack, nbt);
            } catch (Exception ex) {
                Log.warn("无法将NBT数据应用到物品: %s", ex, nbt);
            }

            return itemStack;
        }
    }

    private void parseContent() {
        String string = this.content;
        string = findExtras(string);
        string = findNBT(string);
        string = checkEnchanted(string);
        parseMaterial(string);

        if (this.material == null) {
            this.material = Material.STONE;
        }
    }

    private void parseMaterial(String string) {
        String materialString = string.trim().split(" ", 2)[0];
        String materialName = materialString;
        if (materialString.contains(":")) {
            String[] materialStringSpl = materialString.split(":", 2);
            materialName = materialStringSpl[0];
            try {
                this.durability = Short.parseShort(materialStringSpl[1]);
            } catch (Exception e) {
                this.durability = 0;
            }
        }
        this.material = DecentMaterial.parseMaterial(materialName);
    }

    private String checkEnchanted(String string) {
        if (string.contains(ENCHANTED_INDICATOR)) {
            string = string.replace(ENCHANTED_INDICATOR, "");
            this.enchanted = true;
        }
        return string;
    }

    private String findNBT(String string) {
        if (string.contains("{") && string.contains("}")) {
            int nbtStart = string.indexOf('{');
            int nbtEnd = string.lastIndexOf('}');
            if (nbtStart > 0 && nbtEnd > 0 && nbtEnd > nbtStart) {
                this.nbt = string.substring(nbtStart, nbtEnd + 1);
                string = string.substring(0, nbtStart) + string.substring(nbtEnd + 1);
            }
        }
        return string;
    }

    private String findExtras(String string) {
        if (string.contains("(") && string.contains(")")) {
            int extrasStart = string.indexOf('(');
            int extrasEnd = string.lastIndexOf(')');
            if (extrasStart > 0 && extrasEnd > 0 && extrasEnd > extrasStart) {
                this.extras = string.substring(extrasStart + 1, extrasEnd);
                string = string.substring(0, extrasStart) + string.substring(extrasEnd + 1);
            }
        }
        return string;
    }
    
    /**
     * Takes the provided ItemStack and converts it into a usable HologramItem instance.<br>
     * This is done by converting the ItemStack values into a String equal to what is used when adding an Item to
     * a Hologram Page (i.e. {@code PLAYER_HEAD (Steve)} or {@code DIAMOND_SWORD !ENCHANTED}.
     * 
     * <p><b>IMPORTANT NOTE!</b><br>
     * Due to limitations in the parsing does this method only use specific values, namely:
     * <ul>
     *     <li>Item name</li>
     *     <li>Item Durability (Will be added after the Name with a colon separation)</li>
     *     <li>Enchantments (Will add {@value ENCHANTED_INDICATOR})</li>
     *     <li>Skull Owner/Texture (Texture is prioritized)</li>
     *     <li>CustomModelData (custom_model_data on newer MC versions).</li>
     * </ul>
     * 
     * @param itemStack The Item to convert into a HologramItem.
     * @return Usable HologramItem instance with data from the provided ItemStack.
     */
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
            stringBuilder.append(ENCHANTED_INDICATOR).append(" ");
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

        float customModelData = NbtApiHook.extractCustomModelData(itemStack);
        if (customModelData > 0.0) {
            stringBuilder.append("{CustomModelData:").append(customModelData).append('}');
        }
        return new HologramItem(stringBuilder.toString());
    }

    public static ItemStack parseItemStack(String string, Player player) {
        string = PAPI.setPlaceholders(player, string);
        return new HologramItem(string).parse(player);
    }

}
