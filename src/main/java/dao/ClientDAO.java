package dao;

import com.company.bankapi.model.BankAccount;
import com.company.bankapi.model.BankCard;
import com.company.bankapi.model.Client;
import dao.executor.Executor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements ClientDAOInt {
    private Executor executor;
    private BankAccountDAOInt accountDAO;
    private BankCardDAOInt bankCardDAO;
    public ClientDAO(Executor executor) {
        this.executor = executor;
        accountDAO = new BankAccountDAO(executor);
        bankCardDAO = new BankCardDAO(executor);
    }
    @Override
    public List<Client> getAllClients() throws SQLException {
        return executor.execQuery("SELECT * FROM clients", r -> {
            List<Client> clients = new ArrayList<>();
            while(r.next())
                clients.add(getClient(r.getLong("id")));
            return clients;
        });
    }

    @Override
    public Client getClient(long id) throws SQLException {
        String query = String.format("SELECT * FROM clients WHERE id=%d", id);
        return executor.execQuery(query,
                r -> {
                     r.next();
                     String name = r.getString("client_name");
                     List<BankAccount> accounts = accountDAO.getClientsAccounts(id);
                     List<BankCard> cards = bankCardDAO.getClientsCards(id);
                     if(!cards.isEmpty())
                     return new Client(id, name, cards, accounts);
                     else
                         return new Client(id, name, accounts);

                });
    }
 //Протестировать на количество входящих параметров
   @Override
    public void insertClient(long id, String name) throws SQLException {
        String insert =
                String.format("INSERT into clients values ('%d', '%s')", id, name);
        executor.execUpdate(insert);
    }
    /*
        //При удалении пользователя из БД нужно реализовать также удаление всех Карт и Аккаунтов привязанных к данному Клиенту
        @Override
        public void delete(Client client) throws SQLException {
            executor.execUpdate("DELETE from clients WHERE id =" + client.getUniqueID());
        }

        @Override
        public void update() throws SQLException {

        }
    // Подумать как дропнуть таблицу так чтобы дропнулись и все связанные с ней
        @Override
        public void dropTable() throws SQLException {
            executor.execUpdate("DROP table clients");
        } */
    //Определиться с количеством необходимых полей для класса Client
    @Override
    public void createTable() throws SQLException {
        executor.execUpdate("CREATE table if not exists clients (id bigint, client_name varchar(256), PRIMARY KEY (id))");
    }
}
