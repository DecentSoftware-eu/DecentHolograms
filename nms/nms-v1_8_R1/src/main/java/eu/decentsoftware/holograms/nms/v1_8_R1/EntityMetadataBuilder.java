package eu.decentsoftware.holograms.nms.v1_8_R1;

import com.google.common.base.Strings;
import net.minecraft.server.v1_8_R1.WatchableObject;
import org.bukkit.craftbukkit.v1_8_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

class EntityMetadataBuilder {

    private final List<WatchableObject> watchableObjects;

    private EntityMetadataBuilder() {
        this.watchableObjects = new ArrayList<>();
    }

    List<WatchableObject> toWatchableObjects() {
        return this.watchableObjects;
    }

    EntityMetadataBuilder withInvisible() {
        /*
         * Entity Properties:
         * 0x01 - On Fire
         * 0x02 - Crouched
         * 0x08 - Sprinting
         * 0x10 - Eating/Drinking/Blocking
         * 0x20 - Invisible
         */

        this.watchableObjects.add(EntityMetadataType.ENTITY_PROPERTIES.construct((byte) 0x20));
        return this;
    }

    EntityMetadataBuilder withArmorStandProperties(boolean small, boolean marker) {
        /*
         * Armor Stand Properties:
         * 0x01 - Small
         * 0x02 - Has Gravity
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

        this.watchableObjects.add(EntityMetadataType.ARMOR_STAND_PROPERTIES.construct(data));
        return this;
    }

    EntityMetadataBuilder withCustomName(String customName) {
        this.watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME.construct(customName));
        boolean visible = !Strings.isNullOrEmpty(customName);
        this.watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME_VISIBLE.construct(booleanToByte(visible)));
        return this;
    }

    EntityMetadataBuilder withItemStack(ItemStack itemStack) {
        this.watchableObjects.add(EntityMetadataType.ITEM_STACK.construct(CraftItemStack.asNMSCopy(itemStack)));
        return this;
    }

    private byte booleanToByte(boolean visible) {
        return (byte) (visible ? 1 : 0);
    }

    static EntityMetadataBuilder create() {
        return new EntityMetadataBuilder();
    }

}
