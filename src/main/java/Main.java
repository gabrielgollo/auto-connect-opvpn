import org.json.JSONObject;

public class Main {
    private static ConfigManager getInitialConfigs(){
        ConfigLoader configLoader = new ConfigLoader();
        JSONObject savedConfigurationsLoaded = configLoader.getConfigs();
        return new ConfigManager(savedConfigurationsLoaded);
    }

    public static void main(String[] args) {
        ConfigManager initialConfigs = getInitialConfigs();

        String vpnHost = initialConfigs.get("vpnHost");
        String username = initialConfigs.get("username");
        String password = initialConfigs.get("password");
        String opVpnFileLocation = initialConfigs.get("opVpnFileLocation");
        String secretOtp = initialConfigs.get("secretOtp");

        MainInterface dialog = new MainInterface(vpnHost, username, password, opVpnFileLocation, secretOtp);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
