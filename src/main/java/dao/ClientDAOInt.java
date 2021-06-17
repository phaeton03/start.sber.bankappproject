package dao;

import com.company.bankapi.model.Client;

import java.sql.SQLException;
import java.util.List;

public interface ClientDAOInt {
    Client getClient(long clientID) throws SQLException;
    void createTable() throws SQLException;
    void insertClient(long id, String name) throws SQLException;
    List<Client> getAllClients() throws SQLException;
}
