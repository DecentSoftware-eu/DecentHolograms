package eu.decentsoftware.holograms.nms.v26_2;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.EntityType;

import java.util.Optional;

final class EntityTypeRegistry {

    private EntityTypeRegistry() {
        throw new IllegalStateException("Utility class");
    }

    static double getEntityTypeHeight(EntityType entityType) {
        return findEntityTypes(entityType).getHeight();
    }

    static net.minecraft.world.entity.EntityType<?> findEntityTypes(EntityType entityType) {
        NamespacedKey namespacedKey = getNamespacedKey(entityType);
        String key = namespacedKey.getKey();
        Optional<net.minecraft.world.entity.EntityType<?>> entityTypes = BuiltInRegistries.ENTITY_TYPE.getOptional(Identifier.tryParse(key));
        if (entityTypes.isPresent()) {
            return entityTypes.get();
        }
        throw new DecentHologramsNmsException("Invalid entity type: " + entityType);
    }

    private static NamespacedKey getNamespacedKey(EntityType entityType) {
        try {
            // Using the deprecated #getKey method because #getKeyOrThrow and #getKeyOrNull don't exist on Paper.
            return entityType.getKey();
        } catch (IllegalStateException _) {
            throw new DecentHologramsNmsException("Couldn't get key for entity type: " + entityType);
        }
    }
}
