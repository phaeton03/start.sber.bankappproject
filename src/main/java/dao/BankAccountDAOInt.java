package dao;

import com.company.bankapi.model.BankAccount;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface BankAccountDAOInt {
    List<BankAccount> getClientsAccounts(long userID) throws SQLException;
    BankAccount getAccount(long accountID) throws SQLException;
    void createTable() throws SQLException;
    void updateBalance(long accountID, BigDecimal balance) throws SQLException;
    void insertAccount(long accountID, long clientID, BigDecimal balance) throws SQLException;
}
