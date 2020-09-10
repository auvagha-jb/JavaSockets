package examples.example2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private static final String SERVER_IP = "127.0.0.1";

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, ServerController.NAME_SERVER_PORT);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));//Reads from the socket
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));//Reads user input

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        try {
            while (true) {
                System.out.println(">");
                String command = keyboard.readLine();

                if (command.equals("quit")) {
                    break;//Exit condition
                }
                out.println(command);

                String serverResponse = input.readLine();
                System.out.printf("[SERVER RESPONSE], %s", serverResponse);
            }
        } finally {
            
            socket.close();
            //        System.exit(0);

        }

    }
}
