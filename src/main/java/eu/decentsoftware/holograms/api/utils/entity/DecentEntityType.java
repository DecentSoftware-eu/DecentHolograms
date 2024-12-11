package eu.decentsoftware.holograms.api.utils.entity;

import com.cryptomorin.xseries.XEntityType;
import com.google.common.collect.Sets;
import eu.decentsoftware.holograms.api.utils.Common;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.EntityType;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@UtilityClass
public final class DecentEntityType {

    private static final Map<String, XEntityType> ENTITY_TYPE_ALIASES = new HashMap<>();
    private static final Set<XEntityType> ENTITY_TYPE_BLACKLIST;
    private static final Set<XEntityType> ENTITY_TYPES;

    static {
        ENTITY_TYPES = EnumSet.allOf(XEntityType.class);
        ENTITY_TYPE_BLACKLIST = Sets.newHashSet(
                XEntityType.AREA_EFFECT_CLOUD,
                XEntityType.BLOCK_DISPLAY,
                XEntityType.EVOKER_FANGS,
                XEntityType.FALLING_BLOCK,
                XEntityType.FISHING_BOBBER,
                XEntityType.GLOW_ITEM_FRAME,
                XEntityType.INTERACTION,
                XEntityType.ITEM,
                XEntityType.ITEM_DISPLAY,
                XEntityType.ITEM_FRAME,
                XEntityType.LEASH_KNOT,
                XEntityType.LIGHTNING_BOLT,
                XEntityType.MARKER,
                XEntityType.PAINTING,
                XEntityType.PLAYER,
                XEntityType.TEXT_DISPLAY,
                XEntityType.TNT,
                XEntityType.UNKNOWN
        );

        ENTITY_TYPES.removeIf(ENTITY_TYPE_BLACKLIST::contains);

        for (XEntityType xEntityType : ENTITY_TYPES) {
            String name = getBukkitEntityTypeName(xEntityType);
            if (name != null) {
                ENTITY_TYPE_ALIASES.put(Common.removeSpacingChars(name).toLowerCase(), xEntityType);
            }
        }
    }

    public static List<String> getAllowedEntityTypeNames() {
        return ENTITY_TYPES.stream()
                .map(DecentEntityType::getBukkitEntityTypeName)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static String getBukkitEntityTypeName(XEntityType xEntityType) {
        EntityType entityType = xEntityType.get();
        if (entityType != null) {
            return entityType.name();
        }
        return null;
    }

    public static boolean isAllowed(EntityType entityType) {
        return isAllowed(XEntityType.of(entityType));
    }

    public static boolean isAllowed(XEntityType entityType) {
        return !ENTITY_TYPE_BLACKLIST.contains(entityType);
    }

    @Nullable
    public static EntityType parseEntityType(String string) {
        if (string == null) {
            return null;
        }
        XEntityType entityType = ENTITY_TYPE_ALIASES.get(Common.removeSpacingChars(string).toLowerCase());
        if (entityType == null) {
            entityType = XEntityType.of(string).orElse(null);
        }
        if (entityType != null && isAllowed(entityType)) {
            return entityType.get();
        }
        return null;
    }

}
