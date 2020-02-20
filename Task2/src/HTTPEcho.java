import java.net.*;
import java.io.*;

public class HTTPEcho {
    public static void main( String[] args) throws IOException {

        int port = Integer.parseInt(args[0]);
        ServerSocket serverSocket = new ServerSocket(port);

        // Accepting incoming clients
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(2000);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                StringBuilder sb = new StringBuilder();
                String s = "HTTP/1.1 200 OK\r\n\r\n";
                sb.append(s);

                while(s.length() != 0 && (s = in.readLine()) != null) {
                    sb.append(s + "\r\n");
                }
                out.writeBytes(sb.toString());
                clientSocket.close();

                in.close();
                out.close();

            } catch (IOException ioe) {
                System.err.println("Exception while handling client request: " + ioe.getMessage());
            }
        }

    }
}

