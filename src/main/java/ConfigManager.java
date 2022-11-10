import org.json.JSONObject;

public class ConfigManager {
    JSONObject _config;

    ConfigManager(JSONObject config) {
        _config = config;
    }

    String get(String key){
        try{
            return _config.getString(key);
        } catch (Exception error){
            return "";
        }
    }
}
