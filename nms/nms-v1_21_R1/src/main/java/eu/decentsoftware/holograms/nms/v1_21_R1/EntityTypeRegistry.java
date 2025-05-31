package eu.decentsoftware.holograms.nms.v1_21_R1;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import net.minecraft.world.entity.EntityTypes;
import org.bukkit.entity.EntityType;

import java.util.Optional;

final class EntityTypeRegistry {

    private EntityTypeRegistry() {
        throw new IllegalStateException("Utility class");
    }

    static double getEntityTypeHeight(EntityType entityType) {
        return findEntityTypes(entityType).n().b();
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
