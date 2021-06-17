package dao;

import com.company.bankapi.model.BankCard;
import dao.executor.Executor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankCardDAO implements BankCardDAOInt {
    private Executor executor;

    public BankCardDAO(Executor executor) {
        this.executor = executor;
    }

    /**
     * Возвращаем все карты нужного нам клиента
     * @param userID
     * @return
     * @throws SQLException
     */
    @Override
    public List<BankCard> getClientsCards(long userID) throws SQLException {
        String query = String.format("SELECT * from Cards WHERE userID=%d", userID);
        return executor.execQuery(query, r -> {
            List<BankCard> cards = new ArrayList<>();
            while(r.next()) {
                cards.add(new BankCard(r.getString("card_name"),
                        userID, r.getLong("accountID")));
            }
            return cards;
        });

    }

    @Override
    public void createTable() throws SQLException {
        executor.execUpdate("CREATE table if not exists Cards (userID bigint, accountID bigint, card_name varchar(256), " +
                "FOREIGN key (userID) REFERENCES clients (id), " +
                "FOREIGN key (accountID) REFERENCES accounts (accountID))");
    }

    @Override
    public void insertCard(long clientID, long accountID, String sn) throws SQLException {
        String insert =
                String.format("INSERT into Cards values ('%d', '%d', '%s')", clientID, accountID, sn);
        executor.execUpdate(insert);
    }
}
