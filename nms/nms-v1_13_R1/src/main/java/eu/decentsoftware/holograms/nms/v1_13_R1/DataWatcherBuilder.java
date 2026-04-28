package eu.decentsoftware.holograms.nms.v1_13_R1;

import net.minecraft.server.v1_13_R1.DataWatcher;

class DataWatcherBuilder {

    private final DataWatcher dataWatcher;

    private DataWatcherBuilder() {
        this.dataWatcher = new DataWatcher(null);
    }

    DataWatcher toDataWatcher() {
        return dataWatcher;
    }

    DataWatcherBuilder withInvisible() {
        /*
         * Entity Properties:
         * 0x01 - On Fire
         * 0x02 - Crouched
         * 0x08 - Sprinting
         * 0x10 - Eating/Drinking/Blocking
         * 0x20 - Invisible
         */

        EntityMetadataType.ENTITY_PROPERTIES.addToDataWatcher(dataWatcher, (byte) 0x20);
        return this;
    }

    DataWatcherBuilder withArmorStandProperties(boolean small, boolean marker) {
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

        EntityMetadataType.ARMOR_STAND_PROPERTIES.addToDataWatcher(dataWatcher, data);
        return this;
    }

    DataWatcherBuilder withNoGravity() {
        EntityMetadataType.ENTITY_HAS_NO_GRAVITY.addToDataWatcher(dataWatcher, true);
        return this;
    }

    static DataWatcherBuilder create() {
        return new DataWatcherBuilder();
    }

}
