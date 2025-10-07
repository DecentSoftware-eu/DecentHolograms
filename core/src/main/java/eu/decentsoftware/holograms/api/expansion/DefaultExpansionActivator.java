package eu.decentsoftware.holograms.api.expansion;

import eu.decentsoftware.holograms.api.context.AppContext;
import eu.decentsoftware.holograms.api.context.AppContextFactory;
import eu.decentsoftware.holograms.api.expansion.config.ExpansionConfigSource;
import eu.decentsoftware.holograms.api.expansion.context.ExpansionContext;
import eu.decentsoftware.holograms.api.expansion.context.ExpansionContextEventHandler;
import eu.decentsoftware.holograms.api.expansion.context.ExpansionContextFactory;
import eu.decentsoftware.holograms.api.expansion.requirement.CheckResult;
import eu.decentsoftware.holograms.api.expansion.requirement.EnabledByConfigRequirement;
import eu.decentsoftware.holograms.api.expansion.requirement.ExpansionRequirement;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Default implementation of the ExpansionActivator.
 *
 * @author ZorTik
 */
public class DefaultExpansionActivator implements ExpansionActivator {
    private final AppContextFactory appContextFactory;
    private final ExpansionContextFactory expansionContextFactory;
    private final ExpansionConfigSource configSource;
    private final Logger logger;

    private final Map<String, ExpansionContext> contexts;
    private final List<String> deactivatingExpansions;

    /**
     * Creates a new DefaultExpansionActivator.
     *
     * @param appContextFactory the factory for app contexts used during activation
     * @param expansionContextFactory the factory for creating expansion contexts
     * @param logger the logger to use
     */
    public DefaultExpansionActivator(
            AppContextFactory appContextFactory,
            ExpansionContextFactory expansionContextFactory, ExpansionConfigSource configSource, Logger logger) {
        this.appContextFactory = appContextFactory;
        this.expansionContextFactory = expansionContextFactory;
        this.configSource = configSource;
        this.logger = logger;
        this.contexts = new ConcurrentHashMap<>();
        this.deactivatingExpansions = new CopyOnWriteArrayList<>();
    }

    @Override
    public boolean activateExpansion(Expansion expansion) {
        if (isExpansionActivated(expansion)) {
            throw new IllegalStateException("Expansion " + expansion.getName() + " is already activated.");
        }

        ExpansionContext context = expansionContextFactory.createExpansionContext(expansion);
        AppContext appContext = appContextFactory.createAppContext();
        if (!passesRequirements(expansion, context, appContext)) {
            return false;
        }

        return doActivate(expansion, context, appContext);
    }

    /**
     * Check if all requirements are met.
     *
     * @param expansion the expansion to check
     * @param context the expansion context
     * @param appContext the application context
     * @return true if all requirements are met, false otherwise
     */
    private boolean passesRequirements(Expansion expansion, ExpansionContext context, AppContext appContext) {
        List<ExpansionRequirement> requirements = new ArrayList<>();
        requirements.add(EnabledByConfigRequirement.getInstance());
        requirements.addAll(expansion.getRequirements());

        for (ExpansionRequirement requirement : requirements) {
            CheckResult checkResult = requirement.canEnable(expansion, context, appContext);
            if (checkResult.isSuccess()) {
                continue;
            }

            if (!checkResult.isSilent()) {
                String errorMessage = checkResult.getErrorMessage();
                logger.log(Level.WARNING, "Expansion {0} cannot be activated: {1}",
                        new Object[]{expansion.getName(), errorMessage != null ? errorMessage : "Unknown reason"});
            }

            return false;
        }

        return true;
    }

    /**
     * Actually activate the expansion.
     *
     * @param expansion the expansion to activate
     * @param context the expansion context
     * @param appContext the application context
     * @return true if activation was successful, false otherwise
     */
    private boolean doActivate(Expansion expansion, ExpansionContext context, AppContext appContext) {
        try {
            expansion.onEnable(context, appContext);
        } catch (Exception e) {
            context.close();
            logger.log(Level.SEVERE, "Exception while activating expansion " + expansion.getName(), e);

            return false;
        }

        if (context.isClosed()) {
            logger.log(Level.WARNING, "Expansion {0} closed its context during onEnable, it won't be activated.",
                    expansion.getName());
            return false;
        }

        // Add watcher for context close
        context.addContextEventHandler(createExpansionWatchingHandler(expansion));

        contexts.put(expansion.getId(), context);

        logger.log(Level.INFO, "Activated expansion {0} v{1} by {2}",
                new Object[]{expansion.getName(), expansion.getVersion(), expansion.getAuthor()});
        return true;
    }

    /**
     * Create a handler that watches for context closure and deactivates the expansion if it happens from
     * the inside of the expansion.
     *
     * @param expansion the expansion to watch
     * @return the created event handler
     */
    private @NotNull ExpansionContextEventHandler createExpansionWatchingHandler(Expansion expansion) {
        return new ExpansionContextEventHandler() {
            @Override
            public void onContextClosed(ExpansionContext context) {
                // If the expansion is already being deactivated, the context is closed on purpose
                if (!deactivatingExpansions.contains(expansion.getId())) {
                    logger.log(Level.WARNING, "Expansion {0} closed its context, deactivating it.",
                            expansion.getName());

                    deactivateExpansion(expansion);
                }
            }
        };
    }

    @Override
    public void deactivateExpansion(Expansion expansion) {
        ExpansionContext context = contexts.remove(expansion.getId());
        if (context == null) {
            throw new IllegalStateException("Expansion " + expansion.getName() + " is not activated.");
        }

        deactivatingExpansions.add(expansion.getId());
        try {
            AppContext appContext = appContextFactory.createAppContext();

            expansion.onDisable(context, appContext);

            if (context.getExpansionConfig().isChanged()) {
                configSource.saveConfig(expansion, context.getExpansionConfig());
            }

            if (!context.isClosed()) {
                context.close();
            }
        } finally {
            deactivatingExpansions.remove(expansion.getId());
        }
    }

    @Override
    public boolean isExpansionActivated(Expansion expansion) {
        return contexts.containsKey(expansion.getId());
    }
}
