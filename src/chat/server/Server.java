package src.chat.server;

import java.io.IOException;
import java.net.*;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

public class Server {

    public static void main(String[] args) throws IOException {

        final ExecutorService chat = Executors.newCachedThreadPool();
        int portNumber = 12345;

        ServerSocket serverSocketTCP = null;
        DatagramSocket serverSocketUDP = null;

        Set<ServerClientConnectionTCP> clients = new HashSet<>();
        BlockingQueue<String> messageQueue= new LinkedBlockingQueue<>();

        System.out.println("Chat server started at port " + portNumber);

        try {
            serverSocketTCP = new ServerSocket(portNumber);
            serverSocketUDP = new DatagramSocket(portNumber);

            chat.submit(new ServerTransmitterTCP(clients, messageQueue));
            chat.submit(new ServerTransceiverUDP(serverSocketUDP, clients));



            while(true){
                Socket clientSocket = serverSocketTCP.accept();

                ServerClientConnectionTCP client = new ServerClientConnectionTCP(clientSocket, messageQueue, clients);
                clients.add(client);
                chat.submit(client);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (serverSocketTCP != null){
                serverSocketTCP.close();
                serverSocketUDP.close();
            }
        }
    }
}
