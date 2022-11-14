import java.io.File;
import java.io.PrintWriter;

public class CronService {
    public static void StartVpnAuth(String username, String password, String vpnHost, String opVpnFileLocation, String secretOTP){
        try{
            connectToVpn(username, password, vpnHost, opVpnFileLocation, secretOTP);
            PingManager pingManager1 = new PingManager(vpnHost);
            boolean isAlive = pingManager1.ping();
            System.out.println("Connection is "+isAlive);

            if(!isAlive){
                connectToVpn(username, password, vpnHost, opVpnFileLocation, secretOTP);
            }

            System.out.println("Initializing job");

        } catch (Exception error) {
            System.out.println(error.getMessage());
        }
    }

    static void connectToVpn(String username, String password, String vpnHost, String opVpnFileLocation, String secretOTP){
        Totp otp = new Totp();
        String token = otp.getOTPCode(secretOTP);
        System.out.println(token);

        String passwordWithToken = password+token;
        System.out.println("otp: "+token+" user: "+username+" pass: "+password+" secret: "+secretOTP);

        try{
            File location = new File("");
            String fileLocation = location.getAbsolutePath()+ "\\ovpn_user.txt";
            String commandLine = "openvpn --config \""+opVpnFileLocation+"\" --auth-user-pass \""+fileLocation+"\"";

            File locationForCmd = new File(location.getAbsolutePath()+"\\");

            PrintWriter writer = new PrintWriter(fileLocation, "UTF-8");
            writer.println(username);
            writer.print(password+token);
            writer.close();
            ProcessTerminal.runCommand(locationForCmd, commandLine);

        } catch (Exception e) {
            System.out.println("Erro ao tentar abrir conexão");
            System.out.println(e.getMessage());
        }

    }
}
