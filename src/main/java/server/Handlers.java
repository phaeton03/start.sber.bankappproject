package server;

import com.company.bankapi.model.Bank;
import com.company.bankapi.model.BankCard;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.math.BigDecimal;
import java.net.URI;

import java.util.*;

/**
 * Общий Класс содержащий внутренние классы для работы с HTTP запросами
 * @author      Nikolay Nikolskiy
 * @version     %I% %G%
 * @since       1.0
 */
public class Handlers {
    private static Bank bank = new Bank();

    /**
     * Внутренний класс отправляеи сообщение Server starts successfully if you see this message
     * когда отправляешь запрос типа  "/"
     */
    static class RoutHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String response = "<h1>Server starts successfully if you see this message</h1>";
            //rCode - http код ответа
            exchange.sendResponseHeaders(200, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.flush();
            os.close();
        }
    }

    /**
     * Внутренний класс для просмотра всех карт клиента
     */
    static class ClientsCardsListGetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //parse request
            URI requestedURI = exchange.getRequestURI();
            String query = requestedURI.toString();
            String pattern = "/cards/userID/\\d+";
            long userID;
            if(query.matches(pattern)) {
                String[] queryArr = query.split("/");
                userID = Long.parseLong(queryArr[queryArr.length - 1]);
                List<BankCard> cards = bank.lookClientsCards(userID);
            //send response
            ObjectMapper objectMapper = new ObjectMapper();
            String cardsAsString = objectMapper.writeValueAsString(cards);
            sendResponse(200, cardsAsString, exchange);
            } else {
            sendResponse(404, "Invalid request in URL", exchange);
            }
        }
    }

    /**
     * Внутренний класс для проверки баланса по клиента по всем его счетам
     */
    static class BalanceStatusGetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //parse request
            URI requestedURI = exchange.getRequestURI();
            String query = requestedURI.toString();
            String pattern = "/cards/balance/userID/\\d+";
            long userID;
            if(query.matches(pattern)) {
                String[] queryArr = query.split("/");
                userID = Long.parseLong(queryArr[queryArr.length - 1]);
                Map<Long, BigDecimal> balance = bank.checkBalance(userID);
                ObjectMapper objectMapper = new ObjectMapper();
                String balanceAsString = objectMapper.writeValueAsString(balance);
                sendResponse(200, balanceAsString, exchange);
            } else {
                sendResponse(404, "Invalid request in URL", exchange);
            }
        }
    }

    /**
     * Внутренний класс для выпуска новой карты
     */
    static class CardIssuePostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestedURI = exchange.getRequestURI();
            String query = requestedURI.toString();
            String pattern = "/cards/issue/";
            if(query.matches(pattern)) {
                ObjectMapper objectMapper = new ObjectMapper();
                String response = readResponse(exchange);
                Map<String, Long> params = objectMapper.readValue(response, new TypeReference<>() {});
                for(Map.Entry<String, Long> entry : params.entrySet()) {
                    bank.issueCard(entry.getKey(), entry.getValue());
                }
                sendResponse(200, response, exchange);
           } else {
                sendResponse(404, "Invalid request in URL", exchange);
            }
        }
    }

    /**
     * Внутренний класс для пополнения счета по его номеру
     */
    static class DepositPostHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            URI requestedURI = exchange.getRequestURI();
            String query = requestedURI.toString();
            String pattern = "/cards/account/deposit/";
            if(query.matches(pattern)) {
                ObjectMapper objectMapper = new ObjectMapper();
                String response = readResponse(exchange);
                Map<Long, BigDecimal> params = objectMapper.readValue(response, new TypeReference<Map<Long, BigDecimal>>() {});
                for(Map.Entry<Long, BigDecimal> entry : params.entrySet()) {
                    bank.deposit(entry.getKey(), entry.getValue());
                }
                sendResponse(200, response, exchange);
            } else {
                sendResponse(404, "Invalid request in URL", exchange);
            }
        }
    }

    /**
     * Вспомогательный Метод для чтения HTTP response
     * @param exchange
     * @return - String
     * @throws IOException
     */
    private static String readResponse(HttpExchange exchange) throws IOException {
        InputStreamReader isr = new InputStreamReader(exchange.getRequestBody());
        BufferedReader br = new BufferedReader(isr);
        StringBuilder responseBuilder = new StringBuilder();
        String inputStr;
        while((inputStr = br.readLine()) != null) {
            responseBuilder.append(inputStr);
        }
        return new String(responseBuilder);
    }

    /**
     * Вспомогательный метод для отправки HTTP response
     * @param rcode - код http ответов
     * @param response - ответ от Сервера клиенту
     * @param exchange - объект HttpExchange
     * @throws IOException
     */
    private static void sendResponse(int rcode, String response, HttpExchange exchange) throws IOException {
        exchange.sendResponseHeaders(rcode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }
}
