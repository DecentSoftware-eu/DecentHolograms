package eu.decentsoftware.holograms.api.utils.entity;

import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.utils.Common;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class DecentEntityType {

    private static final Map<String, EntityType> ENTITY_TYPE_ALIASES = new HashMap<>();
    private static final Set<EntityType> ENTITY_TYPE_BLACKLIST;
    private static final Set<EntityType> ENTITY_TYPES;

    static {
        ENTITY_TYPES = EnumSet.allOf(EntityType.class);

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
                ).stream()
                .map((name) -> {
                    try {
                        return EntityType.valueOf(name);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        ENTITY_TYPES.removeIf(ENTITY_TYPE_BLACKLIST::contains);

        for (EntityType entityType : ENTITY_TYPES) {
            ENTITY_TYPE_ALIASES.put(Common.removeSpacingChars(entityType.name()).toLowerCase(), entityType);
        }
    }

    public static List<EntityType> getAllowedEntityTypes() {
        return new ArrayList<>(ENTITY_TYPES);
    }

    public static List<String> getAllowedEntityTypeNames() {
        return ENTITY_TYPES.stream()
                .map(EntityType::name)
                .collect(Collectors.toList());
    }

    public static boolean isAllowed(EntityType entityType) {
        return !ENTITY_TYPE_BLACKLIST.contains(entityType);
    }

    @Nullable
    public static EntityType parseEntityType(String string) {
        EntityType entityType = ENTITY_TYPE_ALIASES.get(Common.removeSpacingChars(string).toLowerCase());
        if (entityType != null && isAllowed(entityType)) {
            return entityType;
        }
        return null;
    }

}
