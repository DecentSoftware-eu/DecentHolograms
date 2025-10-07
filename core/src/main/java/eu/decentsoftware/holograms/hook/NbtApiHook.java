package eu.decentsoftware.holograms.hook;

import de.tr7zw.changeme.nbtapi.NBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBT;
import de.tr7zw.changeme.nbtapi.iface.ReadWriteNBTList;
import de.tr7zw.changeme.nbtapi.utils.DataFixerUtil;
import eu.decentsoftware.holograms.api.utils.Log;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.reflect.Version;
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

            return NBT.itemStackFromNBT(modifiableNBT);
        } catch (Exception ex) {
            Log.warn("Failed to apply NBT Data to Item: %s", ex, nbt);
            return itemStack;
        }
    }

    public static float extractCustomModelData(ItemStack itemStack) {
        if (!loadedSuccessfully) {
            return 0f;
        }

        ReadWriteNBT nbtItem = NBT.itemStackToNBT(itemStack);
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
}
