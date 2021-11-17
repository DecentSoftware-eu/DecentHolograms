package eu.decentsoftware.holograms.api.actions;

import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class ActionHolder {

	protected final List<Action> actions = Lists.newArrayList();

	public void addAction(Action action) {
		actions.add(action);
	}

	public void executeActions(Player player) {
		for (Action action : actions) {
			if (!action.execute(player)) {
				return;
			}
		}
	}

	public void clearActions() {
		actions.clear();
	}

	public Action removeAction(int index) {
		return actions.remove(index);
	}

	public List<Action> getActions() {
		return actions;
	}

}
