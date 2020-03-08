package src.chat.client;


import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {


    public static void main(String[] args) throws IOException {

        System.setProperty("java.net.preferIPv4Stack", "true");
        final ExecutorService service = Executors.newCachedThreadPool();

        System.out.println("Chat client started");

        String serverName = "localhost";
        int serverPort = 12345;
        Socket socketTCP = null;
        DatagramSocket socketUDP = null;
        MulticastSocket multicastSocketUDP = null;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Register your nick: ");
        String nick = scanner.nextLine();


        try {
            socketTCP = new Socket(serverName, serverPort);
            socketUDP = new DatagramSocket(socketTCP.getLocalPort());
            multicastSocketUDP = new MulticastSocket(12346);

//            NetworkInterface netInt = NetworkInterface.getByIndex(1);
//            Enumeration<InetAddress> inetAddresses = netInt.getInetAddresses();
//            InetAddress inetAddr = inetAddresses.nextElement();
//            System.out.println(inetAddr);
            multicastSocketUDP.setInterface(InetAddress.getByName("localhost"));
//            multicastSocketUDP.setNetworkInterface(netInt);
//            multicastSocketUDP.setLoopbackMode(true);


            PrintWriter out = new PrintWriter(socketTCP.getOutputStream(), true);
            service.submit(new ClientReceiverTCP(socketTCP));
            service.submit(new ClientReceiverUDP(socketUDP));
            service.submit(new ClientReceiverUDP(multicastSocketUDP));

            out.println(nick);
            System.out.println("CONNECTED TO CHAT");

            File file = new File("./src/chat/client/art.txt");
            byte[] messageUDP = Files.readAllBytes(file.toPath());
            byte[] headerUDP = (nick + " sends:\n").getBytes();
            byte[] result = Arrays.copyOf(headerUDP, headerUDP.length + messageUDP.length);
            System.arraycopy(messageUDP, 0, result, headerUDP.length, messageUDP.length);
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket sendPacket = new DatagramPacket(result, result.length, address, serverPort);
            DatagramPacket sendPacketMulticast = new DatagramPacket(result, result.length, address, 12346);


            while(true) {
                String message = scanner.nextLine();
                if (message.equals("U")) {
                    socketUDP.send(sendPacket);
                } else if (message.equals("M")) {
                    multicastSocketUDP.send(sendPacketMulticast);
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
                multicastSocketUDP.close();
            }
        }
    }

}
