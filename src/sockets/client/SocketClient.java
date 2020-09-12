package sockets.client;

import sockets.server.SingleSocketServer;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ui.ClientApp;
import utils.FormUtil;
import utils.SocketsUtil;

public final class SocketClient extends Thread {

    private Socket socket;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private final int portNumber;
    private String msgReceived;
    private static boolean connectionStatus;
    
    
    public SocketClient(int portNumber) {
        this.portNumber = portNumber;
        initSocket();
    }
    
    public void initSocket() {
        SocketsUtil.setSelectedPort(portNumber);
        System.out.printf("Selected Port SocketClient.initSocket %s\n", SocketsUtil.getSelectedPort());

        try {
//            socket = new Socket(SocketsUtil.SERVER_IP, SocketsUtil.STATIC_PORT);//Static
            socket = new Socket(SocketsUtil.SERVER_IP, portNumber);//Dynamic
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
            connectionStatus = true;
        } catch (IOException ex) {
            FormUtil.errorFeedback("Connection Error", "Wrong port number entered. Please try again");
            //Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
            connectionStatus = false;
        } catch(IllegalArgumentException e) {
            FormUtil.errorFeedback(FormUtil.WRONG_FORMAT_TITLE, "The port entered is out of the accepted range");
            connectionStatus = false;
        }
    }

    public boolean isConnected() {
        return connectionStatus;
    }

    public void openClientSocket() {
        //Initialize socket with the port input
        
        try {
            //Receiving new nessages
            while (true) {
                msgReceived = inputStream.readUTF();//Receive the incoming message
                ClientApp.setReceivedMessage(msgReceived);
                System.out.printf("Server says: %s\n", msgReceived);
            }

        } catch (IOException ex) {
            Logger.getLogger(SingleSocketServer.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            SocketsUtil.closeClientSocket(inputStream, outputStream, socket);
        }
    }

    public void writeToServer(String message) {
        try {
            outputStream.writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static boolean getConnectionStatus() { 
        return connectionStatus;
    }
    
    @Override
    public void run() {
        openClientSocket();
    }    
}
