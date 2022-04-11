package eu.decentsoftware.holograms.api.utils.config;

import java.util.HashMap;
import java.util.Map;

public class MapConfigValue {
    
    private final Configuration config;
    private final boolean setDefault;
    private final String path;
    private final Map<String, String> defaultValue;
    private Map<String, String> value;
    
    public MapConfigValue(Configuration config, boolean setDefault, String path, Map<String, String> defaultValue) {
        this.config = config;
        this.setDefault = setDefault;
        this.path = path;
        this.defaultValue = defaultValue;
        
        this.updateValue();
    }
    
    public MapConfigValue(Configuration config, String path, Map<String, String> defaultValue){
        this(config, true, path, defaultValue);
    }
    
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
    
    public Configuration getConfig(){
        return config;
    }
    
    public String getPath(){
        return path;
    }
    
    public Map<String, String> getDefaultValue(){
        return defaultValue;
    }
    
    public Map<String, String> getValue(){
        return value;
    }
    
    public void setValue(Map<String, String> value){
        this.value = value;
    }
}
