package com.GabrielGollo;

import org.json.JSONObject;

public class ConfigManager {
    JSONObject _config;

    ConfigManager(JSONObject config) {
        _config = config;
    }

    String get(String key){
        try{
            String value = _config.getString(key);
            if(!(value instanceof String)) return "";
            return value;
        } catch (Exception error){
            return "";
        }
    }
}
