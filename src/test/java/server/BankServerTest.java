package server;

import com.company.bankapi.model.Bank;
import com.company.bankapi.model.BankAccount;
import com.company.bankapi.model.BankCard;
import com.company.bankapi.model.Client;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BankServerTest {
    @BeforeAll
    static void init() {
        Bank bank = new Bank();
        bank.createAndFillTables();
    }
    Bank bank = new Bank();
    @Test
    void testGetClientsCards() throws Exception {
        BankServer.start();
        long userID = 2;
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/cards/userID/2")).GET().build();
        Client client = bank.getClient(userID);
        List<BankCard> cards = client.getCards();
        ObjectMapper objectMapper = new ObjectMapper();
        String cardsAsString = objectMapper.writeValueAsString(cards);
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(),cardsAsString);
        BankServer.stop();
    }
    @Test
    void testGetBalance() throws Exception {
        BankServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/cards/balance/userID/2")).GET().build();
        String test = "{\"20\":1,\"21\":1}";
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), test);
        BankServer.stop();
    }
    @Test
    void testPostDeposit() throws Exception {
        BankServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(new URI("http://localhost:8080/cards/account/deposit/")).
                headers("Content-Type", "text/plain;charset=UTF-8").
                POST(HttpRequest.BodyPublishers.ofString("{\"11\":100}")).build();
        String test = "{\"11\":100}";
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), test);
        BankServer.stop();
    }
    @Test
    void testPostIssueCard() throws Exception {
        BankServer.start();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().
                uri(new URI("http://localhost:8080/cards/issue/")).
                headers("Content-Type", "text/plain;charset=UTF-8").POST(HttpRequest.
                BodyPublishers.ofString("{\"0000 1111 0000 1111\":11}")).build();
        String test = "{\"0000 1111 0000 1111\":11}";
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(response.body(), test);
        BankServer.stop();
    }
}