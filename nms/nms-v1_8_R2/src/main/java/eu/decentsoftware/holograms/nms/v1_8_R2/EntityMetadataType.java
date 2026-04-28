package eu.decentsoftware.holograms.nms.v1_8_R2;

import net.minecraft.server.v1_8_R2.DataWatcher;
import net.minecraft.server.v1_8_R2.ItemStack;

class EntityMetadataType<T> {

    static final EntityMetadataType<Byte> ENTITY_PROPERTIES = new EntityMetadataType<>(DataType.BYTE, 0);
    static final EntityMetadataType<String> ENTITY_CUSTOM_NAME = new EntityMetadataType<>(DataType.STRING, 2);
    static final EntityMetadataType<Byte> ENTITY_CUSTOM_NAME_VISIBLE = new EntityMetadataType<>(DataType.BYTE, 3);
    static final EntityMetadataType<Byte> ARMOR_STAND_PROPERTIES = new EntityMetadataType<>(DataType.BYTE, 10);
    static final EntityMetadataType<ItemStack> ITEM_STACK = new EntityMetadataType<>(DataType.ITEM_STACK, 10);

    private final int dataType;
    private final int index;

    private EntityMetadataType(int dataType, int index) {
        this.dataType = dataType;
        this.index = index;
    }

    DataWatcher.WatchableObject construct(T value) {
        return new DataWatcher.WatchableObject(dataType, index, value);
    }

    void addToDataWatcher(DataWatcher dataWatcher, T value) {
        dataWatcher.a(index, value);
    }

    private static class DataType {

        static final int BYTE = 0;
        static final int STRING = 4;
        static final int ITEM_STACK = 5;

        private DataType() {
            throw new IllegalStateException("Utility class");
        }

    }

}
