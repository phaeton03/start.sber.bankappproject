package dao.dbService;

import com.company.bankapi.model.Bank;
import com.company.bankapi.model.BankAccount;
import com.company.bankapi.model.Client;
import dao.*;
import dao.executor.Executor;

import java.math.BigDecimal;
import java.sql.*;

public class DBService {
    private final Connection connection;
    public DBService(Connection connection) {
        this.connection = connection;
    }

    public static Connection getH2Connection() {
        try {
        Class.forName("org.h2.Driver");
        String login = "19223669";
        String pass = "";
        Connection connection = DriverManager.getConnection("jdbc:h2:mem:bank", login, pass);
        return connection;
        } catch (Exception e) {
          e.printStackTrace();
    }
        return null;
    }
}
