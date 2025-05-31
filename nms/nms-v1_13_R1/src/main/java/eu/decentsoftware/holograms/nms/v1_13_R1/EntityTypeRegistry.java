package eu.decentsoftware.holograms.nms.v1_13_R1;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import org.bukkit.entity.EntityType;

import java.util.EnumMap;
import java.util.Map;

final class EntityTypeRegistry {

    private static final Map<EntityType, EntityTypeInfo> ENTITY_TYPE_INFO_MAP = new EnumMap<>(EntityType.class);

    static {
        // Living Entities:
        register(EntityType.ARMOR_STAND, 1, 2.0f);
        register(EntityType.BAT, 3, 0.9f);
        register(EntityType.BLAZE, 4, 1.8f);
        register(EntityType.CAVE_SPIDER, 6, 0.5f);
        register(EntityType.CHICKEN, 7, 0.7f);
        register(EntityType.COD, 8, 0.3f);
        register(EntityType.COW, 9, 1.4f);
        register(EntityType.CREEPER, 10, 1.7f);
        register(EntityType.DONKEY, 11, 1.6f);
        register(EntityType.DOLPHIN, 12, 0.6f);
        register(EntityType.DROWNED, 14, 1.95f);
        register(EntityType.ELDER_GUARDIAN, 15, 0.85f * 2.35f);
        register(EntityType.ENDER_DRAGON, 17, 8.0f);
        register(EntityType.ENDERMAN, 18, 2.9f);
        register(EntityType.ENDERMITE, 19, 0.3f);
        register(EntityType.EVOKER, 21, 1.95F);
        register(EntityType.GHAST, 26, 4.0f);
        register(EntityType.GIANT, 27, 1.8f * 6);
        register(EntityType.HORSE, 28, 1.6f);
        register(EntityType.HUSK, 30, 1.95f);
        register(EntityType.ILLUSIONER, 31, 1.95F);
        register(EntityType.LLAMA, 36, 1.87f);
        register(EntityType.MAGMA_CUBE, 38, 0.51000005f);
        register(EntityType.MULE, 46, 1.6f);
        register(EntityType.MUSHROOM_COW, 47, 1.4f);
        register(EntityType.OCELOT, 48, 0.7f);
        register(EntityType.PARROT, 50, 0.9f);
        register(EntityType.PIG, 51, 0.9f);
        register(EntityType.PUFFERFISH, 52, 0.7f);
        register(EntityType.PIG_ZOMBIE, 53, 1.95f);
        register(EntityType.POLAR_BEAR, 54, 1.4f);
        register(EntityType.RABBIT, 56, 0.5f);
        register(EntityType.SALMON, 57, 0.4f);
        register(EntityType.SHEEP, 58, 1.3f);
        register(EntityType.SHULKER, 59, 1.0f);
        register(EntityType.SILVERFISH, 61, 0.3f);
        register(EntityType.SKELETON, 62, 1.99f);
        register(EntityType.SKELETON_HORSE, 63, 1.6f);
        register(EntityType.SLIME, 64, 0.51000005f);
        register(EntityType.SNOWMAN, 66, 1.9f);
        register(EntityType.GUARDIAN, 68, 0.85f);
        register(EntityType.SPIDER, 69, 0.9f);
        register(EntityType.SQUID, 70, 0.8f);
        register(EntityType.STRAY, 71, 1.99f);
        register(EntityType.TROPICAL_FISH, 72, 0.4f);
        register(EntityType.TURTLE, 73, 0.4f);
        register(EntityType.VEX, 78, 0.8F);
        register(EntityType.VILLAGER, 79, 1.95f);
        register(EntityType.IRON_GOLEM, 80, 2.7f);
        register(EntityType.VINDICATOR, 81, 1.95F);
        register(EntityType.WITCH, 82, 1.95f);
        register(EntityType.WITHER, 83, 3.5f);
        register(EntityType.WITHER_SKELETON, 84, 2.4f);
        register(EntityType.WOLF, 86, 0.85f);
        register(EntityType.ZOMBIE, 87, 1.95f);
        register(EntityType.ZOMBIE_HORSE, 88, 1.6f);
        register(EntityType.ZOMBIE_VILLAGER, 89, 1.95f);
        register(EntityType.PHANTOM, 90, 0.5f);
        // Objects:
        register(EntityType.BOAT, 1, 0.5625f);
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
        register(EntityType.SHULKER_BULLET, 67, 0.3125f);
        register(EntityType.LLAMA_SPIT, 68, 0.25f);
        register(EntityType.ITEM_FRAME, 71, 0.98f);
        register(EntityType.ENDER_SIGNAL, 72, 0.25f);
        register(EntityType.SPLASH_POTION, 73, 0.25f);
        register(EntityType.THROWN_EXP_BOTTLE, 75, 0.25f);
        register(EntityType.FIREWORK, 76, 0.25f);
        register(EntityType.LEASH_HITCH, 77, 0.5f);
        register(EntityType.EVOKER_FANGS, 79, 0.8f);
        register(EntityType.FISHING_HOOK, 90, 0.25f);
        register(EntityType.SPECTRAL_ARROW, 91, 0.5f);
        register(EntityType.TIPPED_ARROW, 92, 0.5f);
        register(EntityType.DRAGON_FIREBALL, 93, 1.0f);
        register(EntityType.TRIDENT, 94, 0.5f);
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
