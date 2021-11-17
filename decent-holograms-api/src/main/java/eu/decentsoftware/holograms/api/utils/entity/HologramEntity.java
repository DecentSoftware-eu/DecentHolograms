package eu.decentsoftware.holograms.api.utils.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.EntityType;

@Getter
@AllArgsConstructor
public class HologramEntity {

	private final String content;
	private EntityType type;

	public HologramEntity(String string) {
		this.content = string;
		this.parseContent();
	}

	private void parseContent() {
		String string = content.trim();

		type = DecentEntityType.parseEntityType(string.trim());
		if (type == null) {
			type = EntityType.PIG;
		}
	}

}
