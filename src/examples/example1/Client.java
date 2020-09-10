package examples.example1;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) {
        try {
            Scanner userSc = new Scanner(System.in);
            Socket socket = new Socket("127.0.0.1",1342);
            Scanner serverSc = new Scanner(socket.getInputStream());//Gets input stream from the server
            System.out.println("Enter a number");

            int number = userSc.nextInt();//Number that we are to send to the server
            PrintStream p = new PrintStream(socket.getOutputStream());
            p.println(number);

            int temp = serverSc.nextInt();
            System.out.println(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
