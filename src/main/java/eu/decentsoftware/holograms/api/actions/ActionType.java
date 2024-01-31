package eu.decentsoftware.holograms.api.actions;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.commands.CommandValidator;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

public abstract class ActionType {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();

    /*
     * Cache
     */

    private static final Map<String, ActionType> VALUES = Maps.newHashMap();

    public static ActionType getByName(String name) {
        return VALUES.get(name.toUpperCase());
    }

    public static Collection<ActionType> getActionTypes() {
        return VALUES.values();
    }

    /*
     * Actions
     */

    public static final ActionType NONE = new ActionType("NONE") {
        @Override
        public boolean execute(Player player, String... args) {
            return true;
        }
    };

    public static final ActionType MESSAGE = new ActionType("MESSAGE") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);

            String string = String.join(" ", args);
            Common.tell(player, PAPI.setPlaceholders(player, string.replace("{player}", player.getName())));
            return true;
        }
    };

    public static final ActionType COMMAND = new ActionType("COMMAND") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);

            String string = String.join(" ", args);
            DECENT_HOLOGRAMS.getScheduler().executeAtEntity(player, () -> {
                //
                player.chat(PAPI.setPlaceholders(player, string.replace("{player}", player.getName())));
            });
            return true;
        }
    };

    public static final ActionType CONSOLE = new ActionType("CONSOLE") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);

            String string = String.join(" ", args);
            DECENT_HOLOGRAMS.getScheduler().executeAtEntity(player, () -> {
                //
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PAPI.setPlaceholders(player, string.replace("{player}", player.getName())));
            });
            return true;
        }
    };

    public static final ActionType CONNECT = new ActionType("CONNECT") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);
            if (args != null && args.length >= 1) {
                BungeeUtils.connect(player, args[0]);
            }
            return true;
        }
    };

    public static final ActionType TELEPORT = new ActionType("TELEPORT") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);

            String string = String.join(":", args);
            String[] spl = string.split(":");
            if (spl.length == 3 || spl.length == 5) {
                string = player.getLocation().getWorld().getName() + ":" + string;
            }
            Location location = LocationUtils.asLocation(string);
            if (location == null) {
                return false;
            }
            DECENT_HOLOGRAMS.getScheduler().executeAtEntity(player, () -> player.teleport(location));
            return true;
        }
    };

    public static final ActionType SOUND = new ActionType("SOUND") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);

            if (args == null || args.length < 1) {
                return true;
            }

            String[] spl = args[0].split(":", 3);
            Sound sound;
            try {
                sound = Sound.valueOf(spl[0]);
            } catch (Throwable ignored) {
                return true;
            }

            if (spl.length < 3) {
                player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            } else {
                player.playSound(player.getLocation(), sound, Float.parseFloat(spl[1]), Float.parseFloat(spl[2]));
            }
            return true;
        }
    };

    public static final ActionType PERMISSION = new ActionType("PERMISSION") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);
            return args[0] != null && !args[0].trim().isEmpty() && player.hasPermission(args[0]);
        }
    };

    public static final ActionType NEXT_PAGE = new ActionType("NEXT_PAGE") {
        @Override
        public boolean execute(Player player, String... args) {
            if (args == null || args.length == 0) return true;
            Hologram hologram = Hologram.getCachedHologram(args[0]);
            if (hologram == null) return true;
            int nextPage = hologram.getPlayerPage(player) + 1;
            if (nextPage < 0 || hologram.size() <= nextPage) return true;
            hologram.show(player, nextPage);
            return true;
        }
    };

    public static final ActionType PREV_PAGE = new ActionType("PREV_PAGE") {
        @Override
        public boolean execute(Player player, String... args) {
            if (args == null || args.length == 0) return true;
            Hologram hologram = Hologram.getCachedHologram(args[0]);
            if (hologram == null) return true;
            int prevPage = hologram.getPlayerPage(player) - 1;
            if (prevPage < 0 || hologram.size() <= prevPage) return true;
            hologram.show(player, prevPage);
            return true;
        }
    };

    public static final ActionType PAGE = new ActionType("PAGE") {
        @Override
        public boolean execute(Player player, String... args) {
            if (args == null || args.length == 0) return true;
            String[] spl = args[0].split(":");
            Hologram hologram = Hologram.getCachedHologram(spl[0]);
            if (hologram == null) return true;
            int page = CommandValidator.getInteger(spl[1]);
            if (page < 1 || page > hologram.size()) return true;
            hologram.show(player, page - 1);
            return true;
        }
    };

    /*
     * Abstract Methods
     */

    @Getter
    private final String name;

    public ActionType(@NonNull String name) {
        name = name.toUpperCase();
        if (VALUES.containsKey(name)) {
            throw new IllegalArgumentException("ActionType " + name + " already exists!");
        }
        VALUES.put(this.name = name, this);
    }

    public abstract boolean execute(Player player, String... args);

}
