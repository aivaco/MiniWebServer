import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by aivan on 15/09/2017.
 */
public class Message {

   private Type type;
   private State state;
   private String header;
   private String body;
   private OnFile file;
   private String acceptType;

    public Message(int type, String header, String body, String acceptType){
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
        this.body = body;
        this.acceptType = acceptType;
        this.file = new OnFile();

    }

    public String processMessage(){
         String returnMessage = "";
        switch(this.type){
            case POST:
                returnMessage = proccessPost();
                break;
            case GET:
                returnMessage = proccessGet();
                break;
            case HEAD:
                returnMessage = proccessHead();
                break;
            default:
                this.type = this.type.ERROR;
        }

        return returnMessage;
    }

    public String proccessPost(){

        return "";
    }


    public String proccessGet(){
        int acceptLine = 0;
        String path;
        String returnMessage;
        String extension;
        String[] url_parts;
        List<String> types;
        path = header.substring(header.indexOf("GET")+5, header.indexOf("HTTP"));
        path.trim();
        url_parts = path.split("/");
        types = processFileTypes();

        if(this.file.fileExists(url_parts)){

            if (!checkFileType(types,path)) {
                this.state = State.Error406;
            } else {
                this.state = State.Valid;
            }
            returnMessage = getMessage(path);


        }else{
            this.state = State.Eror404;
            returnMessage = getMessage(path);
        }
        //System.out.println(path);
        return returnMessage;
    }

    public String getMessage(String path){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.ENGLISH);
        String returnMessage = "";
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-6"));

        switch(this.state){
            case Eror404:               //Resource doesn't exist.
                String error404Message = "Error 404: File Not Found.\n";
                returnMessage = "HTTP/1.1 404 Not Found\n" +
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
                returnMessage = "HTTP/1.1 406 Not Acceptable\n" +
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
                returnMessage = "HTTP/1.1 501 Not Implemented\n" +
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
                returnMessage = "HTTP/1.1 200 OK\n" +
                        "Content-Type: text/html; charset=utf-8\n" +
                        "Server: JavaSimulated/1.0\n"+
                        "Date: "+ dateFormat.format(date)+"\n" +
                        "Connection: close\n" +
                        "Content-Length: "+content.length()+"\n" +
                        "\n"+
                        content;
                //data = getDocument(path);
                break;
        }
        return returnMessage;
    }

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

    public boolean checkFileType(List<String> type,String path){
        String extension = this.file.checkMIME(path);
        if(type.contains("*/*")){
            return true;
        }else if(type.contains(extension)){
            return true;
        }
        return false;
    }


    public List<String> getDocument(String path){
        List<String> data = new ArrayList<String>();
        data = file.readFile(path);

        return data;
    }


    public String proccessHead(){

        return "";
    }

    public enum Type {
        POST,
        GET,
        HEAD,
        ERROR
    }

    private enum State {
        Valid,
        Eror404,
        Error406,
        Error501
    }
}
