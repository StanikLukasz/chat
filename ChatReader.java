import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ChatReader implements Runnable {
    BufferedReader in;

    public ChatReader(Socket so){
        try {
            this.in = new BufferedReader(new InputStreamReader(so.getInputStream()));
        } catch (IOException e){}
    };

    public void run(){
        try {
            while (true) {
                String message = in.readLine();
                System.out.println(message);
            }
        } catch (IOException e){}
    }
}