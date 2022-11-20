package eu.decentsoftware.holograms.api.holograms.objects;

import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

@Getter
public abstract class FlagHolder {

    protected final @NonNull Set<EnumFlag> flags = Collections.synchronizedSet(new HashSet<>());

    public void addFlags(EnumFlag @NonNull ... flags) {
        this.flags.addAll(Arrays.asList(flags));
    }

    public void removeFlags(EnumFlag @NonNull ... flags) {
        for (EnumFlag flag : flags) {
            this.flags.remove(flag);
        }
    }

    public boolean hasFlag(@NonNull EnumFlag flag) {
        return flags.contains(flag);
    }

}
