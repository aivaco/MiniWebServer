import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Basically this class is in charge of administrate the message which has been received and generates an answer.
 */
public class Message {

    private Type type;                  // The message's type.
    private State state;                // The request's state.
    private String header;              // The message's header.
    private String acceptType;          // It is used to compare the header accept file type and the file type.
    private String path;                // The path to a require file.
    private String body;                // The content's message.
    private OnFile file;                // It is used to manipulates a file.

    public Message(int type, String header, String acceptType , String path, String body){
        switch(type){
            case 1:
                this.type = this.type.POST;
                break;
            case 2:
                this.type = this.type.GET;
                break;
            case 3:
                this.type = this.type.HEAD;
                break;
            default:
                this.type = this.type.ERROR;
        }
        this.header = header;
        this.acceptType = acceptType;
        this.path = path.substring(1).trim();
        this.body = body;
        this.file = new OnFile();

    }

    /***
     * Verifies the message operation and calls the respective function.
     * @return
     */
    public String processMessage(){
        String returnMessage = "";
        switch(this.type){
            case POST:
                returnMessage = checkFileExistence(false);
                break;
            case GET:
                returnMessage = checkFileExistence(false);
                break;
            case HEAD:
                returnMessage = checkFileExistence(true);
                break;
            default:
                this.state = State.Error501;
                returnMessage = getMessage("", false);
        }

        return returnMessage;
    }

    /***
     * Verifies if a file exists in the server.
     * @param isHEAD
     * @return
     */
    private String checkFileExistence(boolean isHEAD) {
        String returnMessage;
        if(this.file.fileExists(this.path)){
            //The file exists

            List<String> types;
            types = processFileTypes();
            //Checks if the file type is accepted
            if (!checkFileType(types,path)) {
                // The file type is not accepted so it returns an error
                this.state = State.Error406;
            } else {
                // The file type is accepted
                this.state = State.Valid;
            }
            returnMessage = getMessage(this.path, isHEAD);
        }else{
            //The file doesn't exists

            this.state = State.Error404;
            returnMessage = getMessage(path, isHEAD);
        }
        return returnMessage;
    }

    /***
     * Process the message and return an appropriate answer.
     * @param path
     * @param isHEAD
     * @return
     */
    public String getMessage(String path, boolean isHEAD){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.ENGLISH);
        String returnMessage = "";
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-6"));

        switch(this.state){
            case Error404:               //Resource doesn't exist.
                String error404Message = "Error 404: File Not Found.\n";
                returnMessage =
                        "HTTP/1.1 404 Not Found\n" +
                                "Content-Type: text/html; charset=utf-8\n" +
                                "Server: JavaSimulated/1.0\n"+
                                "Date: "+ dateFormat.format(date)+"\n" +
                                "Connection: close\n" +
                                "Content-Length: "+error404Message.length()+"\n" +
                                "\n" +
                                error404Message;
                break;
            case Error406:               //Invalid format
                String error406Message = "Error 406: Not Acceptable.\n";
                returnMessage =
                        "HTTP/1.1 406 Not Acceptable\n" +
                                "Content-Type: text/html; charset=utf-8\n" +
                                "Server: JavaSimulated/1.0\n"+
                                "Date: "+ dateFormat.format(date)+"\n" +
                                "Connection: close\n" +
                                "Content-Length: "+error406Message.length()+"\n" +
                                "\n" +
                                error406Message;
                break;
            case Error501:               //Invalid method
                String error501Message = "Error 501: Not Implemented.\n";
                returnMessage =
                        "HTTP/1.1 501 Not Implemented\n" +
                                "Content-Type: text/html; charset=utf-8\n" +
                                "Server: JavaSimulated/1.0\n"+
                                "Date: "+ dateFormat.format(date)+"\n" +
                                "Connection: close\n" +
                                "Content-Length: "+error501Message.length()+"\n" +
                                "\n" +
                                error501Message;
                break;
            case Valid:
                String content = new String(this.file.readBytesFromFile(path));
                returnMessage =
                        "HTTP/1.1 200 OK\n" +
                                "Content-Type: text/html; charset=utf-8\n" +
                                "Server: JavaSimulated/1.0\n"+
                                "Date: "+ dateFormat.format(date)+"\n" +
                                "Connection: close\n" +
                                "Content-Length: "+content.length()+"\n" +
                                "\n";
                if (!isHEAD) {
                    returnMessage+=content;
                }
                //data = getDocument(path);
                break;
        }
        return returnMessage;
    }

    /***
     *
     * @return
     */
    public List<String> processFileTypes(){
        String accept = this.acceptType.substring(8,this.acceptType.length());
        String[] temporal = accept.split(";");
        List<String> types = new ArrayList<>();
        String temp = "";
        for(String temp2 : temporal){
            types.addAll(Arrays.asList(temp2.split(",")));
        }

        for(int i = 0; i<types.size(); ++i){
            temp = types.get(i);
            if(temp.contains("q=")){
                types.remove(temp);
            }
        }

        return types;
    }

    /***
     * Checks the file type if is valid or not.
     * @param type
     * @param path
     * @return
     */
    public boolean checkFileType(List<String> type,String path){
        String extension = this.file.checkMIME(path);
        if(type.contains("*/*")){
            return true;
        }else if(type.contains(extension)){
            return true;
        }
        return false;
    }

    /***
     * Facilitates the differentiation between the requests.
     */
    public enum Type {
        POST,
        GET,
        HEAD,
        ERROR
    }

    /***
     * It is used to manage the possible answer message case.
     */
    private enum State {
        Valid,
        Error404,
        Error406,
        Error501
    }
}
