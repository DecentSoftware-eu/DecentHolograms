package eu.decentsoftware.holograms.api;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.config.ConfigValue;
import eu.decentsoftware.holograms.api.utils.config.FileConfig;
import eu.decentsoftware.holograms.api.utils.config.Phrase;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class Lang {

    private static final DecentHolograms DECENT_HOLOGRAMS = DecentHologramsAPI.get();
    private static final FileConfig CONFIG = new FileConfig(DECENT_HOLOGRAMS.getPlugin(), "lang.yml");

    // General
    public static final Phrase PREFIX = new Phrase(CONFIG, "prefix", Common.PREFIX);
    public static final Phrase NO_PERM = new Phrase(CONFIG, "no_perm", "{prefix}&cYou are not allowed to use this.");
    public static final Phrase ONLY_PLAYER = new Phrase(CONFIG, "only_player", "{prefix}&cThis action can only be executed by player.");
    public static final Phrase RELOADED = new Phrase(CONFIG, "reloaded", "{prefix}&aSuccessfully reloaded in %1$d ms!");

    // Commands
    public static final Phrase USE_HELP = new Phrase(CONFIG, "command.use_help", "{prefix}Use &b/holograms help&7 to view possible commands.");
    public static final Phrase COMMAND_USAGE = new Phrase(CONFIG, "command.usage", "{prefix}Usage: &b%1$s");
    public static final Phrase UNKNOWN_SUB_COMMAND = new Phrase(CONFIG, "command.unknown_sub_command", "{prefix}Unknown sub command.");

    // Hologram Edit
    public static final Phrase HOLOGRAM_DOES_NOT_EXIST = new Phrase(CONFIG, "hologram.does_not_exist", "{prefix}&cHologram with that name doesn't exist.");
    public static final Phrase HOLOGRAM_ALREADY_EXISTS = new Phrase(CONFIG, "hologram.already_exists", "{prefix}&cHologram with that name already exists.");
    public static final Phrase HOLOGRAM_INVALID_NAME = new Phrase(CONFIG, "hologram.invalid_name", "{prefix}&cInvalid name '%1$s', only use alphanumerical characters, underscore and dash.");
    public static final Phrase HOLOGRAM_CREATED = new Phrase(CONFIG, "hologram.created", "{prefix}Hologram has been created!");
    public static final Phrase HOLOGRAM_CLONED = new Phrase(CONFIG, "hologram.cloned", "{prefix}Hologram has been cloned!");
    public static final Phrase HOLOGRAM_DELETED = new Phrase(CONFIG, "hologram.deleted", "{prefix}Hologram has been deleted!");
    public static final Phrase HOLOGRAM_UPDATED = new Phrase(CONFIG, "hologram.updated", "{prefix}Hologram has been updated!");
    public static final Phrase HOLOGRAM_RENAMED = new Phrase(CONFIG, "hologram.renamed", "{prefix}Hologram has been renamed!");
    public static final Phrase HOLOGRAM_TELEPORTED = new Phrase(CONFIG, "hologram.teleported", "{prefix}Teleported!");
    public static final Phrase HOLOGRAM_MOVED = new Phrase(CONFIG, "hologram.moved", "{prefix}Hologram has been moved!");
    public static final Phrase HOLOGRAM_ALIGNED = new Phrase(CONFIG, "hologram.aligned", "{prefix}Hologram has been aligned!");
    public static final Phrase HOLOGRAM_ALIGN_SELF = new Phrase(CONFIG, "hologram.align_self", "{prefix}Cannot align a Hologram to itself!");
    public static final Phrase HOLOGRAM_ALIGN_AXIS = new Phrase(CONFIG, "hologram.align_axis", "{prefix}That axis does not exist!");
    public static final Phrase HOLOGRAM_DOWN_ORIGIN_SET = new Phrase(CONFIG, "hologram.down_origin_set", "{prefix}Origin has been set to &b'%1$s'&7!");
    public static final Phrase HOLOGRAM_DOWN_ORIGIN_DOES_NOT_EXIST = new Phrase(CONFIG, "hologram.down_origin_does_not_exist", "{prefix}Down origin value must be either true or false!");
    public static final Phrase HOLOGRAM_ALWAYS_FACE_PLAYER_SET = new Phrase(CONFIG, "hologram.always_face_player_set", "{prefix}Always Face Player has been set to &b'%1$s'&7!");
    public static final Phrase HOLOGRAM_ALWAYS_FACE_PLAYER_DOES_NOT_EXIST = new Phrase(CONFIG, "hologram.always_face_player_does_not_exist", "{prefix}Always Face Player value must be either true or false!");
    public static final Phrase HOLOGRAM_FACING_SET = new Phrase(CONFIG, "hologram.facing_set", "{prefix}Facing has been set!");
    public static final Phrase HOLOGRAM_FLAG_ADDED = new Phrase(CONFIG, "hologram.flag_add", "{prefix}Flag &b\"%1$s\"&7 has been added!");
    public static final Phrase HOLOGRAM_FLAG_REMOVED = new Phrase(CONFIG, "hologram.flag_remove", "{prefix}Flag &b\"%1$s\"&7 has been removed!");
    public static final Phrase HOLOGRAM_PERMISSION_SET = new Phrase(CONFIG, "hologram.permission_set", "{prefix}Permission has been set!");
    public static final Phrase HOLOGRAM_PERMISSION_REMOVED = new Phrase(CONFIG, "hologram.permission_removed", "{prefix}Permission has been removed!");
    public static final Phrase HOLOGRAM_DISPLAY_RANGE_SET = new Phrase(CONFIG, "hologram.display_range_set", "{prefix}Display range has been set!");
    public static final Phrase HOLOGRAM_UPDATE_RANGE_SET = new Phrase(CONFIG, "hologram.update_range_set", "{prefix}Update range has been set!");
    public static final Phrase HOLOGRAM_UPDATE_INTERVAL_SET = new Phrase(CONFIG, "hologram.update_interval_set", "{prefix}Update interval has been set!");
    public static final Phrase HOLOGRAM_DISABLED = new Phrase(CONFIG, "hologram.disabled", "{prefix}Hologram has been disabled!");
    public static final Phrase HOLOGRAM_ALREADY_DISABLED = new Phrase(CONFIG, "hologram.already_disabled", "{prefix}Hologram is already disabled!");
    public static final Phrase HOLOGRAM_ENABLED = new Phrase(CONFIG, "hologram.enabled", "{prefix}Hologram has been enabled!");
    public static final Phrase HOLOGRAM_ALREADY_ENABLED = new Phrase(CONFIG, "hologram.already_enabled", "{prefix}Hologram is already enabled!");
    public static final Phrase HOLOGRAM_SAVE_FAILED = new Phrase(CONFIG, "hologram.save_failed", "{prefix}&cHologram could not be saved. Check the console for errors!");

    // Page Edit
    public static final Phrase PAGE_ADDED = new Phrase(CONFIG, "page.added", "{prefix}Page has been added!");
    public static final Phrase PAGE_ADD_FAILED = new Phrase(CONFIG, "page.add_failed", "{prefix}Page has been added!");
    public static final Phrase PAGE_INSERTED = new Phrase(CONFIG, "page.inserted", "{prefix}Page has been inserted!");
    public static final Phrase PAGE_INSERT_FAILED = new Phrase(CONFIG, "page.insert_failed", "{prefix}Page has been inserted!");
    public static final Phrase PAGE_DELETED = new Phrase(CONFIG, "page.deleted", "{prefix}Page has been deleted!");
    public static final Phrase PAGE_SWAPPED = new Phrase(CONFIG, "page.swapped", "{prefix}Pages swapped!");
    public static final Phrase PAGE_SWAP_SELF = new Phrase(CONFIG, "page.swap_self", "{prefix}&cCannot swap a page with itself!");
    public static final Phrase PAGE_SWAP_FAILED = new Phrase(CONFIG, "page.swap_failed", "{prefix}&cFailed to swap pages.");
    public static final Phrase PAGE_DOES_NOT_EXIST = new Phrase(CONFIG, "page.does_not_exist", "{prefix}&cThat page doesn't exist.");

    // Actions
    public static final Phrase CLICK_TYPE_DOES_NOT_EXIST = new Phrase(CONFIG, "action.click_type_does_not_exist", "{prefix}&cThat click type doesn't exist.");
    public static final Phrase ACTION_DOES_NOT_EXIST = new Phrase(CONFIG, "action.does_not_exist", "{prefix}&cThat action doesn't exist.");
    public static final Phrase ACTION_ADDED = new Phrase(CONFIG, "action.added", "{prefix}Action has been added.");
    public static final Phrase ACTION_REMOVED = new Phrase(CONFIG, "action.removed", "{prefix}Action has been removed.");
    public static final Phrase ACTION_CLEARED = new Phrase(CONFIG, "action.cleared", "{prefix}Actions have been cleared.");
    public static final Phrase ACTION_NO_ACTIONS = new Phrase(CONFIG, "action.no_actions", "{prefix}There are no actions set on that click type.");

    // Line Edit
    public static final Phrase LINE_ADDED = new Phrase(CONFIG, "line.added", "{prefix}Line has been added!");
    public static final Phrase LINE_ADD_FAILED = new Phrase(CONFIG, "line.add_failed", "{prefix}&cFailed to add line.");
    public static final Phrase LINE_SET = new Phrase(CONFIG, "line.set", "{prefix}Line has been set!");
    public static final Phrase LINE_EDIT = new Phrase(CONFIG, "line.edit", "{prefix}&a&l&nClick to edit the line!");
    public static final Phrase LINE_EDIT_HOVER = new Phrase(CONFIG, "line.edit_hover", "&r%1$s");
    public static final Phrase LINE_INSERTED = new Phrase(CONFIG, "line.inserted", "{prefix}Line has been inserted!");
    public static final Phrase LINE_INSERT_FAILED = new Phrase(CONFIG, "line.insert_failed", "{prefix}&cFailed to insert line.");
    public static final Phrase LINE_REMOVED = new Phrase(CONFIG, "line.removed", "{prefix}Line has been removed!");
    public static final Phrase LINE_SWAPPED = new Phrase(CONFIG, "line.swapped", "{prefix}Lines has been swapped!");
    public static final Phrase LINE_SWAP_SELF = new Phrase(CONFIG, "line.swap_self", "{prefix}&cCannot swap a line with itself!");
    public static final Phrase LINE_SWAP_FAILED = new Phrase(CONFIG, "line.swap_failed", "{prefix}&cFailed to swap lines.");
    public static final Phrase LINE_ALIGNED = new Phrase(CONFIG, "line.aligned", "{prefix}Line has been aligned!");
    public static final Phrase LINE_ALIGN_SELF = new Phrase(CONFIG, "line.align_self", "{prefix}Cannot align a Line to itself!");
    public static final Phrase LINE_ALIGN_AXIS = new Phrase(CONFIG, "line.align_axis", "{prefix}That axis does not exist!");
    public static final Phrase LINE_HEIGHT_SET = new Phrase(CONFIG, "line.height_set", "{prefix}Line height has been set!");
    public static final Phrase LINE_OFFSETX_SET = new Phrase(CONFIG, "line.offsetx_set", "{prefix}Line OffsetX has been set!");
    public static final Phrase LINE_OFFSETZ_SET = new Phrase(CONFIG, "line.offsetz_set", "{prefix}Line OffsetZ has been set!");
    public static final Phrase LINE_FLAG_ADDED = new Phrase(CONFIG, "line.flag_added", "{prefix}Flag &b\"%1$s\"&7 has been added!");
    public static final Phrase LINE_FLAG_REMOVED = new Phrase(CONFIG, "line.flag_removed", "{prefix}Flag &b\"%1$s\"&7 has been removed!");
    public static final Phrase LINE_DOES_NOT_EXIST = new Phrase(CONFIG, "line.does_not_exist", "{prefix}&cThat line doesn't exist.");
    public static final Phrase LINE_PERMISSION_SET = new Phrase(CONFIG, "line.permission_set", "{prefix}Permission has been set!");
    public static final Phrase LINE_PERMISSION_REMOVED = new Phrase(CONFIG, "line.permission_removed", "{prefix}Permission has been removed!");
    public static final Phrase LINE_FACING_SET = new Phrase(CONFIG, "line.facing_set", "{prefix}Facing has been set!");

    // Features
    public static final Phrase FEATURE_DOES_NOT_EXIST = new Phrase(CONFIG, "feature.does_not_exist", "{prefix}&cFeature \"%1$s\" does not exist.");
    public static final Phrase FEATURE_ENABLED = new Phrase(CONFIG, "feature.enabled", "{prefix}Feature &b\"%1$s\"&7 has been enabled!");
    public static final Phrase FEATURE_ALREADY_ENABLED = new Phrase(CONFIG, "feature.already_enabled", "{prefix}&cFeature \"%1$s\" is already enabled!");
    public static final Phrase FEATURE_DISABLED = new Phrase(CONFIG, "feature.disabled", "{prefix}Feature &b\"%1$s\"&7 has been disabled!");
    public static final Phrase FEATURE_ALREADY_DISABLED = new Phrase(CONFIG, "feature.already_disabled", "{prefix}&cFeature \"%1$s\" is already disabled!");
    public static final Phrase FEATURE_RELOADED = new Phrase(CONFIG, "feature.reloaded", "{prefix}Feature &b\"%1$s\"&7 has been reloaded!");

    /*
     *	General Methods
     */

    private static final Map<String, ConfigValue<?>> VALUES = Maps.newHashMap();

    static {
        try {
            Field[] fields = Lang.class.getFields();
            for (Field field : fields) {
                if (field.getType().isAssignableFrom(Phrase.class)) {
                    VALUES.put(field.getName(), (Phrase) field.get(null));
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Lang.reload();
    }

    public static void reload() {
        CONFIG.reload();
        VALUES.values().forEach(ConfigValue::updateValue);
        Common.PREFIX = PREFIX.getValue();
    }

    public static void sendVersionMessage(@NonNull CommandSender sender) {
        Common.tell(sender,
                "\n&fThis server is running &3DecentHolograms v%s&f by &bd0by&f : &7%s",
                DecentHologramsAPI.get().getPlugin().getDescription().getVersion(),
                "https://www.spigotmc.org/resources/96927/"
        );
    }

    public static void sendUpdateMessage(@NonNull CommandSender sender) {
        Common.tell(sender,
                "\n&fA newer version of &3DecentHolograms &fis available. Download it from: &7%s",
                "https://www.spigotmc.org/resources/96927/"
        );
    }

    @NonNull
    public static List<String> getHologramInfo(@NonNull Hologram hologram) {
        List<String> info = new ArrayList<>();
        Location l = hologram.getLocation();
        info.add(String.format(" &8• &7Location: &b%s, %.2f, %.2f, %.2f", l.getWorld().getName(), l.getX(), l.getY(), l.getZ()));
        info.add(String.format(" &8• &7Enabled: &b%s", hologram.isEnabled()));
        if (hologram.getPermission() != null && !hologram.getPermission().isEmpty()) {
            info.add(String.format(" &8• &7Permission: &b%s", hologram.getPermission()));
        }
        info.add(String.format(" &8• &7Pages: &b%d", hologram.size()));
        info.add(String.format(" &8• &7Facing: &b%.1f deg", hologram.getFacing()));
        info.add(String.format(" &8• &7Down Origin: &b%b", hologram.isDownOrigin()));
        info.add(String.format(" &8• &7Update Interval: &b%d ticks", hologram.getUpdateInterval()));
        info.add(String.format(" &8• &7Update Range: &b%d", hologram.getUpdateRange()));
        info.add(String.format(" &8• &7Display Range: &b%d", hologram.getDisplayRange()));
        if (!hologram.getFlags().isEmpty()) {
            String.format(" &8• &7Flags: &b%s", hologram.getFlags().stream().map(EnumFlag::name).collect(Collectors.joining(", ")));
        }
        return info;
    }

}
