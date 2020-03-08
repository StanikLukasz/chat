import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

class ClientInput implements Runnable {
    Scanner scanner;
    PrintWriter out;

    public ClientInput(Scanner sc, Socket so){
        this.scanner = sc;
        try {
            this.out = new PrintWriter(so.getOutputStream(), true);
        } catch (IOException e){}
    };

    public void run(){
        while(true){
            System.out.print("> ");
            String message = scanner.nextLine();
            out.println(message);
        }
    }

    public void register(String name){
        out.println(name);
    }
}