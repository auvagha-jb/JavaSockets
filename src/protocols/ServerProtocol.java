/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocols;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sockets.server.ClientHandler;
import sockets.server.SocketServer;
import utils.SocketsUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class ServerProtocol {

    private static final int REQUEST_TOY_ID = 0;
    private static final int REQUEST_TOY_INFO = 1;
    private static final int REQUEST_MANUFACTURER_INFO = 2;
    private static final int REQUEST_ALL_TOYS = 3;
    private static final int REQUEST_THANK_YOU = 4;
    private static final int SEND_CONNECTION_STATUS = 5;
    private static ClientHandler clientHandler;

    private static HashMap<Integer, String> actions = new HashMap(
            Map.ofEntries(
                    Map.entry(REQUEST_TOY_ID, "Request toy identification details (code,name)"),
                    Map.entry(REQUEST_TOY_INFO, "Request toy information"),
                    Map.entry(REQUEST_MANUFACTURER_INFO, "Request manufacturer information"),
                    Map.entry(REQUEST_ALL_TOYS, "Request all toys"),
                    Map.entry(REQUEST_THANK_YOU, "Request thank you message"),
                    Map.entry(SEND_CONNECTION_STATUS, "Send connection status")
            )
    );

    public static void setClientHandler(ClientHandler handler) {
        clientHandler = handler;
    }

    public String[] getActionStrings() {
        ArrayList<String> actionArrayList = new ArrayList<>();

        actions.entrySet().forEach(set -> {
            actionArrayList.add(set.getValue());
        });

        return actionArrayList.toArray(new String[0]);
    }

    public void processRequest(int actionIndex) {

        String newMessage;

        if (actionIndex == SEND_CONNECTION_STATUS) {
            newMessage = SocketServer.getConnectionStatus();
        } else {
            newMessage = actions.get(actionIndex).replace("Request", "Send");
        }

        writeToClient(newMessage);
        System.out.printf("[SERVER]: %s", newMessage);
    }

    public void writeToClient(String message){

        try {
            clientHandler.getOutputStream().writeUTF(message);
        } catch (IOException ex) {
            Logger.getLogger(SocketServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
