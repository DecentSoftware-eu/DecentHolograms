package eu.decentsoftware.holograms.hook;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBTList;
import de.tr7zw.changeme.nbtapi.utils.DataFixerUtil;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
import lombok.Data;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    public static void initialize() {
        loadedSuccessfully = NBT.preloadApi();
        if (!loadedSuccessfully) {
            Log.warn("NBT-API could not be loaded. Custom NBT in items may not work as expected.");
        } else {
            Log.info("NBT-API loaded successfully.");
        }
    }

    public static ItemStack applyNbtDataToItemStack(ItemStack itemStack, String nbt, Player player) {
        if (!loadedSuccessfully) {
            return itemStack;
        }

        try {
            ReadWriteNBT originalNBT = NBT.itemStackToNBT(itemStack); // Used later for merge.
            ReadWriteNBT modifiableNBT = NBT.itemStackToNBT(itemStack);
            modifiableNBT.getOrCreateCompound("tag")
                    .mergeCompound(NBT.parseNBT(player == null ? nbt : PAPI.setPlaceholders(player, nbt)));
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

            // Load the nbt data
            ItemNbtData nbtData = ItemNbtData.fromJson(nbt);

            /*
             * Since item_model is new, it gets mapped under 'custom_data' in the item.
             * We need to manually re-apply it.
             */
            if (nbtData.getItemModel() != null) {
                modifiableNBT.getOrCreateCompound("components").setString("minecraft:item_model", nbtData.getItemModel());
            }

            return NBT.itemStackFromNBT(modifiableNBT);
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

    public static String extractItemModel(ItemStack itemStack) {
        if (!loadedSuccessfully) {
            return null;
        }

        return extractItemModel(NBT.itemStackToNBT(itemStack));
    }

    public static String extractItemModel(ReadWriteNBT nbtItem) {
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

    public static float extractCustomModelData(ItemStack itemStack) {
        if (!loadedSuccessfully) {
            return 0f;
        }

        return extractCustomModelData(NBT.itemStackToNBT(itemStack));
    }

    public static float extractCustomModelData(ReadWriteNBT nbtItem) {
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
