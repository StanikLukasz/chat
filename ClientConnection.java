import java.io.*;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

class ClientConnection implements Runnable {
    Socket socket;
    String name;
    BufferedReader in;
    BlockingQueue<String> messageQueue;

    public ClientConnection(Socket s, BlockingQueue<String> q) {
        this.socket = s;
        this.messageQueue = q;
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
                    messageQueue.add(name + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
