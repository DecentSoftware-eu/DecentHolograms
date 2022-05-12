package eu.decentsoftware.holograms.plugin.convertors;

import eu.decentsoftware.holograms.api.convertor.IConvertorRest;

public class ConvertorRest implements IConvertorRest {

    private final boolean success;
    private final int count;

    public ConvertorRest(boolean success, int count) {
        this.success = success;
        this.count = count;
    }

    public boolean getSuccess() {
        return success;
    }

    public int getCount() {
        return count;
    }

}
