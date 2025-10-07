package eu.decentsoftware.holograms.expansion.displayentities;

import eu.decentsoftware.holograms.api.context.AppContext;
import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.expansion.context.ExpansionContext;
import org.bukkit.configuration.ConfigurationSection;

public class DisplayEntitiesExpansion implements Expansion {

    @Override
    public void onEnable(ExpansionContext context, AppContext appContext) {
        // TODO
    }

    @Override
    public void onDisable(ExpansionContext context, AppContext appContext) {
        // TODO
    }

    @Override
    public void applyConfigurationDefaults(ConfigurationSection settings) {
        // TODO
    }

    @Override
    public String getId() {
        return "display-entites";
    }

    @Override
    public String getName() {
        // TODO
        return "";
    }

    @Override
    public String getDescription() {
        // TODO
        return "";
    }

    @Override
    public String getAuthor() {
        return "d0by";
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }
}
