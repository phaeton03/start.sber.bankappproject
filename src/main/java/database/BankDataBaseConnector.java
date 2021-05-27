package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class BankDataBaseConnector {
    public static void main(String[] args) throws Exception {
        Class.forName("org.h2.driver");
        try (Connection con =
                     DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/default","19223669",""))
        {
            PreparedStatement preparedStatement = con.createStatement();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
