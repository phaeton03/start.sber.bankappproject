package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {
     List<T> getAll() throws SQLException;
    T get(long id) throws SQLException;
    //void update(T t, String[] params) throws SQLException;
    void delete(T t) throws SQLException;
    void dropTable() throws SQLException;
    void createTable() throws SQLException;
    void insert(String... params) throws SQLException;
    void update() throws SQLException;
}
