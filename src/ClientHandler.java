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
            int messageType = 0;
            int length = 0;
            String line = "";
            String body = "";
            while ( !(line = clientInput.readLine()).isEmpty()) {
                System.out.println(line);
                messageType = checkMessageType(line);
                if (line.startsWith("Content-Length: ")) {
                    int index = line.indexOf(':') + 1;
                    String len = line.substring(index).trim();
                    length = Integer.parseInt(len);
                }
            }
            body = getBody(length);
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

    public int checkMessageType(String message){
        if(message.contentEquals("GET")){
            return 1;
        }else if(message.contentEquals("POST")){
            return 2;
        }else if(message.contentEquals("HEAD")){
            return 3;
        }
        return 0;
    }

    public String getBody(int length){
        int read;
        StringBuilder body = new StringBuilder();
        if(length > 0) {
            try {
                while ((read = clientInput.read()) != -1) {
                    body.append((char) read);
                    if (body.length() == length) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return body.toString();
    }

}
