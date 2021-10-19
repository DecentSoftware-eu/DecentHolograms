package eu.decentsoftware.holograms.utils.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.EntityType;

@Getter
@AllArgsConstructor
public class HologramEntity {

	private EntityType type;
	private String nbt;

	public HologramEntity(String string) {
		string = string.trim();

		int nbtStart = string.indexOf('{');
		int nbtEnd = string.lastIndexOf('}');
		if (nbtStart > 0 && nbtEnd > 0 && nbtEnd > nbtStart) {
			this.nbt = string.substring(nbtStart, nbtEnd + 1);
			string = string.substring(0, nbtStart) + string.substring(nbtEnd + 1);
		}

		try {
			type = DecentEntityType.parseEntityType(string.trim());
		} catch (Throwable ignored) {}

		if (type == null) {
			type = EntityType.PIG;
		}
	}

}
