package com.GabrielGollo;

import org.json.JSONObject;

public class Main {
    private static ConfigManager getInitialConfigs(){
        ConfigLoader configLoader = new ConfigLoader();
        JSONObject savedConfigurationsLoaded = configLoader.getConfigs();
        return new ConfigManager(savedConfigurationsLoaded);
    }

    static private VpnConfigs getInitialConfiguration() {
        ConfigManager initialConfigs = getInitialConfigs();
        String vpnHost = initialConfigs.get("vpnHost");
        String username = initialConfigs.get("username");
        String password = initialConfigs.get("password");
        String opVpnFileLocation = initialConfigs.get("opVpnFileLocation");
        String secretOtp = initialConfigs.get("secretOtp");
        return new VpnConfigs(vpnHost, username, password, opVpnFileLocation, secretOtp);
    }

    public static void main(String[] args) {
        // starts getting configs
        VpnConfigs initialConfiguration = getInitialConfiguration();
        System.out.println(initialConfiguration);

        // setup events
        EventHandler eventsHandler = new EventHandler();

        // start interface
        MainInterface dialog = new MainInterface(initialConfiguration, eventsHandler);
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
