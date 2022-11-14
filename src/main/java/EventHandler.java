import it.sauronsoftware.cron4j.Scheduler;
import org.json.JSONObject;

import javax.swing.*;
import java.util.concurrent.Callable;

public class EventHandler {
    Scheduler runningCron;

    private boolean isCronRunning () {
        if(runningCron==null){
            return false;
        } else {
            return runningCron.isStarted();
        }
    }

    private void saveConfigs(VpnConfigs mainInterfaceConfigs) {
        JSONObject configs = new JSONObject();
        configs.put("vpnHost", mainInterfaceConfigs.vpnHost());
        configs.put("username", mainInterfaceConfigs.username());
        configs.put("password", mainInterfaceConfigs.password());
        configs.put("opVpnFileLocation", mainInterfaceConfigs.opVpnFileLocation());
        configs.put("secretOtp", mainInterfaceConfigs.secretOtp());

        ConfigLoader configLoader = new ConfigLoader();
        configLoader.saveConfigs(configs);
    }

    public void onOk(MainInterface mainInterface, VpnConfigs configurations) {
        boolean cronIsRunning = isCronRunning();
        String username = configurations.username();
        String password = configurations.password();
        String vpnHost = configurations.vpnHost();
        String opVpnFileLocation = configurations.opVpnFileLocation();

        if(!(username.isBlank() || password.isBlank() || vpnHost.isBlank() || opVpnFileLocation.isBlank()) && !cronIsRunning) {
            try{
                CronInfra cronJob = new CronInfra();
                Callable<Void> service = () -> {
                    CronService.StartVpnAuth(configurations);
                    return null;
                };
                mainInterface.disableStartButton();
                runningCron = cronJob.startJob(service);

            } catch (Exception error) {
                System.out.println(error.getMessage());
                runningCron.stop();
                mainInterface.enableStartButton();
            }
        } else {
            JOptionPane.showMessageDialog(null,"Please, complete all fields!");
        }
    }

    public void onCancel(MainInterface mainInterface, VpnConfigs mainInterfaceConfigs) {
        saveConfigs(mainInterfaceConfigs);
        mainInterface.enableStartButton();
        if(runningCron != null) runningCron.stop();
        mainInterface.dispose();
        System.exit(0);
    }
}
