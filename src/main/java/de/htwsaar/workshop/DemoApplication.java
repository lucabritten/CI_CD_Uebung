package de.htwsaar.workshop;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public final class DemoApplication {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "8080"));
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        GreetingService greetings = new GreetingService();
        server.createContext("/health", exchange -> reply(exchange, 200, "OK"));
        server.createContext("/hello", exchange -> reply(exchange, 200, greetings.greet("htw saar")));
        server.start();
        System.out.println("Demo running at http://localhost:" + port + "/hello");
    }
    private static void reply(HttpExchange exchange, int status, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var stream = exchange.getResponseBody()) { stream.write(bytes); }
    }
}
