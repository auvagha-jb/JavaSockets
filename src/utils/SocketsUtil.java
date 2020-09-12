/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sockets.client.SocketClient;

/**
 *
 * @author Jerry Auvagha
 */
public class SocketsUtil {

    private static int selectedPort = 0;
    public static final String SERVER_IP = "127.0.0.1";
    public static final int STATIC_PORT = 625;

    private static final int MIN_PORT = 80;
    private static final int MAX_PORT = 8080;
    
    public static final int WAITING = 0;
    public static final int CONNECTED = 1;
    public static final String[] connectionStatusMsg = {"[WAITING]", "[CONNECTED]"};
    
    private static final int[] reservedPorts = {
        80, 443, //Apache
        3306, //MySQL
        21, 14147, //FileZilla
        25, 79, 105, 106, 110, 143, 2224, //Mercury
        8005, 8009, 8080 //Tomcat
    };

    public static void setSelectedPort() {
        selectedPort = chooseRandomPort();
    }
    
    public static void setSelectedPort(int port) {
        selectedPort = port;
    }
    

    public static int getSelectedPort() {
        return selectedPort;
    }
    
    public static String getConnectionStatus() {
        String status = SocketClient.getConnectionStatus() ? "[CONNECTED]" : "[DISCONNECTED]";
        return String.format("%s: PORT %s", status, selectedPort);
    }
    
    /**
     * Choose random port between min and max values e. between 80 and 8080
     *
     * @return port int
     */
    private static int chooseRandomPort() {
        int port = 0;

        do {
            port = RandomUtil.generateRandomInt(MIN_PORT, MAX_PORT);
        } while (contains(port));

        return port;
    }

    private static boolean contains(int selectedPort) {
        boolean containsValue = false;

        for (int port : reservedPorts) {
            if (selectedPort == port) {
                containsValue = true;
                break;
            } 
        }

        return containsValue;
    }

    public static void closeClientSocket(InputStream inputStream, OutputStream outputStream, Socket socket) {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(SocketClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//    public static void main(String[] args) {
//        setSelectedPort();
//        System.out.printf("Selected port: %s\n", getSelectedPort());
//    }

}
