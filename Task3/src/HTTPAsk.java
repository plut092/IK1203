import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/*
Implement a web server "HTTPAsk":
1. Open Server with @param port
Loop:
    2. Accept connection to client
    3. read request
    4. parse request
    5. evaluate parsed request, this uses TCPClient (task 1)
    6. respond to client with the evaluated request

@param port number for HTTPAsk server
*/

public class HTTPAsk {
    public static void main(String[] args) {

        // check if port argument provided
        if (args.length < 1) {
            System.err.println("Port number not provided");
            System.exit(1);
        }

        int serverPort = Integer.parseInt(args[0]);
        if (serverPort <= 0 || serverPort > 65535) {
            System.err.println("Invalid port number (1-65535)");
            System.exit(1);
        }

        // init variables
        String status_OK = "HTTP/1.1 200 OK\r\n\r\n";
        String status_Not_found = "HTTP/1.1 404 Not Found\r\n\r\n";
        String status_Bad_request = "HTTP/1.1 400 Not Found\r\n\r\n";

        String getRequest;
        String port;
        String hostname;
        String data;
        boolean isAsk;
        boolean isHTTP;

        // Open Server Socket
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }

        while (true) {
            try {
                // Open Client Socket
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(5000);

                // init streams
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

                // reset values
                port = null;
                hostname = null;
                data = null;


                // Reading client request
                getRequest = in.readLine();
                // Parsing client request
                String[] indexedRequest = getRequest.split("[ /?=&]");

                // valid request check
                isAsk = (indexedRequest[2].equals("ask")) ? true : false;
                isHTTP = (indexedRequest[indexedRequest.length - 2].equals("HTTP")) ? true : false;
                if (isAsk && isHTTP) {
                    for (int i = 1; i < indexedRequest.length - 1; i++) {
                        switch (indexedRequest[i - 1]) {
                            case "hostname":
                            hostname = indexedRequest[i];
                            case "port":
                            port = indexedRequest[i];
                            case "string":
                            data = indexedRequest[i];
                        }
                    }
                    int intPort = Integer.parseInt(port);

                    // Response to client request
                    try {
                        out.writeBytes(status_OK + TCPClient.askServer(hostname, intPort, data));
                    } catch (Exception e) {
                        out.writeBytes(status_Not_found);
                    }
                } else
                out.writeBytes(status_Bad_request);

                // Closing client socket and streams
                clientSocket.close();
                in.close();
                out.close();

            } catch (IOException | NullPointerException e) {
                System.err.println("Exception while handling client request: " + e.getMessage());
            }
        }
    }
}








