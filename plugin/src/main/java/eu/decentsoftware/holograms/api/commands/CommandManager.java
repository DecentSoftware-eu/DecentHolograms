package eu.decentsoftware.holograms.api.commands;

import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectField;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectMethod;
import eu.decentsoftware.holograms.api.utils.reflect.ReflectionUtil;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommandManager {

    private final Map<String, DecentCommand> commands = new HashMap<>();
    private DecentCommand mainCommand;

    /*
     *  General Methods
     */

    public void destroy() {
        if (!commands.isEmpty()) {
            commands.values().forEach(CommandManager::unregister);
            commands.clear();
        }
    }

    public void registerCommand(DecentCommand decentCommand) {
        if (commands.containsKey(decentCommand.getName())) return;
        commands.put(decentCommand.getName(), decentCommand);
        CommandManager.register(decentCommand);
    }

    public void unregisterCommand(String name) {
        if (!commands.containsKey(name)) return;
        DecentCommand decentCommand = commands.remove(name);
        CommandManager.unregister(decentCommand);
    }

    public void setMainCommand(DecentCommand decentCommand) {
        this.mainCommand = decentCommand;
    }

    public DecentCommand getMainCommand() {
        return mainCommand;
    }

    public Set<String> getCommandNames() {
        return commands.keySet();
    }

    public Collection<DecentCommand> getCommands() {
        return commands.values();
    }

    /*
     *  Static Methods
     */

    private static final Class<?> CRAFT_SERVER_CLASS;
    private static final ReflectMethod GET_COMMAND_MAP_METHOD;
    private static final ReflectField<Map<String, Command>> COMMAND_MAP_KNOWN_COMMANDS_FIELD;

    static {
        CRAFT_SERVER_CLASS = ReflectionUtil.getObcClass("CraftServer");
        GET_COMMAND_MAP_METHOD = new ReflectMethod(CRAFT_SERVER_CLASS, "getCommandMap");
        COMMAND_MAP_KNOWN_COMMANDS_FIELD = new ReflectField<>(SimpleCommandMap.class, "knownCommands");
    }

    public static void register(Command command) {
        if (command == null) return;
        SimpleCommandMap commandMap = GET_COMMAND_MAP_METHOD.invoke(DecentHologramsAPI.get().getPlugin().getServer());
        CommandManager.unregister(command);
        commandMap.register("DecentHolograms", command);
    }

    public static void unregister(Command command) {
        if (command == null) return;
        SimpleCommandMap commandMap = GET_COMMAND_MAP_METHOD.invoke(DecentHologramsAPI.get().getPlugin().getServer());
        Map<String, Command> cmdMap = COMMAND_MAP_KNOWN_COMMANDS_FIELD.getValue(commandMap);
        if (cmdMap != null && !cmdMap.isEmpty()) {
            cmdMap.remove(command.getLabel());
            for (final String alias : command.getAliases()) {
                cmdMap.remove(alias);
            }
        }
    }

}
