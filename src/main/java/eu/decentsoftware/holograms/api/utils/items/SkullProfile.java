package eu.decentsoftware.holograms.api.utils.items;

import com.mojang.authlib.GameProfile;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

class SkullProfile {
    public static Constructor<?> gameProfileField;

    static {
        try {
            Class<?> resolvableProfileClass = Class.forName("net.minecraft.world.item.component.ResolvableProfile");
            Class<?> gameProfileClass = Class.forName("com.mojang.authlib.GameProfile");

            Constructor<?> constructor = resolvableProfileClass.getDeclaredConstructor(gameProfileClass);
            constructor.setAccessible(true);
            gameProfileField = constructor;

        } catch (Exception e) {
            e.printStackTrace();
            gameProfileField = null;
        }
    }

    public static Object getObject(GameProfile profile) {
        try {
            return gameProfileField.newInstance(profile);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
