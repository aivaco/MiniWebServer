import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class ClientHandler implements Runnable{

    private Socket clientSocket;                    // Takes the input from the client
    private BufferedReader clientInput;             // It is  used to read the message.
    private PrintWriter serverOutput;               // Used to write an answer to the client.
    private Message message;                        // Contains the message which was sent to the server.
    private String returnMessage;                   // The answer.

    private EntryLog entryLog;                      // Used to store the actions of the server.
    private static Semaphore mutex = new Semaphore(1);

    public ClientHandler(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.serverOutput = new PrintWriter(socket.getOutputStream(), true);
        this.clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.entryLog = new EntryLog();
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
        String refererLog = "";
        String urlLog = "";
        String dataLog = "";
        System.out.println("\nClient accepted");

        try {
            //Starts to read the message.
            while ( !(line = clientInput.readLine()).isEmpty()) {
                if(firstLine) {
                    messageType = checkMessageType(line);
                    urlLog = line.substring(line.indexOf("/"), line.indexOf("HTTP")-1);
                    // Checks if the request is a GET
                    if (messageType == 2) {
                        // Is GET so the data is in the in the URL
                        int dataIndex = urlLog.indexOf("?");
                        //Checks if has URL has any parameters
                        if(dataIndex != -1) {
                            // Has parameters
                            dataLog = urlLog.substring(dataIndex+1);
                            dataLog.replaceAll("&", "&amp;");
                            urlLog = urlLog.substring(0, dataIndex);
                        }
                    }
                    firstLine = false;
                }
                else if(line.contains("Accept:")){
                    acceptType = line;
                }
                else if(line.contains("Referer:")) {
                    int indexURL = StringUtils.ordinalIndexOf(line, "/", 3);
                    refererLog = line.substring(line.indexOf("://") + 3, indexURL);
                }
                else if (line.startsWith("Content-Length: ")) {
                    int index = line.indexOf(':') + 1;
                    String len = line.substring(index).trim();
                    length = Integer.parseInt(len);
                }
                header.append(line);
                System.out.println(line);
            }
            body = getBody(length);
            System.out.println(body);
            // Checks if the request is POST
            if (messageType == 1) {
                // Is POST so the data is in the body
                dataLog = body.replaceAll("&", "&amp;");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Process the message
        message = new Message(messageType, header.toString(), acceptType, urlLog, body);
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

        entryLog.setMethod(messageType);
        entryLog.setServer("localhost");
        entryLog.setRefer(refererLog);
        entryLog.setUrl(urlLog);
        entryLog.setDatos(dataLog);

        try {
            mutex.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        OnFile onFile = new OnFile();
        onFile.writeLineInFile( "<tr>\n<td>"+entryLog.getMethod()+"</td>\n<td>"+entryLog.getTimeStamp()+"</td>\n<td>"+entryLog.getServer()+"</td>\n<td>"+entryLog.getRefer()+"</td>\n<td>"+entryLog.getUrl()+"</td>\n<td>"+entryLog.getDatos()+"</td>\n</tr>", "bitacora.html");
        mutex.release();
    }

    /***
     * Checks the method required by the client.
     * @param message
     * @return
     */
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

    /***
     * Starts to read from the last line of the header (for this case: Accept-Length:#) the quantity of bytes which have been indicated in it.
     * @param length
     * @return
     */
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
