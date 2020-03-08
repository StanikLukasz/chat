import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    public static void main(String[] args) throws IOException {

        final ExecutorService service = Executors.newCachedThreadPool();
        System.out.println("JAVA TCP CLIENT");
        String serverName = "localhost";
        int serverPort = 12345;
        Socket socket = null;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Register your nick: ");
        String nick = scanner.nextLine();

        try {
            socket = new Socket(serverName, serverPort);

            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            service.submit(new ChatReader(socket));

            out.println(nick);
            System.out.println("CONNECTED TO CHAT");

            while(true) {
                String message = scanner.nextLine();
                out.println(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socket != null){
                socket.close();
            }
        }
    }

}
