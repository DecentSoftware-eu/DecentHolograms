package eu.decentsoftware.holograms.api.holograms.objects;

import eu.decentsoftware.holograms.api.holograms.enums.EnumFlag;
import lombok.Getter;

import java.util.*;

@Getter
public abstract class FlagHolder {

    protected final Set<EnumFlag> flags = Collections.synchronizedSet(new HashSet<>());

    public void addFlags(EnumFlag... flags) {
        this.flags.addAll(Arrays.asList(flags));
    }

    public void removeFlags(EnumFlag... flags) {
        for (EnumFlag flag : flags) {
            this.flags.remove(flag);
        }
    }

    public boolean hasFlag(EnumFlag flag) {
        return flags.contains(flag);
    }

}
