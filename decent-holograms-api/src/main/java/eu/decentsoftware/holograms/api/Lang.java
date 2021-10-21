package eu.decentsoftware.holograms.api;

import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.utils.Common;
import eu.decentsoftware.holograms.utils.config.ConfigValue;
import eu.decentsoftware.holograms.utils.config.Configuration;
import eu.decentsoftware.holograms.utils.config.Phrase;
import eu.decentsoftware.holograms.utils.message.Message;
import lombok.experimental.UtilityClass;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.Map;

@UtilityClass
public class Lang {

	private static final DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();
	private static final Configuration CONFIG = new Configuration(PLUGIN.getPlugin(), "lang.yml");

	// General
	public static final Phrase PREFIX = new Phrase(CONFIG, "prefix", "&8[&3DecentHolograms&8] &7");
	public static final Phrase NO_PERM = new Phrase(CONFIG, "no_perm", "{prefix}&cYou are not allowed to use this.");
	public static final Phrase ONLY_PLAYER = new Phrase(CONFIG, "only_player", "{prefix}&cThis action can only be executed by player.");
	public static final Phrase RELOADED = new Phrase(CONFIG, "reloaded", "{prefix}Successfully reloaded!");

	// Commands
	public static final Phrase USE_HELP = new Phrase(CONFIG, "command.use_help", "{prefix}Use &b/holograms help&7 to view possible commands.");
	public static final Phrase COMMAND_USAGE = new Phrase(CONFIG, "command.usage", "{prefix}Usage: &b%1$s");
	public static final Phrase UNKNOWN_SUB_COMMAND = new Phrase(CONFIG, "command.unknown_sub_command", "{prefix}Unknown sub command.");

	// Hologram Edit
	public static final Phrase HOLOGRAM_DOES_NOT_EXIST = new Phrase(CONFIG, "hologram.does_not_exist", "{prefix}&cHologram with that name doesn't exist.");
	public static final Phrase HOLOGRAM_ALREADY_EXISTS = new Phrase(CONFIG, "hologram.already_exists", "{prefix}&cHologram with that name already exists.");
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
	public static final Phrase HOLOGRAM_ORIGIN_SET = new Phrase(CONFIG, "hologram.origin_set", "{prefix}Origin has been set to &b'%1$s'&7!");
	public static final Phrase HOLOGRAM_ORIGIN_DOES_NOT_EXIST = new Phrase(CONFIG, "hologram.origin_does_not_exist", "{prefix}Origin with that name does not exist!");
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

	// Line Edit
	public static final Phrase LINE_ADDED = new Phrase(CONFIG, "line.added", "{prefix}Line has been added!");
	public static final Phrase LINE_ADD_FAILED = new Phrase(CONFIG, "line.add_failed", "{prefix}&cFailed to add line.");
	public static final Phrase LINE_SET = new Phrase(CONFIG, "line.set", "{prefix}Line has been set!");
	public static final Phrase LINE_EDIT = new Phrase(CONFIG, "line.edit", "{prefix}&aClick to edit the line!");
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
	public static final Phrase LINE_DOES_NOT_EXIST = new Phrase(CONFIG, "line.does_not_exist", "{prefix}&cLine with index %1$s is not present.");
	public static final Phrase LINE_PERMISSION_SET = new Phrase(CONFIG, "line.permission_set", "{prefix}Permission has been set!");
	public static final Phrase LINE_PERMISSION_REMOVED = new Phrase(CONFIG, "line.permission_removed", "{prefix}Permission has been removed!");

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

	public static void sendVersionMessage(CommandSender sender) {
		String format = "\n&fThis server is running &3DecentHolograms v%s&f by &bd0by&f : &7%s";
		String version = PLUGIN.getPlugin().getDescription().getVersion();
		String url = "https://www.spigotmc.org/resources/96927/";
		if (sender instanceof Player) {
			Message.sendHoverURL((Player) sender, Common.colorize(String.format(format, version, url)), url, url);
			return;
		}
		Common.tell(sender, format, version, url);
	}

}
