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
        System.out.println("Chat server started at port " + portNumber);

        Set<ServerClientConnection> clients = new HashSet<>();
        BlockingQueue<String> messageQueue= new LinkedBlockingQueue<>();
        chat.submit(new ServerTransmitterTCP(clients, messageQueue));

        ServerSocket serverSocketTCP = null;
        DatagramSocket serverSocketUDP = null;

        try {
            serverSocketTCP = new ServerSocket(portNumber);
            serverSocketUDP = new DatagramSocket(portNumber);

            byte[] receiveBuffer = new byte[1024];

            while(true){
                Arrays.fill(receiveBuffer, (byte)0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                serverSocketUDP.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                System.out.println("received msg: " + msg);

                Socket clientSocket = serverSocketTCP.accept();

                ServerClientConnection client = new ServerClientConnection(clientSocket, messageQueue, clients);
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
