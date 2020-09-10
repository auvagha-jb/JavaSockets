package examples.example1;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket s1 = new ServerSocket(1342);
            Socket ss = s1.accept();//Accepts the connection -> Blocks execution of the rest

            Scanner sc = new Scanner(ss.getInputStream());
            int number = sc.nextInt();

            int temp = number * 2;
            PrintStream p = new PrintStream(ss.getOutputStream());//Send result back to client
            p.println(temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
