package eu.decentsoftware.holograms.nms.paper_v1_21_R6;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import org.bukkit.NamespacedKey;
import net.minecraft.world.entity.EntityType;

import java.util.Optional;

final class EntityTypeRegistry {

    private EntityTypeRegistry() {
        throw new IllegalStateException("Utility class");
    }

    static double getEntityTypeHeight(org.bukkit.entity.EntityType entityType) {
        return findEntityTypes(entityType).getDimensions().height();
    }

    static EntityType<?> findEntityTypes(org.bukkit.entity.EntityType entityType) {
        NamespacedKey namespacedKey = getNamespacedKey(entityType);
        String key = namespacedKey.getKey();
        Optional<EntityType<?>> entityTypes = EntityType.byString(key);
        if (entityTypes.isPresent()) {
            return entityTypes.get();
        }
        throw new DecentHologramsNmsException("Invalid entity type: " + entityType);
    }

    private static NamespacedKey getNamespacedKey(org.bukkit.entity.EntityType entityType) {
        try {
            // Using the deprecated #getKey method because #getKeyOrThrow and #getKeyOrNull don't exist on Paper.
            return entityType.getKey();
        } catch (IllegalStateException e) {
            throw new DecentHologramsNmsException("Couldn't get key for entity type: " + entityType);
        }
    }

}
