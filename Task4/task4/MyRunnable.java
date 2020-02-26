import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyRunnable implements Runnable {

    private Socket clientSocket;

    public MyRunnable(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        System.out.println("A New Thread Is Running");
        try {
            clientSocket.setSoTimeout(5000);

            // init streams
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            // reset values
            String port = null;
            String hostname = null;
            String data = null;


            // Reading client request
            String getRequest = in.readLine();
            // Parsing client request
            String[] indexedRequest = getRequest.split("[ /?=&]");

            // valid request check
            boolean isAsk = (indexedRequest[2].equals("ask")) ? true : false;
            boolean isHTTP = (indexedRequest[indexedRequest.length - 2].equals("HTTP")) ? true : false;
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
                    out.writeBytes("HTTP/1.1 200 OK\r\n\r\n" + TCPClient.askServer(hostname, intPort, data));
                } catch (Exception e) {
                    out.writeBytes("HTTP/1.1 404 Not Found\r\n\r\n");
                }
            } else
                out.writeBytes("HTTP/1.1 400 Bad Request\r\n\r\n");

            // Closing client socket and streams
            clientSocket.close();
            in.close();
            out.close();
        } catch (IOException | NullPointerException e) {
            System.err.println("Exception while handling client request: " + e.getMessage());
        }
    }
}
