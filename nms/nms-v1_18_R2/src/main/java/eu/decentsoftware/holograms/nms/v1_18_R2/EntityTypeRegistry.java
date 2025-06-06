package eu.decentsoftware.holograms.nms.v1_18_R2;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import net.minecraft.core.IRegistry;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.entity.EntityType;

import java.util.Optional;

final class EntityTypeRegistry {

    private static final float SLIME_BASE_HEIGHT = 0.51000005f;

    private EntityTypeRegistry() {
        throw new IllegalStateException("Utility class");
    }

    static int getEntityTypeId(EntityType entityType) {
        return IRegistry.W.a(findEntityTypes(entityType));
    }

    static double getEntityTypeHeight(EntityType entityType) {
        if (entityType == EntityType.SLIME) {
            // Slime height is 2.04 in NMS, which is incorrect for a size 1 Slime
            return SLIME_BASE_HEIGHT;
        }
        return findEntityTypes(entityType).m().b;
    }

    static EntityTypes<?> findEntityTypes(EntityType entityType) {
        String key = entityType.getKey().getKey();
        Optional<EntityTypes<?>> entityTypes = EntityTypes.a(key);
        if (entityTypes.isPresent()) {
            return entityTypes.get();
        }
        throw new DecentHologramsNmsException("Invalid entity type: " + entityType);
    }

}
