package server;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Класс для работы с сервером.
 * @author Nikolay Nikolskiy
 * @version     %I% %G%
 * @since       1.0
 */
public class BankServer {
    private static int port = 8080;
    private static HttpServer server;

    /**
     * Метод для запуска Сервера
     */
    public static void start() {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            System.out.println("server started at " + port);
            server.createContext("/", new Handlers.RoutHandler());
            server.createContext("/cards/userID/", new Handlers.ClientsCardsListGetHandler());
            server.createContext("/cards/balance/userID/", new Handlers.BalanceStatusGetHandler());
            server.createContext("/cards/issue/", new Handlers.CardIssuePostHandler());
            server.createContext("/cards/account/deposit/", new Handlers.DepositPostHandler());
            server.setExecutor(null);
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Метод для остановки работы Сервера
     */
    public static void stop() {
        System.out.println("server stoped at " + port);
        server.stop(0);
    }
}
