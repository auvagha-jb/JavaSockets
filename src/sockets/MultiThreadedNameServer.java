package sockets;

import examples.example2.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import utils.SocketsUtil;

public class MultiThreadedNameServer {

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    //Init the number of threads
    private static final ExecutorService pool = Executors.newFixedThreadPool(4);

    public static void main(String[] args) throws IOException {
        ServerSocket listener = new ServerSocket(SocketsUtil.STATIC_PORT);

        while (true) {
            System.out.println("[SERVER] Waiting for client to connect");

            Socket client = listener.accept();//Establishes connection
            System.out.println("[SERVER] Client connected");
            ClientHandler clientThread = new ClientHandler(client);
            clients.add(clientThread);
            pool.execute(clientThread);
        }
    }

}
