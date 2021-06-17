package dao.executor;


import java.sql.*;

/**Реализуем Паттерн Executor для нашей БД.
 * Выносим общую часть (отправку запроса) в отдельный класс
 * @author      Nikolay Nikolskiy
 * @version     %I% %G%
 * @since       1.0
 * */
public class Executor {
    private final Connection connection;
    /**
     * Конструктор для инициализации объекта Connection
     * @param connection - передаем объект Connection для осуществления действий с БД
     */
    public Executor(Connection connection) {
        this.connection = connection;
    }
    /**
     * Создаем метод execUpdate для отправка запросов вида Update к БД
     * @param update - отправляем строку запроса вида update
     */
    public void execUpdate(String update) throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute(update);
        statement.close();
    }

    /**
     * Создаем метод для реализации SQL запросов вида Query
     * @param query - запрос типа query
     * @param resultHandler - объект класса для работы с ResultSet
     * @param <T> - типизированный параметр берется по типу query
     * @return
     */
    public <T> T execQuery(String query, ResultHandler<T> resultHandler) throws SQLException {
            Statement statement = connection.createStatement();
            statement.execute(query);
            ResultSet rs = statement.getResultSet();
            T value = resultHandler.handle(rs);
            rs.close();
            statement.close();
            return value;
    }
}
