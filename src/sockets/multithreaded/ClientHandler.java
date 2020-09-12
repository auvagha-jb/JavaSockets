/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sockets.multithreaded;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.ServerApp;
import utils.SocketsUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class ClientHandler extends Thread {

    private final Socket clientSocket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String msgReceived;

    public ClientHandler(Socket client) {
        this.clientSocket = client;
        try {
            inputStream = new DataInputStream(clientSocket.getInputStream());
            outputStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public DataOutputStream getOutputStream() {
        return outputStream;
    }

    public void openServerSocket() {
        System.out.printf("Selected Port SocketServer.initSocket %s\n", clientSocket.getLocalPort());

        try {
            //Receiving new nessages
            while (true) {
                msgReceived = inputStream.readUTF();//Receive the incoming message
                ServerApp.setReceivedMessage(msgReceived);
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SocketsUtil.closeClientSocket(inputStream, outputStream, clientSocket);
        }
    }

    @Override
    public void run() {
        openServerSocket();
    }
}
