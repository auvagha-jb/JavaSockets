package examples.example2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class NameServer {
    
    public static void main(String[] args) throws IOException {
        System.out.println("[SERVER] Waiting for client to connect");
        ServerSocket listener = new ServerSocket(ServerController.NAME_SERVER_PORT);

        Socket client = listener.accept();//Establishes connection
        System.out.println("[SERVER] Client connected");

        //A print writer to send information to the client
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        //A buffer reader to be able to receive information from the client
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        try {
            while (true) {
                String request = in.readLine();

                if (request.contains("name")) {
                    out.println(ServerController.getRandom());
                } else {
                    out.println("Commnd not found. Type 'send name' to get a random name");
                }
            }
        } finally {
            System.out.println("[SERVER] Closing connection...");
            out.close();
            in.close();
        }


    }

}
