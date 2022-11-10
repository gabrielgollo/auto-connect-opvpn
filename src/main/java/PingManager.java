import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class PingManager {
    InetAddress inetSolver;
    PingManager(String host) throws UnknownHostException {
        inetSolver = InetAddress.getByName(host);
    }

    public boolean ping() throws IOException {
        return inetSolver.isReachable(100);
    }
}
