package dao;

import com.company.bankapi.model.BankCard;

import java.sql.SQLException;
import java.util.*;

public interface BankCardDAOInt {
     List<BankCard> getClientsCards(long id) throws SQLException;
     void createTable() throws SQLException;
     void insertCard(long clientID, long accountID, String sn) throws SQLException;
}
