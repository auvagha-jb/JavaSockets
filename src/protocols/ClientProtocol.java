/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocols;

import database.Manufacturers;
import database.Toys;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import models.Manufacturer;
import models.Toy;
import sockets.SocketClient;
import ui.ClientApp;
import utils.FormUtil;

/**
 *
 * @author Jerry Auvagha
 */
public class ClientProtocol {

    public static final int INSERT_NEW_TOY = 10;
    public static final int INSERT_NEW_MANUFACTURER = 11;
    public static final int SEND_TOY_ID = 0;//direct
    public static final int SEND_TOY_INFO = 1;//direct
    public static final int SEND_MANUFACTURER_INFO = 2;//direct
    public static final int SEND_ALL_TOYS = 3;
    public static final int SEND_THANK_YOU = 4;
    public static final int REQUEST_CONNECTION_STATUS = 5;
    public static final String REQUEST_CONNECTION_STRING = "Send me the connection sts";
    private String previousMessageStream;

    private final Toys toysTable;
    private final Manufacturers manufacturersTable;

    private static HashMap<Integer, String> actions = new HashMap(
            Map.ofEntries(Map.entry(SEND_TOY_ID, "Send toy identification details (code,name)"),
                    Map.entry(SEND_TOY_INFO, "Send toy information"),
                    Map.entry(SEND_MANUFACTURER_INFO, "Send manufacturer information"),
                    Map.entry(SEND_ALL_TOYS, "Send all toys"),
                    Map.entry(SEND_THANK_YOU, "Send thank you message"),
                    Map.entry(REQUEST_CONNECTION_STATUS, "Request connection status")
            )
    );

    public ClientProtocol() {
        this.toysTable = new Toys();
        this.manufacturersTable = new Manufacturers();
    }

    public void setPreviousMessageStream(String previousMessageStream) {
        this.previousMessageStream = previousMessageStream;
    }

    public String[] getActionStrings() {
        ArrayList<String> actionArrayList = new ArrayList<>();

        actions.entrySet().forEach(set -> {
            actionArrayList.add(set.getValue());
        });

        return actionArrayList.toArray(new String[0]);
    }

    /*For inserting new item (toy or maufacturer)*/
    public void insertNewToy(String message, Map<String, String> formInput) {

        try {

            Toy toy = new Toy(formInput);
            int toyCode = toysTable.insert(toy);
            toy.setToyCode(toyCode);

            sendMessage(String.format("%s: %s", message, toy.toyInformationToString()));
            ClientApp.updateToyComboBoxes(toy);

        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ensure price and batch number are numbers", FormUtil.WRONG_FORMAT_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }

    
   public void insertNewManufacturer(String message, Map<String, String> formInput) {
       try {

            Manufacturer man = new Manufacturer(formInput);
            int manufacturerId = manufacturersTable.insert(man);
            man.setManufacturerId(manufacturerId);

            sendMessage(String.format("%s: %s", message, man.manufacturerInfoToString()));
            ClientApp.updateManufacturerComboBox(man);

        } catch (NumberFormatException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Ensure zip code is a number", FormUtil.WRONG_FORMAT_TITLE, JOptionPane.ERROR_MESSAGE);
        }
   }

    public void sendMessage(String newMessage) {
        SocketClient.writeToServer(String.format("%s\n %s", previousMessageStream, newMessage));
        System.out.printf("%s\n", newMessage);
    }

}
