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
    private static final EnumSet<EntityType> BUKKIT_ENTITY_TYPE_BLACKLIST;
    private static final EnumSet<EntityType> BUKKIT_ALLOWED_ENTITY_TYPES;

    static {
        ENTITY_TYPES = EnumSet.allOf(XEntityType.class);
        ENTITY_TYPE_BLACKLIST = Sets.newHashSet(
                XEntityType.AREA_EFFECT_CLOUD,
                XEntityType.BLOCK_DISPLAY,
                XEntityType.CHEST_MINECART,
                XEntityType.COMMAND_BLOCK_MINECART,
                XEntityType.DRAGON_FIREBALL,
                XEntityType.EVOKER_FANGS,
                XEntityType.EXPERIENCE_ORB,
                XEntityType.FALLING_BLOCK,
                XEntityType.FIREBALL,
                XEntityType.FIREWORK_ROCKET,
                XEntityType.FISHING_BOBBER,
                XEntityType.FURNACE_MINECART,
                XEntityType.GLOW_ITEM_FRAME,
                XEntityType.HOPPER_MINECART,
                XEntityType.INTERACTION,
                XEntityType.ITEM,
                XEntityType.ITEM_DISPLAY,
                XEntityType.ITEM_FRAME,
                XEntityType.LEASH_KNOT,
                XEntityType.LIGHTNING_BOLT,
                XEntityType.MARKER,
                XEntityType.MINECART,
                XEntityType.PAINTING,
                XEntityType.PLAYER,
                XEntityType.SMALL_FIREBALL,
                XEntityType.SPAWNER_MINECART,
                XEntityType.TEXT_DISPLAY,
                XEntityType.TNT_MINECART,
                XEntityType.TNT,
                XEntityType.UNKNOWN,
                XEntityType.WITHER_SKULL,
                // Boats and Chest Boats
                XEntityType.ACACIA_BOAT,
                XEntityType.ACACIA_CHEST_BOAT,
                XEntityType.BIRCH_BOAT,
                XEntityType.BIRCH_CHEST_BOAT,
                XEntityType.CHERRY_BOAT,
                XEntityType.CHERRY_CHEST_BOAT,
                XEntityType.DARK_OAK_BOAT,
                XEntityType.DARK_OAK_CHEST_BOAT,
                XEntityType.JUNGLE_BOAT,
                XEntityType.JUNGLE_CHEST_BOAT,
                XEntityType.MANGROVE_BOAT,
                XEntityType.MANGROVE_CHEST_BOAT,
                XEntityType.OAK_BOAT,
                XEntityType.OAK_CHEST_BOAT,
                XEntityType.PALE_OAK_BOAT,
                XEntityType.PALE_OAK_CHEST_BOAT,
                XEntityType.SPRUCE_BOAT,
                XEntityType.SPRUCE_CHEST_BOAT
        );

        BUKKIT_ENTITY_TYPE_BLACKLIST = EnumSet.copyOf(ENTITY_TYPE_BLACKLIST.stream()
                .map(XEntityType::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet()));
        BUKKIT_ALLOWED_ENTITY_TYPES = EnumSet.complementOf(BUKKIT_ENTITY_TYPE_BLACKLIST);

        ENTITY_TYPES.removeIf(ENTITY_TYPE_BLACKLIST::contains);

        for (XEntityType xEntityType : ENTITY_TYPES) {
            String name = getBukkitEntityTypeName(xEntityType);
            if (name != null) {
                ENTITY_TYPE_ALIASES.put(Common.removeSpacingChars(name).toLowerCase(), xEntityType);
            }
        }
    }

    public static List<String> getAllowedEntityTypeNames() {
        return BUKKIT_ALLOWED_ENTITY_TYPES.stream()
                .map(EntityType::name)
                .collect(Collectors.toList());
    }

    private static String getBukkitEntityTypeName(XEntityType xEntityType) {
        EntityType entityType = xEntityType.get();
        if (entityType != null) {
            return entityType.name();
        }
        return null;
    }

    private static boolean isAllowed(EntityType entityType) {
        return !BUKKIT_ENTITY_TYPE_BLACKLIST.contains(entityType);
    }

    @Nullable
    public static EntityType parseEntityType(String string) {
        if (string == null) {
            return null;
        }

        EntityType bukkitEntityType = getBukkitEntityTypeByName(string);
        if (bukkitEntityType == null) {
            bukkitEntityType = getBukkitEntityTypeByNameViaXEntityType(string);
        }

        if (bukkitEntityType != null && isAllowed(bukkitEntityType)) {
            return bukkitEntityType;
        }
        return null;
    }

    @Nullable
    private static EntityType getBukkitEntityTypeByNameViaXEntityType(String string) {
        XEntityType xEntityType = XEntityType.of(string).orElseGet(() -> getXEntityTypeFromAlias(string));
        if (xEntityType != null) {
            return xEntityType.get();
        }
        return null;
    }

    @Nullable
    private static EntityType getBukkitEntityTypeByName(String string) {
        EntityType bukkitEntityType;
        try {
            bukkitEntityType = EntityType.valueOf(string);
        } catch (IllegalArgumentException e) {
            bukkitEntityType = null;
        }
        return bukkitEntityType;
    }

    private static XEntityType getXEntityTypeFromAlias(String string) {
        return ENTITY_TYPE_ALIASES.get(Common.removeSpacingChars(string).toLowerCase());
    }
}
