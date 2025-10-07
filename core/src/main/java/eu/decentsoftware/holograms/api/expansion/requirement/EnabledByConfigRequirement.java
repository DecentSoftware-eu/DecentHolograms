package eu.decentsoftware.holograms.api.expansion.requirement;

import eu.decentsoftware.holograms.api.context.AppContext;
import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.expansion.context.ExpansionContext;
import org.jetbrains.annotations.NotNull;

/**
 * An ExpansionRequirement that checks if the expansion is enabled in the configuration.
 *
 * @author ZorTik
 */
public class EnabledByConfigRequirement implements ExpansionRequirement {
    private static EnabledByConfigRequirement instance = null;

    private EnabledByConfigRequirement() {
    }

    /**
     * Gets the singleton instance of EnabledByConfigRequirement.
     *
     * @return the singleton instance
     */
    public static @NotNull EnabledByConfigRequirement getInstance() {
        if (instance == null) {
            instance = new EnabledByConfigRequirement();
        }

        return instance;
    }

    @Override
    public CheckResult canEnable(Expansion expansion, ExpansionContext context, AppContext appContext) {
        if (context.getExpansionConfig().isEnabled()) {
            return CheckResult.success();
        } else {
            return new DisabledByConfigResult();
        }
    }
}
