package src.chat.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws IOException {

        final ExecutorService service = Executors.newCachedThreadPool();

        System.out.println("Chat client started");

        String serverName = "localhost";
        int serverPort = 12345;
        Socket socketTCP = null;
        DatagramSocket socketUDP = null;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Register your nick: ");
        String nick = scanner.nextLine();


        try {
            socketTCP = new Socket(serverName, serverPort);

            /////
            socketUDP = new DatagramSocket(socketTCP.getLocalPort());

            InetAddress address = InetAddress.getByName("localhost");
            byte[] sendBuffer = "Ping Java Udp".getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, address, serverPort);
            socketUDP.send(sendPacket);
            /////

            PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);
            service.submit(new ClientReceiverTCP(socketTCP));

            out.println(nick);
            System.out.println("CONNECTED TO CHAT");

            while(true) {
                String message = scanner.nextLine();
                out.println(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socketTCP != null){
                socketTCP.close();
                socketUDP.close();
            }
        }
    }

}
