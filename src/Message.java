import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by aivan on 15/09/2017.
 */
public class Message {

   private Type type;
   private String header;
   private String body;
   private OnFile file;

    public Message(int type, String header, String body){
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
        String path;
        String[] url_parts;
        String returnMessage = "";
        List<String> data;
        //System.out.println(returnMessage);
        path = header.substring(header.indexOf("GET")+5, header.indexOf("HTTP"));
        path.trim();
        url_parts = path.split("/");
        if(this.file.fileExists(url_parts)){
           data = getDocument(path);
        }else{
            returnMessage = getMessage();
        }
        //System.out.println(path);
        return returnMessage;
    }

    public String getMessage(){
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm:ss z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT-6"));
        String error404;
        String error404Message = "Error 404: file not found.\n";
        error404 = "HTTP/1.1 404 Not Found\n" +
                "Content-Type: text/html; charset=utf-8\n" +
                "Server: JavaSimulated/1.0\n"+
                "Date: "+ dateFormat.format(date)+"\n" +
                "Connection: close\n" +
                "Content-Length: "+error404Message.length()+"\n" +
                "\n" +
                error404Message;

        return error404;
    }

    public void checkFileType(String path){
        String ext = path.substring(path.indexOf('.')+1,path.length());

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
}
