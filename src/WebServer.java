import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {

    // Socket that will keep listening to incoming requests from the clients
    private ServerSocket serverListener;
    // Socket to establish connection with the client
    private Socket socket;
    OnFile onFile;

    public void start(int port) throws IOException {

        onFile = new OnFile();
        onFile.createFile("bitacora","html");
        onFile.writeLineInFile(
                "<meta charset=\"UTF-8\">\n<table summary=\"Bitácora\" border=\"1\"><caption align=\"bottom\">Bitácora de solicitudes al MiniServidor</caption>\n" +
                "<tbody>\n" +
                "<tr><th scope=\"col\">Método</th><th scope=\"col\">Estampilla de Tiempo</th><th scope=\"col\">Servidor</th><th scope=\"col\">Refiere</th><th scope=\"col\">URL</th><th scope=\"col\">Datos</th></tr>\n", "bitacora.html");

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
