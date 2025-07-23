package eu.decentsoftware.holograms.api.actions;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Map;

public abstract class ActionType {

    /*
     * Cache
     */

    private static final Map<String, ActionType> VALUES = Maps.newHashMap();

    public static ActionType getByName(String name) {
        return VALUES.get(name.toUpperCase());
    }


    /*
     * Abstract Methods
     */

    @Getter
    private final String name;

    protected ActionType(@NonNull String name) {
        name = name.toUpperCase();
        if (VALUES.containsKey(name)) {
            throw new IllegalArgumentException("ActionType " + name + " already exists!");
        }
        this.name = name;
        VALUES.put(this.name, this);
    }

    public abstract boolean execute(Player player, String... args);

}
