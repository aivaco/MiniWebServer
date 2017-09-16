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
            int length = 0;
            StringBuilder body = new StringBuilder();
            while ( !(test = clientInput.readLine()).isEmpty()) {
                System.out.println(test);
                if (test.startsWith("Content-Length: ")) {
                    int index = test.indexOf(':') + 1;
                    String len = test.substring(index).trim();
                    length = Integer.parseInt(len);
                }
            }
            //Reads the body
            if (length > 0) {
                int read;
                while ((read = clientInput.read()) != -1) {
                    body.append((char) read);
                    if (body.length() == length) {
                        break;
                    }
                }
            }
            System.out.println(body);

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
