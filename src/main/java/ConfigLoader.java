import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {
    String filePath = new File("").getAbsolutePath();
    String completeFilePath = filePath + "\\src\\saved_configs.json";

    JSONObject getConfigs(){
        try{


            byte[] encoded = Files.readAllBytes(Paths.get(completeFilePath));
            String fileContent = new String(encoded);

            return new JSONObject(fileContent);
        } catch (Exception error){
            System.out.println("Failed to open File!");
            return new JSONObject();
        }
    }

    void saveConfigs(JSONObject json) {
        try{
            FileWriter file = new FileWriter(completeFilePath);
            file.write(json.toString());
            file.close();
        } catch (Exception error){
            System.out.println("Failed to save File!");
        }

    }

    public static void main(String[] args) {
        ConfigLoader configLoader = new ConfigLoader();
        JSONObject configs = configLoader.getConfigs();
        System.out.println(configs.toString());
    }
}
