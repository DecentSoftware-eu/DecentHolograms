package eu.decentsoftware.holograms.api.actions;

import lombok.NonNull;
import org.bukkit.entity.Player;

public class Action {

	private final ActionType type;
	private final String data;

	public Action(@NonNull String string) {
		String[] spl = string.split(":", 2);
		this.type = ActionType.getByName(spl[0]);
		this.data = spl.length > 1 ? spl[1] : "";
	}

	public Action(ActionType type, String data) {
		this.type = type;
		this.data = data;
	}

	public boolean execute(Player player) {
		return type.execute(player, data);
	}

	public boolean isValid() {
		ActionType type = ActionType.getByName(toString().split(":", 2)[0]);
		return type != null;
	}

	@Override
	public String toString() {
		return String.format("%s:%s", type.getName(), data);
	}

}
