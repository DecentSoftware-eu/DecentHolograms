package eu.decentsoftware.holograms.api.expansion.requirement;

import eu.decentsoftware.holograms.api.context.AppContext;
import eu.decentsoftware.holograms.api.expansion.Expansion;
import eu.decentsoftware.holograms.api.expansion.context.ExpansionContext;

public interface ExpansionRequirement {

    /**
     * Checks if the requirement is met for the given expansion and application context.
     *
     * @param expansion the expansion to check the requirement for
     * @param appContext the application context
     * @return the result of the requirement check
     */
    CheckResult canEnable(Expansion expansion, ExpansionContext context, AppContext appContext);
}
