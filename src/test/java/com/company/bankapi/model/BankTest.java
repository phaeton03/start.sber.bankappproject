package com.company.bankapi.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.*;

class BankTest {
    List<BankCard> cardsList;
    List<BankAccount> accountsList;

    @BeforeEach
    void init() {
        cardsList = new ArrayList<>(
                Arrays.asList(
                        new BankCard("1111", 1, 10),
                        new BankCard("2222", 1, 10)));
        accountsList = Arrays.asList(
                new BankAccount(10, BigDecimal.ZERO, 1),
                new BankAccount(11, BigDecimal.ZERO, 1));
    }
    @Test
    void issueCard() {
    }
    @Test
    void testIdPositiveNumber(){
        Client client = new Client(1l, "Mike", cardsList, accountsList);
        assertTrue(client.getUniqueID() > 0);
    }
    @Test
    void testClientsCardsSize() {
        Client client = new Client(1l, "Mike", cardsList, accountsList);
        assertTrue(client.getCards().size() == 2);
    }
    @Test
    void testClientsCardsNull() {
        Client client = new Client(1l, "Mike", accountsList);
        assertNull(client.getCards());
    }
    @Test
    void testClientsCardsType() {
        Client client = new Client(1l, "Mike", cardsList, accountsList);
        assertTrue(client.getCards().get(0) instanceof BankCard);
    }

    @Test
    void testDepositIncrement() {
        BigDecimal amount = new BigDecimal(101);
        BankAccount bankAccount = new BankAccount(10, BigDecimal.ONE,1);
        bankAccount.deposit(amount);
        BigDecimal testResult = new BigDecimal(102);
        assertTrue(bankAccount.getBalance().equals(testResult));
    }
    @Test
    void testCheckBalanceAccountsSize() {
        Client client = new Client(1l, "Mike", cardsList, accountsList);
        List<BankAccount> accounts = client.getAccounts();
        assertTrue(accounts.size() == 2);
    }
    @Test
    void testCheckBalanceAccountsNotNull() {
        Map<Long, BigDecimal> mapAccount = new HashMap<>();
        Client client = new Client(1l, "Mike", cardsList, accountsList);
        List<BankAccount> accounts = client.getAccounts();
        assertNotNull(accounts);
     }

    @AfterEach
    void clean(){
        cardsList = new ArrayList<>();
        accountsList = new ArrayList<>();
    }
}