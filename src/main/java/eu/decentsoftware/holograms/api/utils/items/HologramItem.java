package eu.decentsoftware.holograms.api.utils.items;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.NBTItem;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.utils.DataFixerUtil;
import eu.decentsoftware.holograms.api.utils.HeadDatabaseUtils;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
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
            Log.warn("Error parsing item: %s", e, content);
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
            ReadWriteNBT originalNBT = NBT.itemStackToNBT(itemStack); // Used later for merge.
            ReadWriteNBT modifiableNBT = NBT.itemStackToNBT(itemStack);
            modifiableNBT.getOrCreateCompound("tag")
                .mergeCompound(NBT.parseNBT(player == null ? nbt : PAPI.setPlaceholders(player, nbt)));
            try{
                /*
                 * DataFixerUtil has an issue where it expects to find "Count", due to expecting pre-1.20.5 NBT data,
                 * but since we used a 1.20.5+ ItemStack to create the NBT is there only "count", which causes
                 * DataFixerUtil to not find a valid NBT and does nothing.
                 * This addition fixes that issue.
                 */
                modifiableNBT.setByte("Count", (byte) 1);
                modifiableNBT = DataFixerUtil.fixUpItemData(modifiableNBT, DataFixerUtil.VERSION1_20_4, DataFixerUtil.getCurrentVersion());
                /*
                 * Updating the NBT removes the modern NBT variants of enchants and alike, as Datafixer discards them.
                 * So we have to manually merge them in again... Not pretty, but it does the job.
                 */
                modifiableNBT.mergeCompound(originalNBT);

                return NBT.itemStackFromNBT(modifiableNBT);
            } catch (NoSuchFieldException | IllegalAccessException ex) {
                Log.warn("Failed to apply NBT Data to Item: %s", ex, nbt);
                return itemStack;
            }
        } else {
            try {
                Bukkit.getUnsafe().modifyItemStack(itemStack, nbt);
            } catch (Exception ex) {
                Log.warn("Failed to apply NBT Data to Item: %s", ex, nbt);
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

        ReadWriteNBT nbtItem = NBT.itemStackToNBT(itemStack);
        int customModelData;
        if (Version.afterOrEqual(Version.v1_20_R4)) {
            // components contains item tags in 1.20.5+
            customModelData = nbtItem.getOrCreateCompound("components")
                .getInteger("minecraft:custom_model_data");
        } else {
            // 1.20.4 and older have CMD under "tag".
            customModelData = nbtItem.getOrCreateCompound("tag")
                .getInteger("CustomModelData");
        }

        if (customModelData > 0) {
            stringBuilder.append("{CustomModelData:").append(customModelData).append('}');
        }
        return new HologramItem(stringBuilder.toString());
    }

    public static ItemStack parseItemStack(String string, Player player) {
        string = PAPI.setPlaceholders(player, string);
        return new HologramItem(string).parse(player);
    }

}
