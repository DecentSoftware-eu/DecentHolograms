package eu.decentsoftware.holograms.nms.v1_21_R5;

import com.google.common.base.Strings;
import net.minecraft.network.chat.IChatBaseComponent;
import net.minecraft.network.syncher.DataWatcher;
import org.bukkit.craftbukkit.v1_21_R5.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_21_R5.util.CraftChatMessage;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class EntityMetadataBuilder {

    private final List<DataWatcher.Item<?>> watchableObjects;

    private EntityMetadataBuilder() {
        this.watchableObjects = new ArrayList<>();
    }

    List<DataWatcher.Item<?>> toWatchableObjects() {
        return watchableObjects;
    }

    EntityMetadataBuilder withInvisible() {
        /*
         * Entity Properties:
         * 0x01 - On Fire
         * 0x02 - Crouched
         * 0x08 - Sprinting
         * 0x10 - Swimming
         * 0x20 - Invisible
         * 0x40 - Has glowing effect
         * 0x80 - If flying with an elytra
         */

        watchableObjects.add(EntityMetadataType.ENTITY_PROPERTIES.construct((byte) 0x20));
        return this;
    }

    EntityMetadataBuilder withArmorStandProperties(boolean small, boolean marker) {
        /*
         * Armor Stand Properties:
         * 0x01 - Small
         * 0x02 - Unused
         * 0x04 - Has Arms
         * 0x08 - Remove Baseplate
         * 0x10 - Marker (Zero bounding box)
         */

        byte data = 0x08; // Always Remove Baseplate
        if (small) {
            data |= 0x01;
        }
        if (marker) {
            data |= 0x10;
        }

        watchableObjects.add(EntityMetadataType.ARMOR_STAND_PROPERTIES.construct(data));
        return this;
    }

    EntityMetadataBuilder withCustomName(String customName) {
        IChatBaseComponent iChatBaseComponent = CraftChatMessage.fromStringOrNull(customName);
        Optional<IChatBaseComponent> optionalIChatBaseComponent = Optional.ofNullable(iChatBaseComponent);
        watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME.construct(optionalIChatBaseComponent));
        boolean visible = !Strings.isNullOrEmpty(customName);
        watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME_VISIBLE.construct(visible));
        return this;
    }

    EntityMetadataBuilder withItemStack(ItemStack itemStack) {
        watchableObjects.add(EntityMetadataType.ITEM_STACK.construct(CraftItemStack.asNMSCopy(itemStack)));
        return this;
    }

    EntityMetadataBuilder withSilent() {
        watchableObjects.add(EntityMetadataType.ENTITY_SILENT.construct(true));
        return this;
    }

    EntityMetadataBuilder withNoGravity() {
        watchableObjects.add(EntityMetadataType.ENTITY_HAS_NO_GRAVITY.construct(true));
        return this;
    }

    static EntityMetadataBuilder create() {
        return new EntityMetadataBuilder();
    }

}
