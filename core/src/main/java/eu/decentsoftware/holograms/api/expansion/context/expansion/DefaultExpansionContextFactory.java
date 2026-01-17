package eu.decentsoftware.holograms.api.expansion.context.expansion;

import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.expansion.config.ExpansionConfig;
import eu.decentsoftware.holograms.api.expansion.config.ExpansionConfigSource;
import eu.decentsoftware.holograms.nms.NmsPacketListenerService;
import eu.decentsoftware.holograms.plugin.file.FileSystemService;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.logging.Logger;

public class DefaultExpansionContextFactory implements ExpansionContextFactory {
    private final CommandManager commandManager;
    private final NmsPacketListenerService packetListenerService;
    private final ExpansionConfigSource configSource;
    private final FileSystemService fileSystemService;
    private final Plugin plugin;
    private final Logger logger;

    public DefaultExpansionContextFactory(
            CommandManager commandManager,
            NmsPacketListenerService packetListenerService,
            ExpansionConfigSource configSource, FileSystemService fileSystemService, Plugin plugin, Logger logger) {
        this.commandManager = commandManager;
        this.packetListenerService = packetListenerService;
        this.configSource = configSource;
        this.fileSystemService = fileSystemService;
        this.plugin = plugin;
        this.logger = logger;
    }

    @Override
    public ExpansionContext createExpansionContext(Expansion expansion) {
        ExpansionConfig config = configSource.loadOrCreateConfig(expansion);
        File dataFolder = fileSystemService.getExpansionDataDirectory(expansion.getId());

        return new DefaultExpansionContext(
                commandManager, packetListenerService, config, dataFolder, plugin, logger);
    }
}
