package eu.decentsoftware.holograms.api.objects;

import eu.decentsoftware.holograms.api.objects.enums.EnumFlag;

import java.util.Set;

public interface IFlagsHolder {

	void addFlags(EnumFlag... flags);

	void removeFlags(EnumFlag... flags);

	boolean hasFlag(EnumFlag flag);

	Set<EnumFlag> getFlags();

}
