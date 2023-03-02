package eu.decentsoftware.holograms.api.holograms.enums;

import lombok.Getter;

/**
 * This enum contains all possible types of Hologram Lines.
 */
@Getter
public enum HologramLineType {
	UNKNOWN(0, 0),
	TEXT(-0.5d, -2.45d),
	HEAD(-2.0d, -2.0d),
	SMALLHEAD(-1.1875d, -1.1875d),
	ICON(-0.55d, -1.325d),
	ENTITY(0, -1.5d);

	private final double offsetY;
	private final double clickableOffsetY;

	HologramLineType(double offsetY, double clickableOffsetY) {
		this.offsetY = offsetY;
		this.clickableOffsetY = clickableOffsetY;
	}

}
