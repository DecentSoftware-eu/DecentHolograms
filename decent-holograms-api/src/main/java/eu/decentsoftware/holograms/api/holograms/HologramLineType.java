package eu.decentsoftware.holograms.api.holograms;

import lombok.Getter;

/**
 * This enum contains all possible types of Hologram Lines.
 */
@Getter
public enum HologramLineType {
	UNKNOWN(0.0d),
	TEXT(-0.5d),
	HEAD(-2.0d),
	SMALLHEAD(-1.1875d),
	ICON(-0.55d),
	ENTITY(-0.5d);

	private final double offsetY;

	HologramLineType(double offsetY) {
		this.offsetY = offsetY;
	}
}
