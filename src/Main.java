import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {

        WebServer webServer = new WebServer();
        webServer.start(8080);
    }
}
