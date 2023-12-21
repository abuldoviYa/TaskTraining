import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.concurrent.Executors;

public class SimpleProxyServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new ProxyHandler());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("Proxy server started on port 8080");
    }

    static class ProxyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

            try {
                Headers headers = exchange.getResponseHeaders();
                headers.set("Access-Control-Allow-Origin", "*"); // Adjust the origin accordingly
                headers.set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                headers.set("Access-Control-Allow-Headers", "Content-Type, Authorization");

                if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, -1);
                    return;
                }

                String requestMethod = exchange.getRequestMethod();
                String apiUrl = exchange.getRequestURI().getPath()+ "?" + exchange.getRequestURI().getQuery();;

                System.out.println(apiUrl.substring(1));
                URL url = new URL(apiUrl.substring(1));

                HttpURLConnection apiConnection = (HttpURLConnection) url.openConnection();
                apiConnection.setRequestMethod(requestMethod);


                apiConnection.setRequestProperty("Content-Type", exchange.getRequestHeaders().getFirst("Content-Type"));

                int responseCode = apiConnection.getResponseCode();
                InputStream apiInputStream = responseCode < 400 ? apiConnection.getInputStream() : apiConnection.getErrorStream();

                exchange.sendResponseHeaders(responseCode, 0);

                try (OutputStream outputStream = exchange.getResponseBody()) {
                    byte[] buffer = new byte[8192];
                    int length;
                    while ((length = apiInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }
                }

                apiInputStream.close();
                apiConnection.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
