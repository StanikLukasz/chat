package src.chat.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Set;

public class ServerTransceiverUDP implements Runnable {
    byte[] receiveBuffer;
    DatagramSocket socket;
    Set<ServerClientConnectionTCP> clients;

    public ServerTransceiverUDP(DatagramSocket s, Set<ServerClientConnectionTCP> c){
        this.receiveBuffer = new byte[1024];
        this.socket = s;
        this.clients = c;
    }


    public void run(){
        try {
            while (true) {
                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
                System.out.println(msg);
                byte[] sendBuffer = msg.getBytes();
                for (ServerClientConnectionTCP client: clients) {
                    DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, client.socket.getRemoteSocketAddress());
                    socket.send(sendPacket);
                }
            }
        } catch (IOException e){}
    }
}
