package eu.decentsoftware.holograms.nms.v1_16_R2;

import eu.decentsoftware.holograms.nms.api.DecentHologramsNmsException;
import eu.decentsoftware.holograms.shared.reflect.ReflectField;
import net.minecraft.server.v1_16_R2.Entity;

import java.util.concurrent.atomic.AtomicInteger;

class EntityIdGenerator {

    private static final ReflectField<AtomicInteger> ENTITY_COUNT_FIELD = new ReflectField<>(Entity.class, "entityCount");

    int getFreeEntityId() {
        try {
            /*
             * We are getting the new entity ids the same way as the server does. This is to ensure
             * that the ids are unique and don't conflict with any other entities.
             */
            AtomicInteger entityCount = ENTITY_COUNT_FIELD.get(null);
            return entityCount.incrementAndGet();
        } catch (Exception e) {
            throw new DecentHologramsNmsException("Failed to get new entity ID", e);
        }
    }

}
