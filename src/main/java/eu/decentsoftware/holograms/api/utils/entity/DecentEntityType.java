package eu.decentsoftware.holograms.api.utils.entity;

import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.utils.Common;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class DecentEntityType {

    private static final Map<String, EntityType> ENTITY_TYPE_ALIASES = new HashMap<>();
    private static final Set<String> ENTITY_TYPE_BLACKLIST;
    private static final EnumSet<EntityType> ENTITY_TYPES = EnumSet.allOf(EntityType.class);

    static {
        for (EntityType entityType : ENTITY_TYPES) {
            ENTITY_TYPE_ALIASES.put(Common.removeSpacingChars(entityType.name()).toLowerCase(), entityType);
        }

        ENTITY_TYPE_BLACKLIST = Sets.newHashSet(
                "ARMOR_STAND",
                "PRIMED_TNT",
                "AREA_EFFECT_CLOUD",
                "FISHING_HOOK",
                "GIANT",
                "EVOKER_FANGS",
                "EXPERIENCE_ORB",
                "LEASH_HITCH",
                "DROPPED_ITEM",
                "ITEM_FRAME",
                "GLOW_ITEM_FRAME",
                "THROWN_EXP_BOTTLE",
                "SPLASH_POTION",
                "SPECTRAL_ARROW",
                "LLAMA_SPIT",
                "MARKER",
                "LIGHTNING",
                "TRIDENT",
                "PAINTING",
                "PLAYER",
                "MINECART",
                "MINECART_CHEST",
                "MINECART_COMMAND",
                "MINECART_FURNACE",
                "MINECART_HOPPER",
                "MINECART_MOB_SPAWNER",
                "MINECART_TNT",
                "COMPLEX_PART",
                "WEATHER",
                "TIPPED_ARROW",
                "UNKNOWN"
        );
    }

    public static List<EntityType> getAllowedEntityTypes() {
        return ENTITY_TYPES.stream()
            .filter(DecentEntityType::isAllowed)
            .collect(Collectors.toList());
    }

    public static List<String> getAllowedEntityTypeNames() {
        return ENTITY_TYPES.stream()
            .filter(DecentEntityType::isAllowed)
            .map(EntityType::name)
            .collect(Collectors.toList());
    }

    public static boolean isAllowed(EntityType entityType) {
        return !ENTITY_TYPE_BLACKLIST.contains(entityType.name());
    }

    @Nullable
    public static EntityType parseEntityType(String string) {
        EntityType entityType = ENTITY_TYPE_ALIASES.get(Common.removeSpacingChars(string).toLowerCase());
        if (entityType == null) return null;
        if (isAllowed(entityType)) {
            return null;
        }
        return entityType;
    }

}
