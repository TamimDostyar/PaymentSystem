// this is a class abstraction

package payment.database;
import java.sql.*;

abstract public class Database {
    public abstract Connection connect() throws SQLException;
}
