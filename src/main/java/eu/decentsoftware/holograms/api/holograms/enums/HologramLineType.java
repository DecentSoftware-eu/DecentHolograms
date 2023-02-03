package eu.decentsoftware.holograms.api.holograms.enums;

import lombok.Getter;

/**
 * This enum contains all possible types of Hologram Lines.
 */
@Getter
public enum HologramLineType {
	UNKNOWN(0, 0),
	TEXT(-0.5d, -1.4875d),
	HEAD(-2.0d, -2.0d),
	SMALLHEAD(-1.1875d, -1.1875d - 0.5d),
	ICON(-0.55d, -0.55d - 0.8875d),
	ENTITY(0, -1.5d);

	private final double offsetY;
	private final double clickableOffsetY;

	HologramLineType(double offsetY, double clickableOffsetY) {
		this.offsetY = offsetY;
		this.clickableOffsetY = clickableOffsetY;
	}

}
