package dao.executor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**Функциональный интерфейс для работы с ResultSet
 * @author      Nickolay Nickolsky
 * @version     %I% %G%
 * @since       1.0
 * */
public interface ResultHandler<T> {
    /**
     * Метод для работы с ResultSet. Так как отдавать значение ResultSet принимающей стороне не безопасно
     * вся работа по обработке запроса и его закрытию будет осуществляться в данном методе
     * @param rs - передаем параметр типа ResultSet
     * @return - типизированное значение выходного параметра
     * @throws SQLException
     */
    T handle(ResultSet rs) throws SQLException;
}
