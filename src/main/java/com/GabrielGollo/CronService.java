package com.GabrielGollo;

import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class CronService {
    Process runningBuilder;
    Thread thread1;
    public void StartVpnAuth(VpnConfigs configs){
        try{
            String username = configs.username();
            String password = configs.password();
            String vpnHost = configs.vpnHost();
            String opVpnFileLocation = configs.opVpnFileLocation();
            String secretOTP = configs.secretOtp();

            connectToVpn(username, password, opVpnFileLocation, secretOTP);
            PingManager pingManager1 = new PingManager(vpnHost);
            boolean isAlive = pingManager1.ping();
            System.out.println("Connection is "+isAlive);

            if(!isAlive){
                connectToVpn(username, password,  opVpnFileLocation, secretOTP);
            }

            System.out.println("Initializing job");

        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }

    private void connectToVpn(String username, String password, String opVpnFileLocation, String secretOTP){
        Totp otp = new Totp();
        String token = otp.getOTPCode(secretOTP);
        System.out.println(token);

        String passwordWithToken = password+token;
        System.out.println("otp: "+token+" user: "+username+" pass: "+password+" secret: "+secretOTP);

        try{
            File location = new File("");
            String fileLocation = location.getAbsolutePath()+ "\\src\\ovpn_user.txt";
            String commandLine = "openvpn --config \""+opVpnFileLocation+"\" --auth-user-pass \""+fileLocation+"\"";

            File locationForCmd = new File(location.getAbsolutePath()+"\\");

            PrintWriter writer = new PrintWriter(fileLocation, StandardCharsets.UTF_8);
            writer.println(username);
            writer.print(passwordWithToken);
            writer.close();
            if(runningBuilder == null) {
                thread1 = new Thread(() -> {
                    try {
                        runningBuilder = ProcessTerminal.runCommand(locationForCmd, commandLine);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                });

                thread1.start();
                thread1.wait();
            }


        } catch (Exception e) {
            System.out.println("Erro ao tentar abrir conexão");
            System.out.println(e.getMessage());
        }

    }
}
