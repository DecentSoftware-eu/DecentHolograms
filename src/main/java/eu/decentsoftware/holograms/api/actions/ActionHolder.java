package eu.decentsoftware.holograms.api.actions;

import com.google.common.collect.Lists;
import lombok.NonNull;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ActionHolder {

	protected final @NonNull List<Action> actions = Lists.newArrayList();

	public void addAction(@NonNull Action action) {
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

	@Nullable
	public Action removeAction(int index) {
		return actions.remove(index);
	}

	@NonNull
	public List<Action> getActions() {
		return actions;
	}

}
