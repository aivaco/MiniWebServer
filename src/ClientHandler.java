import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{

    private Socket clientSocket;
    // Takes the input from the client
    private BufferedReader clientInput;
    private PrintWriter serverOutput;
    private Message message;
    private String returnMessage;


    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.serverOutput = new PrintWriter(socket.getOutputStream(), true);
        this.clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        boolean firstLine = true;
        int messageType = 0;
        int length = 0;
        String line = "";
        StringBuilder header = new StringBuilder();
        String body = "";
        String acceptType = "";
        System.out.println("\nClient accepted");

        try {
            while ( !(line = clientInput.readLine()).isEmpty()) {
                if(firstLine) {
                    messageType = checkMessageType(line);
                    firstLine = false;
                }
                if(line.contains("Accept:")){
                    acceptType = line;
                }
                header.append(line);
                if (line.startsWith("Content-Length: ")) {
                    int index = line.indexOf(':') + 1;
                    String len = line.substring(index).trim();
                    length = Integer.parseInt(len);
                }
                System.out.println(line);
            }
            body = getBody(length);
            System.out.println(body);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Process the message
        message = new Message(messageType,header.toString(),body,acceptType);
        returnMessage = message.processMessage();
        serverOutput.print(returnMessage);
        serverOutput.flush();

        try {
            clientInput.close();
            serverOutput.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public int checkMessageType(String message){
        if(message.contains("POST")){
            return 1;
        }else if(message.contains("GET")){
            return 2;
        }else if(message.contains("HEAD")){
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
