package eu.decentsoftware.holograms.api.actions;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.commands.CommandValidator;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.utils.BungeeUtils;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.PAPI;
import eu.decentsoftware.holograms.api.utils.location.LocationUtils;
import eu.decentsoftware.holograms.api.utils.scheduler.S;
import eu.decentsoftware.holograms.logging.Log;
import eu.decentsoftware.holograms.platform.api.data.DecentLocation;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;

public abstract class ActionType {

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
            // Actions are executed from a packet listener thread, and resolving placeholders
            // reads the player, so it has to happen where the player is owned.
            S.forPlayer(player, platformPlayer -> platformPlayer.sendMessage(
                    Common.colorize(PAPI.setPlaceholders(player, string.replace("{player}", player.getName())))));
            return true;
        }
    };

    public static final ActionType COMMAND = new ActionType("COMMAND") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);

            String string = String.join(" ", args);
            S.forPlayer(player, platformPlayer -> platformPlayer.chat(
                    PAPI.setPlaceholders(player, string.replace("{player}", player.getName()))));
            return true;
        }
    };

    public static final ActionType CONSOLE = new ActionType("CONSOLE") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);

            String string = String.join(" ", args);
            // Two halves, two owners: resolving placeholders reads the player, while a console
            // command is dispatched globally.
            S.forPlayer(player, () -> {
                String command = PAPI.setPlaceholders(player, string.replace("{player}", player.getName()));
                S.sync(() -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
            });
            return true;
        }
    };

    public static final ActionType CONNECT = new ActionType("CONNECT") {
        @Override
        public boolean execute(Player player, String... args) {
            Validate.notNull(player);
            if (args != null && args.length >= 1) {
                String server = args[0];
                // Proxy plugin messaging is Bukkit-family specific, so it stays here rather than
                // moving onto PlatformPlayer - but sending still touches the player.
                S.forPlayer(player, () -> BungeeUtils.connect(player, server));
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
            if (location == null || location.getWorld() == null) {
                return false;
            }
            DecentLocation target = new DecentLocation(location.getWorld().getName(),
                    location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
            // Not player.teleport(): a cross-region move is unsupported on region-threaded
            // servers and has to go through the platform, which completes it asynchronously.
            S.forPlayer(player, platformPlayer -> platformPlayer.teleport(target));
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
            float volume = 1.0f;
            float pitch = 1.0f;
            if (spl.length >= 3) {
                try {
                    volume = Float.parseFloat(spl[1]);
                    pitch = Float.parseFloat(spl[2]);
                } catch (NumberFormatException ignored) {
                    // Fall back to the defaults rather than dropping the sound entirely.
                }
            }

            float finalVolume = volume;
            float finalPitch = pitch;
            // Reads the player's position, so it belongs on the thread that owns them.
            S.forPlayer(player, platformPlayer -> {
                try {
                    platformPlayer.playSound(spl[0], finalVolume, finalPitch);
                } catch (IllegalArgumentException e) {
                    // Reported rather than swallowed so a typo is visible, but contained so it
                    // does not surface as a scheduler stack trace on every click.
                    Log.warn("Cannot play sound for %s: %s", player.getName(), e.getMessage());
                }
            });
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
