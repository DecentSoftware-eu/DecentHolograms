package eu.decentsoftware.holograms.api.commands;

import org.bukkit.command.CommandSender;

@FunctionalInterface
public interface CommandHandler {

	/**
	 * Handle Command.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return True or False depending on success.
	 */
	boolean handle(CommandSender sender, String[] args) throws DecentCommandException;

}
