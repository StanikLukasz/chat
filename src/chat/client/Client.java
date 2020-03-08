package src.chat.client;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
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
            socketUDP = new DatagramSocket(socketTCP.getLocalPort());

            PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);
            service.submit(new ClientReceiverTCP(socketTCP));
            service.submit(new ClientReceiverUDP(socketUDP));

            out.println(nick);
            System.out.println("CONNECTED TO CHAT");

            File file = new File("./src/chat/client/art.txt");
            byte[] messageUDP = Files.readAllBytes(file.toPath());

            byte[] headerUDP = (nick + " sends:\n").getBytes();
            byte[] result = Arrays.copyOf(headerUDP, headerUDP.length + messageUDP.length);
            System.arraycopy(messageUDP, 0, result, headerUDP.length, messageUDP.length);

            while(true) {
                String message = scanner.nextLine();
                if (message.equals("U")){
                    InetAddress address = InetAddress.getByName("localhost");
                    DatagramPacket sendPacket = new DatagramPacket(result, result.length, address, serverPort);
                    socketUDP.send(sendPacket);
                } else {
                    out.println(message);
                }
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
