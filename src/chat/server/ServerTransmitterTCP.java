package src.chat.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

public class ServerTransmitterTCP implements Runnable {
    Set<ServerClientConnectionTCP> clients;
    BlockingQueue<String> messageQueue;

    public ServerTransmitterTCP(Set<ServerClientConnectionTCP> c, BlockingQueue<String> m){
        this.clients = c;
        this.messageQueue = m;
    }

   public void run(){
        try {
            while (true) {
                String message = messageQueue.take();
                System.out.println(message);
                for (ServerClientConnectionTCP client : clients) {
                    try {
                        Socket clientSocket = client.socket;
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                        out.println(message);
                    } catch (IOException e) {
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
   }
}
