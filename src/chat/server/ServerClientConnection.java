package src.chat.server;

import java.io.*;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

class ServerClientConnection implements Runnable {
    Socket socket;
    String name;
    BufferedReader in;
    BlockingQueue<String> messageQueue;
    Set<ServerClientConnection> clients;

    public ServerClientConnection(Socket s, BlockingQueue<String> q, Set<ServerClientConnection> c) {
        this.socket = s;
        this.messageQueue = q;
        this.clients = c;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.name = in.readLine();
        } catch (IOException e) {}
        messageQueue.add(name + " just joined!");
    }
    public void run() {
            try {
                while(true) {
                    String message = in.readLine();
                    if (message != null) {
                        messageQueue.add(name + ": " + message);
                    } else {
                        clients.remove(this);
                        messageQueue.add(name + " left chat");
                        return;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
