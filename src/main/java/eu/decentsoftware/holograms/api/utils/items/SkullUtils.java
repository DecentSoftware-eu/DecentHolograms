package eu.decentsoftware.holograms.api.utils.items;

import com.google.common.collect.ForwardingMultimap;
import eu.decentsoftware.holograms.api.utils.reflect.*;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

@UtilityClass
public final class SkullUtils {

    /*
     * TODO:
     *  - Get/Set owner
     *  - Get textures of owners from the mojang api
     */

    private static boolean initialized = false;
    private static ReflectField<?> PROFILE_FIELD;
    private static ReflectMethod PROFILE_GET_PROPERTIES_METHOD;
    private static ReflectConstructor PROFILE_CONSTRUCTOR;
    private static ReflectMethod PROPERTY_GET_VALUE_METHOD;
    private static ReflectConstructor PROPERTY_CONSTRUCTOR;

    private static void init() {
        Class<?> craftMetaSkullClass = ReflectionUtil.getObcClass("inventory.CraftMetaSkull");
        Class<?> gameProfileClass = ReflectionUtil.getClass("com.mojang.authlib.GameProfile");
        Class<?> propertyClass = ReflectionUtil.getClass("com.mojang.authlib.properties.Property");
        PROFILE_FIELD = new ReflectField<>(craftMetaSkullClass, "profile");
        PROFILE_GET_PROPERTIES_METHOD = new ReflectMethod(gameProfileClass, "getProperties");
        PROFILE_CONSTRUCTOR = new ReflectConstructor(gameProfileClass, UUID.class, String.class);
        PROPERTY_GET_VALUE_METHOD = new ReflectMethod(propertyClass, "getValue");
        PROPERTY_CONSTRUCTOR = new ReflectConstructor(propertyClass, String.class, String.class);
        initialized = true;
    }

    @Nullable
    public static String getTexture(@NonNull ItemStack itemStack) {
        try {
            if (!initialized) {
                init();
            }

            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            if (meta == null) {
                return null;
            }

            Object profile = PROFILE_FIELD.getValue(meta);
            if (profile == null) {
                return null;
            }

            ForwardingMultimap<String, Object> properties = PROFILE_GET_PROPERTIES_METHOD.invoke(profile);
            Object property = properties.get("textures");
            if (property != null) {
                return PROPERTY_GET_VALUE_METHOD.invoke(property);
            }
        } catch (ClassCastException ignored) {
        }
        return null;
    }

    public static void setSkullTexture(@NonNull ItemStack itemStack, @NonNull String texture) {
        try {
            if (!initialized) {
                init();
            }

            SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
            if (meta != null) {
                Object profile = PROFILE_CONSTRUCTOR.newInstance(UUID.randomUUID(), null);
                Object property = PROPERTY_CONSTRUCTOR.newInstance("textures", texture);

                ForwardingMultimap<String, Object> properties = PROFILE_GET_PROPERTIES_METHOD.invoke(profile);
                properties.put("textures", property);

                PROFILE_FIELD.setValue(meta, profile);
            }
            itemStack.setItemMeta(meta);

            if (Version.before(13)) {
                // noinspection deprecation
                itemStack.setDurability((short) SkullType.PLAYER.ordinal());
            }
        } catch (ClassCastException ignored) {
        }
    }

}
