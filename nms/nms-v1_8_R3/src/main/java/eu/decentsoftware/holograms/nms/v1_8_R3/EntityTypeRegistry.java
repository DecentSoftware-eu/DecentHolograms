package eu.decentsoftware.holograms.nms.v1_8_R3;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import org.bukkit.entity.EntityType;

import java.util.EnumMap;
import java.util.Map;

final class EntityTypeRegistry {

    private static final Map<EntityType, EntityTypeInfo> ENTITY_TYPE_INFO_MAP = new EnumMap<>(EntityType.class);

    static {
        // Living Entities:
        register(EntityType.ARMOR_STAND, 30, 2.0f);
        register(EntityType.CREEPER, 50, 1.8f);
        register(EntityType.SKELETON, 51, 1.95f);
        register(EntityType.SPIDER, 52, 0.9f);
        register(EntityType.GIANT, 53, 1.8f * 6);
        register(EntityType.ZOMBIE, 54, 1.8f);
        register(EntityType.SLIME, 55, 0.51000005f);
        register(EntityType.GHAST, 56, 4.0f);
        register(EntityType.PIG_ZOMBIE, 57, 1.8f);
        register(EntityType.ENDERMAN, 58, 2.9f);
        register(EntityType.CAVE_SPIDER, 59, 0.5f);
        register(EntityType.SILVERFISH, 60, 0.3f);
        register(EntityType.BLAZE, 61, 1.8f);
        register(EntityType.MAGMA_CUBE, 62, 0.51000005f);
        register(EntityType.ENDER_DRAGON, 63, 8.0f);
        register(EntityType.WITHER, 64, 3.5f);
        register(EntityType.BAT, 65, 0.9f);
        register(EntityType.WITCH, 66, 1.8f);
        register(EntityType.ENDERMITE, 67, 0.3f);
        register(EntityType.GUARDIAN, 68, 0.85f);
        register(EntityType.PIG, 90, 0.9f);
        register(EntityType.SHEEP, 91, 1.3f);
        register(EntityType.COW, 92, 1.3f);
        register(EntityType.CHICKEN, 93, 0.7f);
        register(EntityType.SQUID, 94, 0.95f);
        register(EntityType.WOLF, 95, 0.8f);
        register(EntityType.MUSHROOM_COW, 96, 1.3f);
        register(EntityType.SNOWMAN, 97, 1.9f);
        register(EntityType.OCELOT, 98, 0.8f);
        register(EntityType.IRON_GOLEM, 99, 2.9f);
        register(EntityType.HORSE, 100, 1.6f);
        register(EntityType.RABBIT, 101, 0.7f);
        register(EntityType.VILLAGER, 120, 1.8f);
        // Objects:
        register(EntityType.BOAT, 1, 0.6f);
        register(EntityType.DROPPED_ITEM, 2, 0.25f);
        register(EntityType.MINECART, 10, 0.7f);
        register(EntityType.PRIMED_TNT, 50, 0.98f);
        register(EntityType.ENDER_CRYSTAL, 51, 2.0f);
        register(EntityType.ARROW, 60, 0.5f);
        register(EntityType.SNOWBALL, 61, 0.25f);
        register(EntityType.EGG, 62, 0.25f);
        register(EntityType.FALLING_BLOCK, 70, 0.98f);
        register(EntityType.FIREBALL, 63, 1.0f);
        register(EntityType.SMALL_FIREBALL, 64, 0.3125f);
        register(EntityType.ENDER_PEARL, 65, 0.25f);
        register(EntityType.WITHER_SKULL, 66, 0.3125f);
        register(EntityType.ITEM_FRAME, 71, 0.98f);
        register(EntityType.ENDER_SIGNAL, 72, 0.25f);
        register(EntityType.THROWN_EXP_BOTTLE, 75, 0.25f);
        register(EntityType.FIREWORK, 76, 0.25f);
        register(EntityType.LEASH_HITCH, 77, 0.0f);
    }

    private EntityTypeRegistry() {
        throw new IllegalStateException("Utility class");
    }

    private static void register(EntityType entityType, int typeId, double height) {
        ENTITY_TYPE_INFO_MAP.put(entityType, new EntityTypeInfo(typeId, height));
    }

    static int getEntityTypeId(EntityType entityType) {
        EntityTypeInfo entityTypeInfo = ENTITY_TYPE_INFO_MAP.get(entityType);
        if (entityTypeInfo == null) {
            throw new DecentHologramsNmsException("Invalid entity type: " + entityType);
        }
        return entityTypeInfo.getTypeId();
    }

    static double getEntityTypeHeight(EntityType entityType) {
        EntityTypeInfo entityTypeInfo = ENTITY_TYPE_INFO_MAP.get(entityType);
        if (entityTypeInfo == null) {
            throw new DecentHologramsNmsException("Invalid entity type: " + entityType);
        }
        return entityTypeInfo.getHeight();
    }

    private static class EntityTypeInfo {

        private final int typeId;
        private final double height;

        EntityTypeInfo(int typeId, double height) {
            this.typeId = typeId;
            this.height = height;
        }

        int getTypeId() {
            return typeId;
        }

        double getHeight() {
            return height;
        }

    }

}
