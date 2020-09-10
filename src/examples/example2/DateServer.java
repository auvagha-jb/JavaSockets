package examples.example2;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * A service that provides the date to the client
 */
public class DateServer {
    private static final int PORT = 9090;

    public static void main(String[] args) throws IOException {
        String dateString = new Date().toString();

        System.out.println("[SERVER] Waiting for client to connect");
        ServerSocket listener = new ServerSocket(PORT);
        Socket client = listener.accept();//Establishes connection
        System.out.println("[SERVER] Client connected");

        //Making a print writer to send information to the client
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        out.println(dateString);
        System.out.println("[SERVER] Sent date. Closing connection...");

        client.close();
        listener.close();
    }
}
