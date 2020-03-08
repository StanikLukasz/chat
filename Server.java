import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
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

        Set<ClientConnection> clients = new HashSet<>();
        BlockingQueue<String> messageQueue= new LinkedBlockingQueue<>();
        chat.submit(new Sender(clients, messageQueue));

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(portNumber);
            while(true){
                Socket clientSocket = serverSocket.accept();

                ClientConnection client = new ClientConnection(clientSocket, messageQueue, clients);
                clients.add(client);
                chat.submit(client);





                // read msg, send response


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }
}
