package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.expansion.source.BuiltInExpansionSource;
import eu.decentsoftware.holograms.api.expansion.source.JarFileExpansionSource;
import eu.decentsoftware.holograms.api.utils.Common;
import eu.decentsoftware.holograms.api.utils.file.FileUtils;
import eu.decentsoftware.holograms.plugin.file.FileSystemService;
import org.bukkit.plugin.Plugin;

public class ExpansionManager {
    private final ExpansionRegistry registry;
    private final ExpansionSourceRegistry sourceRegistry;
    private final FileSystemService fileSystemService;
    private final Plugin plugin;

    public ExpansionManager(
            ExpansionRegistry registry,
            ExpansionSourceRegistry sourceRegistry, FileSystemService fileSystemService, Plugin plugin) {
        this.registry = registry;
        this.sourceRegistry = sourceRegistry;
        this.fileSystemService = fileSystemService;
        this.plugin = plugin;
    }

    public void load() {
        ExpansionLoader loader = ExpansionLoader.getDefaultLoader();

        this.sourceRegistry.registerSource(new BuiltInExpansionSource(loader, plugin));

        // Load external expansions from jars
        FileUtils.getFilesFromTree(
                        fileSystemService.getExpansionJarsDirectory(), Common.NAME_REGEX + "\\.jar", true)
                .forEach(jarFile -> sourceRegistry.registerSource(new JarFileExpansionSource(jarFile, loader)));
    }

    public void unload() {
        sourceRegistry.getAllSources().forEach(sourceRegistry::unregisterSource);

        registry.getAllExpansions().forEach(expansion -> registry.unregisterExpansion(expansion.getId()));
    }

    public ExpansionRegistry getRegistry() {
        return registry;
    }

    public ExpansionSourceRegistry getSourceRegistry() {
        return sourceRegistry;
    }
}
