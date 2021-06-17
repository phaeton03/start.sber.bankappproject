package dao;

import com.company.bankapi.model.BankAccount;
import dao.executor.Executor;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO implements BankAccountDAOInt {
    private Executor executor;

    public BankAccountDAO(Executor executor) {
        this.executor = executor;
    }

    @Override
    public BankAccount getAccount(long accountID) throws SQLException {
        String query = String.format("SELECT * FROM Accounts WHERE accountID=%d", accountID);
        return executor.execQuery(query, r -> {
           r.next();
           return new BankAccount(accountID,
                   r.getBigDecimal("balance"),
                   r.getLong("clientID"));
        });
    }

    @Override
    public List<BankAccount> getClientsAccounts(long userID) throws SQLException {
        String query = String.format("SELECT * FROM Accounts WHERE clientID=%d", userID);
        return executor.execQuery(query, r -> {
           List<BankAccount> accounts = new ArrayList<>();
            while(r.next() == true){
                accounts.add(new BankAccount(r.getLong("accountID"),
                       r.getBigDecimal("balance"),
                       userID));
           }
            return accounts;
        });
    }
//Новое значение для поля Баланс
    @Override
    public void updateBalance(long accountID, BigDecimal newBalance) throws SQLException {
        String query = String.format("UPDATE Accounts SET balance=%s WHERE accountID=%d", newBalance, accountID);
        executor.execUpdate(query);
    }

    @Override
    public void insertAccount(long accountID, long clientID, BigDecimal balance) throws SQLException {
        String query = String.format("INSERT into Accounts values ('%d', '%d', '%s')", accountID, clientID, balance);
        executor.execUpdate(query);
    }

    @Override
    public void createTable() throws SQLException {
        executor.execUpdate("CREATE table if not exists Accounts (accountID bigint, clientID bigint, balance decimal, " +
                "PRIMARY KEY (accountID)," +
                "FOREIGN KEY (clientID) REFERENCES clients (id))");
    }

}
