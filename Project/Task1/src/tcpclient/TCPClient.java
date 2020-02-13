package tcpclient;

import java.net.*;
import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TCPClient {

    private static int BUFFERSIZE = 1024;

    public static String askServer(String hostname, int port, String ToServer) throws IOException {

        if (ToServer == null) {
            return askServer(hostname, port);
        } else {
            Socket socket = new Socket(hostname, port);
            byte[] fromUserBuffer = ToServer.getBytes();
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            String response;

            // Send ToServer msg on output stream
            msgToServer(fromUserBuffer, outputStream);

            // Receive msg from the server
            response = msgFromServer(inputStream, socket);

            socket.close();

            return response;

        }
    }

    public static String askServer(String hostname, int port) throws IOException {

        Socket socket = new Socket(hostname, port);
        InputStream inputStream = socket.getInputStream();
        String response;

        response = msgFromServer(inputStream, socket);

        socket.close();

        return response;
    }

    private static String msgFromServer(InputStream inputStream, Socket socket) throws IOException {
        int fromServerLength;
        byte[] fromServerBuffer = new byte[BUFFERSIZE];
        ByteArrayOutputStream byteArrayFromServer = new ByteArrayOutputStream();
        String responseFromServer;

        while (true) {
            try {
                /*
                the socket timer is to be set to the maximum time the
                client wants to wait for a response from the server. This may result in
                a premature timeout if the value is set to a time less than
                the response time of the server
                */
                socket.setSoTimeout(2000);
                fromServerLength = inputStream.read(fromServerBuffer);
            } catch(Exception e) {
                fromServerLength = -1;
            }

            if (fromServerLength == -1) {
                break;
            }
            byteArrayFromServer.write(fromServerBuffer, 0, fromServerLength);
        }
        responseFromServer = byteArrayFromServer.toString();

        return responseFromServer;
    }

    private static void msgToServer(byte[] fromUserBuffer, OutputStream outputStream) throws IOException {
        int fromUserLength = fromUserBuffer.length;
        outputStream.write(fromUserBuffer, 0, fromUserLength);
        outputStream.write('\n');

    }
}

