package eu.decentsoftware.holograms.api.commands;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * This Interface represents a Command.
 */
public interface CommandBase {

	/**
	 * @return The name of this Command.
	 */
	String getName();

	/**
	 * @return The permission required to execute this Command.
	 */
	String getPermission();

	/**
	 * @return The aliases for this Command.
	 */
	List<String> getAliases();

	/**
	 * @return Boolean whether this Command is only executable by Players.
	 */
	boolean isPlayerOnly();

	/**
	 * @return Minimum arguments to execute this command.
	 */
	int getMinArgs();

	/**
	 * @return Usage of this command.
	 */
	String getUsage();

	/**
	 * @return Simple description of what this command does.
	 */
	String getDescription();

	/**
	 * Add a SubCommand for this Command.
	 *
	 * @param commandBase The instance of {@link CommandBase} you want to add as a SubCommand.
	 * @return Instance of this Command.
	 */
	CommandBase addSubCommand(CommandBase commandBase);

	/**
	 * Get a sub command by name.
	 *
	 * @param name Subcommand name
	 * @return Subcommand
	 */
	CommandBase getSubCommand(String name);

	/**
	 * Get the names of all sub commands.
	 *
	 * @return names of all sub commands.
	 */
	Set<String> getSubCommandNames();

	/**
	 * Get all sub commands.
	 *
	 * @return all sub commands.
	 */
	Collection<CommandBase> getSubCommands();

	/**
	 * @return The handler for this Command.
	 */
	CommandHandler getCommandHandler();

	/**
	 * @return The tab complete handler for this Command.
	 */
	TabCompleteHandler getTabCompleteHandler();

}
