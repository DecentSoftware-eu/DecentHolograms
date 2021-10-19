package eu.decentsoftware.holograms.api;

public final class DecentHologramsProvider {

	private static DecentHolograms INSTANCE;

	/**
	 * Get the instance of running DecentHolograms.
	 * <p>
	 *     If you use shaded version of the API, you must enable
	 *     DecentHologram first from DecentHologramsAPI class.
	 * </p>
	 *
	 * @return the instance of running DecentHolograms.
	 */
	public static DecentHolograms getDecentHolograms() {
		if (INSTANCE == null) {
			throw new IllegalStateException("Instance of running DecentHolograms does not exist, enable it first.");
		}
		return INSTANCE;
	}

}
