/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.ServerApp;
import utils.SocketsUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class SocketServer {

    //Socket functionality -> These were static
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static String msgReceived;
    
    private static void initSocket() {
        SocketsUtil.setSelectedPort();//Initialize port number with random port number dynamically
        
        try {
//            serverSocket = new ServerSocket(SocketsUtil.getSelectedPort());//Dynamic
            serverSocket = new ServerSocket(SocketsUtil.STATIC_PORT);
            socket= serverSocket.accept();
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void openServerSocket() {
        initSocket();//inititalize socket variables
        System.out.printf("Selected Port SocketServer.initSocket %s\n", SocketsUtil.getSelectedPort());
           
        try {
            //Receiving new nessages
            while (true) {
                msgReceived = inputStream.readUTF();//Receive the incoming message
                ServerApp.setReceivedMessage(msgReceived);
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SocketsUtil.closeClientSocket(inputStream, outputStream, socket);
        }
    }
    
    public static void writeToClient(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
