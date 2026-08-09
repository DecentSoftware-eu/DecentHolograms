package eu.decentsoftware.holograms.plugin;

import eu.decentsoftware.holograms.api.DecentHolograms;
import eu.decentsoftware.holograms.api.DecentHologramsAPI;
import eu.decentsoftware.holograms.api.commands.CommandManager;
import eu.decentsoftware.holograms.api.commands.DecentCommand;
import eu.decentsoftware.holograms.api.holograms.HologramManager;
import eu.decentsoftware.holograms.display.DisplayModule;
import eu.decentsoftware.holograms.display.command.DisplaysCommand;
import eu.decentsoftware.holograms.hook.NbtApiHook;
import eu.decentsoftware.holograms.logging.Log;
import eu.decentsoftware.holograms.platform.bukkit.BukkitPlatformBootstrap;
import eu.decentsoftware.holograms.platform.bukkit.BukkitPlatformFactory;
import eu.decentsoftware.holograms.platform.bukkit.UnsupportedServerException;
import eu.decentsoftware.holograms.plugin.commands.HologramsCommand;
import eu.decentsoftware.holograms.plugin.convertors.ConvertorFactory;
import eu.decentsoftware.holograms.plugin.features.DamageDisplayFeature;
import eu.decentsoftware.holograms.plugin.features.HealingDisplayFeature;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class DecentHologramsPlugin extends JavaPlugin {

    private boolean enabled = false;

    @Override
    public void onLoad() {
        Log.setLogger(getLogger());
        DecentHologramsAPI.onLoad(this);
    }

    @Override
    public void onEnable() {
        BukkitPlatformBootstrap bootstrap;
        try {
            bootstrap = new BukkitPlatformFactory().create(this);
        } catch (UnsupportedServerException e) {
            if (e.getCause() == null) {
                getLogger().severe(e.getMessage());
            } else {
                getLogger().log(Level.SEVERE, e.getMessage(), e.getCause());
            }
            getLogger().severe("Plugin will now be disabled.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        DecentHologramsAPI.onEnable(bootstrap);
        enabled = true;

        DecentHolograms decentHolograms = DecentHologramsAPI.get();
        HologramManager hologramManager = decentHolograms.getHologramManager();
        decentHolograms.getFeatureManager().registerFeature(new DamageDisplayFeature(this, hologramManager));
        decentHolograms.getFeatureManager().registerFeature(new HealingDisplayFeature(this, hologramManager));

        CommandManager commandManager = decentHolograms.getCommandManager();
        DisplayModule displayModule = decentHolograms.getDisplayModule();
        DisplaysCommand displaysCommand = displayModule == null ? null : displayModule.getDisplaysCommand();
        ConvertorFactory convertorFactory = new ConvertorFactory(this, hologramManager);
        DecentCommand mainCommand = new HologramsCommand(displaysCommand, decentHolograms, convertorFactory);
        commandManager.setMainCommand(mainCommand);
        commandManager.registerCommand(mainCommand);

        // Enable NBT API to avoid lag spikes when parsing NBT for the first time.
        NbtApiHook.initialize();
    }

    @Override
    public void onDisable() {
        if (!enabled) {
            return;
        }

        DecentHologramsAPI.onDisable();
    }

}
