package eu.decentsoftware.holograms.nms.v1_9_R1;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import net.minecraft.server.v1_9_R1.DataWatcher;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
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
        Optional<net.minecraft.server.v1_9_R1.ItemStack> itemStackOptional = Optional.fromNullable(
                CraftItemStack.asNMSCopy(itemStack));
        this.watchableObjects.add(EntityMetadataType.ITEM_STACK.construct(itemStackOptional));
        return this;
    }

    EntityMetadataBuilder withSilent() {
        this.watchableObjects.add(EntityMetadataType.ENTITY_SILENT.construct(true));
        return this;
    }

    static EntityMetadataBuilder create() {
        return new EntityMetadataBuilder();
    }

}
