package src.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientReceiverTCP implements Runnable {
    BufferedReader in;

    public ClientReceiverTCP(Socket so){
        try {
            this.in = new BufferedReader(new InputStreamReader(so.getInputStream()));
        } catch (IOException e){}
    };

    public void run(){
        try {
            while (true) {
                String message = in.readLine();
                if (message != null) {
                    System.out.println(message);
                } else {
                    System.out.println("Connection to chat server lost!");
                    return;
                }

            }
        } catch (IOException e){}
    }
}