import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    // Socket that will keep listening to incoming requests from the clients
    private ServerSocket serverListener;
    // Socket to establish connection with the client
    private Socket socket;


    public void start(int port) throws IOException {
        // Starts the server socket
        serverListener = new ServerSocket(port);
        System.out.println("Server started");
        System.out.println("\nWaiting for a client");

        while (true) {
            // Accepts the client
            socket = serverListener.accept();
            new ClientHandler(socket).run();
        }

    }


}
