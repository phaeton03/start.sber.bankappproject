package dao;

import com.company.bankapi.model.BankAccount;
import com.company.bankapi.model.BankCard;
import com.company.bankapi.model.Client;
import dao.dbService.DBService;
import dao.executor.Executor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientDAOTest {
    Executor executor;
    BankAccountDAOInt accountDAO;
    BankCardDAOInt bankCardDAO;
    ClientDAOInt clientDAO;
    @BeforeEach
    void init() throws Exception {
        executor = new Executor(DBService.getH2Connection());
        accountDAO = new BankAccountDAO(executor);
        bankCardDAO = new BankCardDAO(executor);
        clientDAO = new ClientDAO(executor);
        clientDAO.createTable();
        accountDAO.createTable();
        bankCardDAO.createTable();
    }
    @Test
    void getAllClients() {
    }

    @Test
    void testGetClient() throws Exception {
        long id = 1l;
        String nameTest = "Nick";
        String insert =
                String.format("INSERT into clients values ('%d', '%s')", id, nameTest);
        executor.execUpdate(insert);
        String query = String.format("SELECT * FROM clients WHERE id=%d", id);
        Client client = executor.execQuery(query,
                r -> {
                    r.next();
                    String name = r.getString("client_name");
                    List<BankAccount> accounts = new ArrayList<>();
                    List<BankCard> cards = new ArrayList<>();
                    if(!cards.isEmpty())
                        return new Client(id, name, cards, accounts);
                    else
                        return new Client(id, name, accounts);
                });
            assertEquals(client.getUniqueID(), id);
            assertEquals(client.getName(), nameTest);
            assertTrue(client instanceof Client);
        }

    @Test
    void testAddClient() throws Exception {
        long id = 1l;
        String name = "Nick";
        String insert =
                String.format("INSERT into clients values ('%d', '%s')", id, name);
        executor.execUpdate(insert);
        Client client = clientDAO.getClient(id);
        assertEquals(client.getName(), name);
        assertEquals(client.getUniqueID(), id);
    }
    @Test
    void testAddAccount() throws Exception {
        long accountID = 10L;
        long clientID = 1L;
        BigDecimal balance = BigDecimal.ZERO;
        long id = 1l;
        String name = "Nick";
        String insert =
                String.format("INSERT into clients values ('%d', '%s')", id, name);
        executor.execUpdate(insert);
        String query = String.format("INSERT into Accounts values ('%d', '%d', '%s')", accountID, clientID, balance);
        executor.execUpdate(query);
        BankAccount account = accountDAO.getAccount(accountID);
        assertEquals(account.getAccountNumber(), accountID);
        assertEquals(account.getClientID(), clientID);
        assertEquals(account.getBalance(), balance);
    }
    @Test
    void testAddCard() throws Exception {
        long accountID = 10L;
        long clientID = 1L;
        String sn = "1000 1111 2222 2121";
        BigDecimal balance = BigDecimal.ZERO;
        String name = "Nick";
        String insert =
                String.format("INSERT into clients values ('%d', '%s')", clientID, name);
        executor.execUpdate(insert);
        String query = String.format("INSERT into Accounts values ('%d', '%d', '%s')", accountID, clientID, balance);
        executor.execUpdate(query);
        String insertCard =
                String.format("INSERT into Cards values ('%d', '%d', '%s')", clientID, accountID, sn);
        executor.execUpdate(insertCard);
        List<BankCard> cards = bankCardDAO.getClientsCards(clientID);
        assertEquals(cards.size(), 1);
        assertEquals(cards.get(0).getUniqueSN(), sn);
    }
    @AfterEach
    void clear() throws Exception {
        executor.execUpdate("DROP table cards");
        executor.execUpdate("DROP table accounts");
        executor.execUpdate("DROP table clients");
        accountDAO = null;
        bankCardDAO = null;
        clientDAO = null;
    }
}