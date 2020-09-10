package sockets.update;

import sockets.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.ClientApp;
import utils.FormUtil;
import utils.SocketsUtil;

public class SocketClient {

    private static Socket socket;
    private static DataInputStream inputStream;
    private static DataOutputStream outputStream;
    private static String msgReceived;
    private static boolean connectionStatus;

    public static void initSocket(int portNumber) {
        connectionStatus = true;

        SocketsUtil.setSelectedPort(portNumber);
        System.out.printf("Selected Port SocketClient.initSocket %s\n", SocketsUtil.getSelectedPort());

        try {
            socket = new Socket(SocketsUtil.SERVER_IP, SocketsUtil.STATIC_PORT);//Static
//            socket = new Socket(SocketsUtil.SERVER_IP, SocketsUtil.getSelectedPort());//Dynamic
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            connectionStatus = false;
            FormUtil.errorFeedback("Connection Error", "Wrong port number entered. Please try again");
            //Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean isConnected() {
        return connectionStatus;
    }

    public static void openClientSocket() {
        try {
            //Receiving new nessages
            while (true) {
                msgReceived = inputStream.readUTF();//Receive the incoming message
                ClientApp.setReceivedMessage(msgReceived);
                System.out.printf("Server says: %s\n", msgReceived);
            }

        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SocketsUtil.closeClientSocket(inputStream, outputStream, socket);
        }
    }

    public static void writeToServer(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
