import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConcHTTPAsk {
    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = serverSetup(args);
            System.out.println("Server listening on port " + serverSocket.getLocalPort());

            while (true) {
                MyRunnable myRunnable = new MyRunnable(serverSocket.accept());
                Thread thread = new Thread(myRunnable);
                thread.start();
            }
        } catch (IOException e) {
            System.out.println("Server Exception: " + e.getMessage());
        }

    }

    private static ServerSocket serverSetup(String[] args) throws IOException {

        int serverPort = Integer.parseInt(args[0]);

        if (serverPort < 1 || serverPort >= 65535) {
            System.err.println("Invalid server port (1 - 65535)");
            System.exit(1);
        }
        ServerSocket serverSocket = new ServerSocket(serverPort);

        return serverSocket;
    }
}
