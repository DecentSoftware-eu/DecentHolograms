package eu.decentsoftware.holograms.api.expansion.context;

import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.expansion.config.ExpansionConfig;
import eu.decentsoftware.holograms.api.expansion.config.ExpansionConfigSource;
import eu.decentsoftware.holograms.nms.NmsPacketListenerService;

import java.util.logging.Logger;

public class DefaultExpansionContextFactory implements ExpansionContextFactory {
    private final CommandManager commandManager;
    private final NmsPacketListenerService packetListenerService;
    private final ExpansionConfigSource configSource;
    private final Logger logger;

    public DefaultExpansionContextFactory(
            CommandManager commandManager,
            NmsPacketListenerService packetListenerService, ExpansionConfigSource configSource, Logger logger) {
        this.commandManager = commandManager;
        this.packetListenerService = packetListenerService;
        this.configSource = configSource;
        this.logger = logger;
    }

    @Override
    public ExpansionContext createExpansionContext(Expansion expansion) {
        ExpansionConfig config = configSource.loadOrCreateConfig(expansion);

        return new DefaultExpansionContext(commandManager, packetListenerService, config, logger);
    }
}
