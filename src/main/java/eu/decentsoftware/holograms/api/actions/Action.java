package eu.decentsoftware.holograms.api.actions;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class Action {

    private final @NonNull ActionType type;
    private @Nullable String data;

    /**
     * Create a new instance of {@link Action} from string.
     * <p>
     * The string must be in the format of {@code type:data}.
     * For example, {@code MESSAGE:Hello World!}. The format
     * is explained more in depth on the wiki along with all
     * actions types.
     *
     * @param string String to parse.
     * @throws IllegalArgumentException If the action type is invalid.
     */
    public Action(@NonNull String string) {
        if (string.contains(":")) {
            String[] spl = string.split(":", 2);
            this.type = ActionType.getByName(spl[0]);
            this.data = spl.length > 1 ? spl[1] : "";
        } else {
            this.type = ActionType.getByName(string);
            this.data = null;
        }
        if (this.type == null) {
            throw new IllegalArgumentException("Invalid action type in action: " + string);
        }
    }

    public Action(@NonNull ActionType type, @Nullable String data) {
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
