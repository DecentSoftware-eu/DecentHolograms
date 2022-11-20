package eu.decentsoftware.holograms.api.actions;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;

@Getter
@Setter
public class Action {

	private final ActionType type;
	private String data;

	public Action(@NonNull String string) {
		if (string.contains(":")) {
			String[] spl = string.split(":", 2);
			this.type = ActionType.getByName(spl[0]);
			this.data = spl.length > 1 ? spl[1] : "";
		} else {
			this.type = ActionType.getByName(string);
			this.data = null;
		}
	}

	public Action(@NonNull ActionType type, @NonNull String data) {
		this.type = type;
		this.data = data;
	}

	public boolean execute(Player player) {
		return type.execute(player, data);
	}

	public boolean isValid() {
		return type != null;
	}

	@Override
	public String toString() {
		return data == null ? type.getName() : String.format("%s:%s", type.getName(), data);
	}

}
