import java.net.*;
import java.io.*;

public class HTTPEcho {
    public static void main( String[] args) {

        if (args.length != 1) {
            System.err.println("Bad Argument - port needed");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        try (
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();
                PrintWriter out =
                        new PrintWriter(clientSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
        ) {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                out.println(inputLine);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

    }
}

