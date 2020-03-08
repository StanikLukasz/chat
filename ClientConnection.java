import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

class ClientConnection implements Runnable {
    Socket socket;
    String name;
    BufferedReader in;
    BlockingQueue<String> messageQueue;
    Set<ClientConnection> clients;

    public ClientConnection(Socket s, BlockingQueue<String> q, Set<ClientConnection> c) {
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
