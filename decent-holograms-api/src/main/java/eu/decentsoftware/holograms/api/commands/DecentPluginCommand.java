package eu.decentsoftware.holograms.api.commands;

import eu.decentsoftware.holograms.api.exception.DecentCommandException;
import eu.decentsoftware.holograms.utils.Common;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.logging.Level;

/**
 * This class represents a command that can be registered.
 *
 * @see DecentCommand
 */
public abstract class DecentPluginCommand extends DecentCommand implements TabExecutor {


	/**
	 * Create new instance of DecentPluginCommand.
	 * <p>
	 * The command won't be registered, you must use {@code register()} method to register it.
	 * </p>
	 *
	 * @param name       Name of the command
	 * @param permission Permission required to execute the command.
	 * @param aliases    All the aliases for the command.
	 * @param playerOnly Boolean - Is the command executable only by players?
	 */
	public DecentPluginCommand(String name, String permission, boolean playerOnly, String... aliases) {
		super(name, permission, playerOnly, aliases);
	}

	/**
	 * Register this command.
	 *
	 * @param plugin The plugin registering this command.
	 */
	public void register(JavaPlugin plugin) {
		PluginCommand command = plugin.getServer().getPluginCommand(name);
		if (command == null) {
			Common.log(Level.SEVERE, "Command \"%s\" could not be registered as it is not defined in plugin.yml.", name);
			return;
		}
		command.setExecutor(this);
		command.setTabCompleter(this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		try {
			return this.handle(sender, args);
		} catch (DecentCommandException e) {
			Common.tell(sender, e.getMessage());
			return true;
		}
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
		return this.handeTabComplete(sender, args);
	}

}
