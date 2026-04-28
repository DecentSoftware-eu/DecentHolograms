package eu.decentsoftware.holograms.nms.v1_11_R1;

import com.google.common.base.Strings;
import net.minecraft.server.v1_11_R1.DataWatcher;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

class EntityMetadataBuilder {

    private final List<DataWatcher.Item<?>> watchableObjects;

    private EntityMetadataBuilder() {
        this.watchableObjects = new ArrayList<>();
    }

    List<DataWatcher.Item<?>> toWatchableObjects() {
        return this.watchableObjects;
    }

    EntityMetadataBuilder withCustomName(String customName) {
        this.watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME.construct(customName));
        boolean visible = !Strings.isNullOrEmpty(customName);
        this.watchableObjects.add(EntityMetadataType.ENTITY_CUSTOM_NAME_VISIBLE.construct(visible));
        return this;
    }

    EntityMetadataBuilder withItemStack(ItemStack itemStack) {
        this.watchableObjects.add(EntityMetadataType.ITEM_STACK.construct(CraftItemStack.asNMSCopy(itemStack)));
        return this;
    }

    EntityMetadataBuilder withSilent() {
        this.watchableObjects.add(EntityMetadataType.ENTITY_SILENT.construct(true));
        return this;
    }

    EntityMetadataBuilder withNoGravity() {
        this.watchableObjects.add(EntityMetadataType.ENTITY_HAS_NO_GRAVITY.construct(true));
        return this;
    }

    static EntityMetadataBuilder create() {
        return new EntityMetadataBuilder();
    }

}
