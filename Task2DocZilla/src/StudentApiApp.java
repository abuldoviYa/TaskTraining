import DAO.StudentDAO;
import com.sun.net.httpserver.HttpServer;
import handler.StudentHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class StudentApiApp {

    public void start() {
        StudentDAO.createTableIfNotExists();

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/students", new StudentHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
