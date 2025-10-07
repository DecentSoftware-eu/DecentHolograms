package eu.decentsoftware.holograms.api.expansion.requirement;

import org.jetbrains.annotations.Nullable;

/**
 * The result of a requirement check.
 */
public interface CheckResult {

    /**
     * Returns an error message if the requirement is not met, or null if it is met.
     *
     * @return the error message, or null
     */
    @Nullable
    String getErrorMessage();

    /**
     * Success state.
     *
     * @return true if the requirement is met, false otherwise
     */
    boolean isSuccess();

    /**
     * Whether this result should be logged or not.
     *
     * @return true if the result is silent, false otherwise
     */
    default boolean isSilent() {
        return false;
    }

    /**
     * Creates a successful CheckResult.
     *
     * @return a successful CheckResult
     */
    static CheckResult success() {
        return new CheckResult() {
            @Override
            public @Nullable String getErrorMessage() {
                return null;
            }

            @Override
            public boolean isSuccess() {
                return true;
            }
        };
    }

    /**
     * Creates a failed CheckResult with the given error message.
     *
     * @param errorMessage the error message, may be null
     * @return a failed CheckResult
     */
    static CheckResult failure(String errorMessage) {
        return new CheckResult() {
            @Override
            public @Nullable String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public boolean isSuccess() {
                return false;
            }
        };
    }
}
