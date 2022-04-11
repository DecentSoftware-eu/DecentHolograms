package eu.decentsoftware.holograms.api.utils.config;

import java.util.HashMap;
import java.util.Map;

public class MapableConfigValues extends ConfigValue<Map<String, String>> {
    
    public MapableConfigValues(Configuration config, boolean setDefault, String path, Map<String, String> defaultValue) {
        super(config, setDefault, path, defaultValue);
    }
    
    public MapableConfigValues(Configuration config, String path, Map<String, String> defaultValue){
        super(config, true, path, defaultValue);
    }
    
    @Override
    public void updateValue() {
        if (!config.contains(path)) {
            value = defaultValue;
            if (setDefault) {
                for (Map.Entry<String, String> values : defaultValue.entrySet()) {
                    config.set(path + "." + values.getKey(), values.getValue());
                }
                
                config.saveData();
                config.reload();
            }
        } else {
            Map<String, String> temp = new HashMap<>();
            for (String key : config.getSectionKeys(path)) {
                temp.put(key, config.getString(path + "." + key));
            }
            
            value = temp.isEmpty() ? defaultValue : temp;
        }
    }
}
