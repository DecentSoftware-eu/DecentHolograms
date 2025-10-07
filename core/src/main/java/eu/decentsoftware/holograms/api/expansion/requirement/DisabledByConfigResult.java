package eu.decentsoftware.holograms.api.expansion.requirement;

import org.jetbrains.annotations.Nullable;

public class DisabledByConfigResult implements CheckResult {

    @Override
    public @Nullable String getErrorMessage() {
        return "Expansion is disabled in the config.";
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isSilent() {
        return true;
    }
}
