/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.multithreaded;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocols.ServerProtocol;
import sockets.SocketClient;
import ui.ServerApp;
import utils.FormUtil;
import utils.SocketsUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class SocketServer{

    private static ArrayList<ClientHandler> clientList = new ArrayList<>();
    ClientHandler clientHandler;
    ExecutorService pool = Executors.newFixedThreadPool(4);
    private static boolean connectionStatus;
    
    public void initSocket() {
        //Generate random port
        SocketsUtil.setSelectedPort();
        ServerSocket serverSocket;

        try {
            serverSocket = new ServerSocket(SocketsUtil.STATIC_PORT);
            while (serverSocket.isBound()) {
                System.out.println("[SERVER] Waiting for client to connect");

                Socket client = serverSocket.accept();//Establishes connection 
                System.out.println("[SERVER] Client connected");
                clientHandler = new ClientHandler(client);
                ServerProtocol.setClientHandler(clientHandler);
                clientHandler.start();
                clientList.add(clientHandler);
                pool.execute(clientHandler);
                
                //Set the connection status
                connectionStatus = true;
            }

        } catch (IOException ex) {
            connectionStatus = false;
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            FormUtil.errorFeedback("Server Error", "Multiple servers running terminate previous processes and try again");
        }
    }

    
     public static String getConnectionStatus() {
        String status = connectionStatus ? "[CONNECTED]" : "[DISCONNECTED]";
        return String.format("%s: PORT %s", status, SocketsUtil.getSelectedPort());
    }
    
}
