/**
 * It is used to manage an entry for the log.
 */
public class EntryLog {
    private String method;
    private String timeStamp;
    private String server;
    private String refer;
    private String url;
    private String datos;

    public EntryLog(){

    }

    public String getMethod() {
        return method;
    }

    public void setMethod(int method) {

        switch (method) {
            case 1:
                this.method = "POST";
                break;
            case 2:
                this.method = "GET";
                break;
            case 3:
                this.method = "HEAD";
                break;
            default:
                this.method = "UNDEFINED";
        }
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDatos() {
        return datos;
    }

    public void setDatos(String datos) {
        this.datos = datos;
    }
}
