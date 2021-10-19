package eu.decentsoftware.holograms.api.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsProvider;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.exception.DecentCommandException;
import lombok.NonNull;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class represents a command that cannot be registered,
 * use it as a SubCommand.
 *
 * @see DecentPluginCommand
 */
public abstract class DecentCommand implements CommandBase {

	protected static final DecentHolograms PLUGIN = DecentHologramsProvider.getDecentHolograms();

	/**
	 * The name of this command.
	 */
	protected final String name;

	/**
	 * The permission required to execute this command.
	 */
	protected final String permission;

	/**
	 * All aliases for this command.
	 */
	protected final String[] aliases;

	/**
	 * Is this command only executable by Players?
	 */
	protected final boolean playerOnly;

	/**
	 * All SubCommands for this command.
	 */
	protected final Map<String, CommandBase> subCommands = Maps.newLinkedHashMap();

	public DecentCommand(final String name, final String permission, final boolean playerOnly, final String... aliases) {
		this.name = name;
		this.permission = permission;
		this.aliases = aliases;
		this.playerOnly = playerOnly;
	}

	/**
	 * Get the name of this Command.
	 *
	 * @return the name of this Command.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Get the permission required to execute this Command.
	 *
	 * @return the permission required to execute this Command.
	 */
	@Override
	public String getPermission() {
		return permission;
	}

	/**
	 * Get the aliases for this Command.
	 *
	 * @return the aliases for this Command.
	 */
	@Override
	public String[] getAliases() {
		return aliases;
	}

	/**
	 * Check if this Command is only executable by Players.
	 *
	 * @return Boolean if this Command is only executable by Players.
	 */
	@Override
	public boolean isPlayerOnly() {
		return playerOnly;
	}

	/**
	 * Add a SubCommand for this Command.
	 *
	 * @param commandBase The instance of {@link CommandBase} you want to add as a SubCommand.
	 * @return The intance of this Command.
	 */
	@Override
	public CommandBase addSubCommand(@NonNull final CommandBase commandBase) {
		return subCommands.put(commandBase.getName(), commandBase);
	}

	@Override
	public CommandBase getSubCommand(@NonNull final String name) {
		return subCommands.get(name);
	}

	@Override
	public Set<String> getSubCommandNames() {
		return subCommands.keySet();
	}

	/**
	 * Get all the SubCommands for this command.
	 *
	 * @return all the SubCommands for this command.
	 */
	@Override
	public Collection<CommandBase> getSubCommands() {
		return subCommands.values();
	}

	/**
	 * Handle the Command.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return Boolean whether the execution was successful.
	 */
	protected final boolean handle(final CommandSender sender, final String[] args) throws DecentCommandException {
		if (!CommandValidator.canExecute(sender, this, true)) {
			return true;
		}

		if (args.length != 0) {
			for (CommandBase subCommand : subCommands.values()) {
				if (CommandValidator.isIdentifier(args[0], subCommand)) {
					final String[] subCommandArgs = Arrays.copyOfRange(args, 1, args.length);
					if (subCommandArgs.length < subCommand.getMinArgs()) {
						Lang.COMMAND_USAGE.send(sender, subCommand.getUsage());
						return true;
					}
					return ((DecentCommand) subCommand).handle(sender, subCommandArgs);
				}
			}
		} else if (getMinArgs() > 0) {
			Lang.USE_HELP.send(sender);
			return true;
		}

		return this.getCommandHandler().handle(sender, args);
	}

	/**
	 * Handle Tab Complete of the Command.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return List of tab completed Strings.
	 */
	protected final List<String> handeTabComplete(final CommandSender sender, final String[] args) {
		if (getPermission() != null && !sender.hasPermission(getPermission())) {
			return ImmutableList.of();
		}

		if (args.length == 1) {
			List<String> matches = Lists.newLinkedList();
			List<String> subs = subCommands.values().stream()
					.filter(sub -> CommandValidator.canExecute(sender, sub, false))
					.map(CommandBase::getName)
					.collect(Collectors.toList());

			subCommands.values().forEach(sub -> {
				if (CommandValidator.canExecute(sender, sub, false)) {
					subs.addAll(Lists.newArrayList(sub.getAliases()));
				}
			});

			StringUtil.copyPartialMatches(args[0], subs, matches);

			if (!matches.isEmpty()) {
				Collections.sort(matches);
				return matches;
			}
		} else if (args.length > 1) {
			for (CommandBase subCommand : subCommands.values()) {
				if (CommandValidator.isIdentifier(args[0], subCommand)) {
					return ((DecentCommand) subCommand).handeTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
				}
			}
		}

		if (this.getTabCompleteHandler() == null) {
			return ImmutableList.of();
		}

		return this.getTabCompleteHandler().handleTabComplete(sender, args);
	}

}
