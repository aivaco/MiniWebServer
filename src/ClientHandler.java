import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    Socket clientSocket;
    // Takes the input from the client
    private BufferedReader clientInput;
    private PrintWriter serverOutput;


    public ClientHandler(Socket socket) throws IOException {
        clientSocket = socket;
        serverOutput = new PrintWriter(socket.getOutputStream(), true);
        clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        System.out.println("\nClient accepted");
        try {
            String test = "";

            while ( !(test = clientInput.readLine()).isEmpty()) {
                System.out.println(test);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            clientInput.close();
            serverOutput.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
