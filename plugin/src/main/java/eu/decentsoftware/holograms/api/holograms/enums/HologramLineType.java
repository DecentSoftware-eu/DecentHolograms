package eu.decentsoftware.holograms.api.holograms.enums;

import lombok.Getter;

/**
 * This enum contains all possible types of Hologram Lines.
 */
@Getter
public enum HologramLineType {
	UNKNOWN(0, 0),
	TEXT(-0.5d, -2.45d);

	private final double offsetY;
	private final double clickableOffsetY;

	HologramLineType(double offsetY, double clickableOffsetY) {
		this.offsetY = offsetY;
		this.clickableOffsetY = clickableOffsetY;
	}

}
