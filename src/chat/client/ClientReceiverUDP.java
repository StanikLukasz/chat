package src.chat.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.Set;

public class ClientReceiverUDP implements Runnable {
    byte[] receiveBuffer;
    DatagramSocket socket;

    public ClientReceiverUDP(DatagramSocket s){
        this.receiveBuffer = new byte[1024];
        this.socket = s;
    }


    public void run(){
        try {
            while (true) {
                Arrays.fill(receiveBuffer, (byte) 0);
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                String msg = new String(receivePacket.getData());
                System.out.println(msg.substring(0, receivePacket.getLength()));
            }
        } catch (IOException e){}
    }
}
