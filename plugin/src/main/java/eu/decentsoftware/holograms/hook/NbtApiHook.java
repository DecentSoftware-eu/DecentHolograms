package eu.decentsoftware.holograms.hook;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBTList;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class provides a utility wrapper for the NBT-API library, allowing safe access to NBT data manipulation
 * functionality for item modifications.
 * <p>
 * This ensures the NBT-API is loaded properly before any operations are attempted, preventing runtime errors
 * that could occur from using an uninitialized NBT-API.
 *
 * @author d0by
 * @since 2.9.2
 */
@UtilityClass
public class NbtApiHook {

    private static boolean loadedSuccessfully;
    private static ReflectMethod setCustomModelData;
    private static ReflectMethod setItemModel;

    public static void initialize() {
        loadedSuccessfully = NBT.preloadApi();
        if (!loadedSuccessfully) {
            Log.warn("NBT-API could not be loaded. Custom NBT in items may not work as expected.");
        } else {
            Log.info("NBT-API loaded successfully.");
        }

        // Set up bukkit API access
        if (Version.afterOrEqual(Version.v1_20_R4)) {
            setCustomModelData = new ReflectMethod(ItemMeta.class, "setCustomModelData", Integer.class);
            setItemModel = new ReflectMethod(ItemMeta.class, "setItemModel", NamespacedKey.class);
        }
    }

    // Applies NBT data to an item for versions 1.20.5+
    public static ItemStack applyNbtDataToItemStack(final ItemStack itemStack, ItemNbtData nbt) {
        if (!loadedSuccessfully) {
            return itemStack;
        }

        try {
            ItemStack toModify = itemStack.clone();
            // item_model was present in 1.21.2+
            ItemMeta meta = toModify.getItemMeta();
            if (nbt.getItemModel() != null && Version.afterOrEqual(Version.v1_21_R2)) {
                setItemModel.invoke(meta, namespacedKeyFromString(nbt.getItemModel(), null));
            }
            if (nbt.getCustomModelData() != 0f) {
                setCustomModelData.invoke(meta, ((Float) nbt.getCustomModelData()).intValue());
            }
            toModify.setItemMeta(meta);

            return toModify;
        } catch (Exception ex) {
            Log.warn("Failed to apply NBT Data to Item: %s", ex, nbt);
            return itemStack;
        }
    }

    public static ItemNbtData readData(ItemStack itemStack) {
        if (!loadedSuccessfully) {
            return ItemNbtData.EMPTY;
        }

        ReadWriteNBT nbt = NBT.itemStackToNBT(itemStack);
        return new ItemNbtData(extractItemModel(nbt), extractCustomModelData(nbt));
    }

    private static String extractItemModel(ReadWriteNBT nbtItem) {
        if (!loadedSuccessfully) {
            return null;
        }

        String itemModel;
        if (Version.afterOrEqual(Version.v1_21_R2)) {
            itemModel = nbtItem.getOrCreateCompound("components")
                    .getString("minecraft:item_model");
        } else {
            itemModel = null;
        }
        return itemModel;
    }

    private static float extractCustomModelData(ReadWriteNBT nbtItem) {
        if (!loadedSuccessfully) {
            return 0f;
        }

        float customModelData;
        if (Version.afterOrEqual(Version.v1_21_R3)) {
            // New structure components:{custom_model_data={floats[...]}} since 1.21.4
            ReadWriteNBTList<Float> floats = nbtItem.getOrCreateCompound("components")
                    .getOrCreateCompound("minecraft:custom_model_data")
                    .getFloatList("floats");

            customModelData = floats.isEmpty() ? 0.0F : floats.get(0);
        } else if (Version.afterOrEqual(Version.v1_20_R4)) {
            // components contains item tags in 1.20.5+
            customModelData = nbtItem.getOrCreateCompound("components")
                    .getInteger("minecraft:custom_model_data");
        } else {
            // 1.20.4 and older have CMD under "tag".
            customModelData = nbtItem.getOrCreateCompound("tag")
                    .getInteger("CustomModelData");
        }
        return customModelData;
    }

    // Taken from Bukkit 1.21 for backwards compatibility.
    @Nullable
    private static NamespacedKey namespacedKeyFromString(@NotNull String string, @Nullable Plugin defaultNamespace) {
        // Paper - Return null for empty string, check length
        Preconditions.checkArgument(string != null, "Input string must not be null");
        if (string.isEmpty() || string.length() > Short.MAX_VALUE) return null;
        // Paper end - Return null for empty string, check length

        String[] components = string.split(":", 3);
        if (components.length > 2) {
            return null;
        }

        String key = (components.length == 2) ? components[1] : "";
        if (components.length == 1) {
            String value = components[0];
            if (value.isEmpty() || !isValidKey(value)) {
                return null;
            }

            return (defaultNamespace != null) ? new NamespacedKey(defaultNamespace, value) : NamespacedKey.minecraft(value);
        } else if (components.length == 2 && !isValidKey(key)) {
            return null;
        }

        String namespace = components[0];
        if (namespace.isEmpty()) {
            return (defaultNamespace != null) ? new NamespacedKey(defaultNamespace, key) : NamespacedKey.minecraft(key);
        }

        if (!isValidNamespace(namespace)) {
            return null;
        }

        return new NamespacedKey(namespace, key);
    }

    private static boolean isValidNamespaceChar(char c) {
        return (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '_' || c == '-';
    }

    private static boolean isValidKeyChar(char c) {
        return isValidNamespaceChar(c) || c == '/';
    }

    private static boolean isValidNamespace(String namespace) {
        int len = namespace.length();
        if (len == 0) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (!isValidNamespaceChar(namespace.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    private static boolean isValidKey(String key) {
        int len = key.length();
        if (len == 0) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (!isValidKeyChar(key.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Represents the result of reading data from an item stack.
     * This is used to prevent redundancy with NBTAPI.
     */
    @Data
    public static class ItemNbtData {
        public static final ItemNbtData EMPTY = new ItemNbtData(null, 0f);

        private final String itemModel;
        private final float customModelData;
        private String json;

        public ItemNbtData(String itemModel, float customModelData) {
            this.itemModel = itemModel;
            this.customModelData = customModelData;
            this.json = toJson();
        }

        /**
         * Converts this result to json.
         *
         * @return the json.
         */
        private String toJson() {
            if ((this.itemModel == null || this.itemModel.isEmpty()) && this.customModelData == 0f)
                return "";

            // Use Gson to allow for easier expansion in the future.
            JsonObject object = new JsonObject();
            if (this.itemModel != null && !this.itemModel.isEmpty())
                object.addProperty("minecraft:item_model", this.itemModel);
            if (this.customModelData != 0f)
                object.addProperty("CustomModelData", this.customModelData);
            return object.toString();
        }

        public static ItemNbtData fromJson(String json) {
            if (json == null || json.isEmpty())
                return EMPTY;

            JsonObject object = new JsonParser().parse(json).getAsJsonObject();
            String itemModel = object.has("minecraft:item_model") ? object.get("minecraft:item_model").getAsString() : null;
            float customModelData = object.has("CustomModelData") ? object.get("CustomModelData").getAsFloat() : 0f;
            return new ItemNbtData(itemModel, customModelData);
        }
    }
}
