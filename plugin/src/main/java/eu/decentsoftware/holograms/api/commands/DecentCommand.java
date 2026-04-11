package eu.decentsoftware.holograms.api.commands;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.Lang;
import eu.decentsoftware.holograms.api.utils.Common;
import org.apache.commons.lang.Validate;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class DecentCommand extends Command implements CommandBase {

	protected static final DecentHolograms PLUGIN = DecentHologramsAPI.get();
	protected final Map<String, CommandBase> subCommands = new LinkedHashMap<>();
	protected final CommandInfo info;

	protected DecentCommand(String name) {
		super(name);
		this.info = getClass().getAnnotation(CommandInfo.class);
		if (info == null) {
			throw new DecentCommandException(String.format("Command %s is not annotated with @CommandInfo.", name));
		}
		this.setAliases(Arrays.asList(info.aliases()));
	}

	@Override
	public Set<String> getSubCommandNames() {
		return Collections.emptySet();
	}

	@Override
	public Collection<CommandBase> getSubCommands() {
		return subCommands.values();
	}

	@Override
	public CommandBase getSubCommand(String name) {
		Validate.notNull(name);
		return subCommands.get(name);
	}

	@Override
	public CommandBase addSubCommand(CommandBase commandBase) {
		subCommands.put(commandBase.getName(), commandBase);
		return this;
	}

	@Override
	public boolean execute(CommandSender sender, String s, String[] args) {
		try {
			return this.handle(sender, args);
		} catch (DecentCommandException e) {
			Common.tell(sender, e.getMessage());
			return true;
		}
	}

	@Override
	public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
		return handleTabComplete(sender, args);
	}

	@Override
	public String[] getPermissions() {
		return info.permissions();
	}

	@Override
	public boolean isPlayerOnly() {
		return info.playerOnly();
	}

	@Override
	public int getMinArgs() {
		return info.minArgs();
	}

	@Override
	public String getUsage() {
		return info.usage();
	}

	@Override
	public String getDescription() {
		return info.description();
	}

	protected void printHelpSubCommandsAndAliases(CommandSender sender, CommandBase command) {
		printHelpSubCommandsAndAliases(sender, command, subCommand -> true);
	}

    protected void printHelpSubCommandsAndAliases(CommandSender sender, CommandBase command, Predicate<CommandBase> subCommandFilter) {
        List<CommandBase> helpSubCommands = command.getSubCommands().stream()
				.filter(subCommandFilter)
				.collect(Collectors.toList());
        for (CommandBase subCommand : helpSubCommands) {
            Common.tell(sender, " &8• &b" + subCommand.getUsage() + " &8- &7" + subCommand.getDescription());
        }
        sender.sendMessage("");
        Common.tell(sender, " &7Aliases: &b" + getAliasesFormatted(command));
        sender.sendMessage("");
    }

	private String getAliasesFormatted(CommandBase command) {
		return command.getName() + (command.getAliases().size() > 1
				? ", " + String.join(", ", command.getAliases())
				: "");
	}

	/**
	 * Handle the Command.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return Boolean whether the execution was successful.
	 */
	protected final boolean handle(CommandSender sender, String[] args) throws DecentCommandException {
		if (!CommandValidator.canExecute(sender, this)) {
			return true;
		}

		if (args.length != 0) {
			for (CommandBase subCommand : getSubCommands()) {
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
			Lang.COMMAND_USAGE.send(sender, getUsage());
			return false;
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
	@Deprecated
	protected final List<String> handeTabComplete(CommandSender sender, String[] args) {
		return handleTabComplete(sender, args);
	}

	/**
	 * Handle Tab Complete of the Command.
	 *
	 * @param sender The sender.
	 * @param args The arguments.
	 * @return List of tab completed Strings.
	 */
	protected final List<String> handleTabComplete(CommandSender sender, String[] args) {
		if (getPermission() != null && !sender.hasPermission(getPermission())) {
			return ImmutableList.of();
		}

		if (args.length == 1) {
			List<String> subs = new ArrayList<>();
			getSubCommands().forEach(cmd -> {
				subs.add(cmd.getName());
				subs.addAll(Lists.newArrayList(cmd.getAliases()));
			});

			List<String> matches = TabCompleteHandler.getPartialMatches(args[0], subs);

			if (!matches.isEmpty()) {
				Collections.sort(matches);
				return matches;
			}
		} else if (args.length > 1) {
			for (CommandBase subCommand : getSubCommands()) {
				if (CommandValidator.isIdentifier(args[0], subCommand)) {
					return ((DecentCommand) subCommand).handleTabComplete(sender, Arrays.copyOfRange(args, 1, args.length));
				}
			}
		}

		if (this.getTabCompleteHandler() == null) {
			return ImmutableList.of();
		}

		return this.getTabCompleteHandler().handleTabComplete(sender, args);
	}

}
