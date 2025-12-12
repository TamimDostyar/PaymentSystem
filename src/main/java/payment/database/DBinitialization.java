// database connection happens here

package payment.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBinitialization extends Database {

    private String dbURL = "jdbc:sqlite:bank.db";

    @Override
    public Connection connect() throws SQLException {
        Connection connect = null;
        try {
            connect = DriverManager.getConnection(dbURL);
            System.out.println("Connection to SQLite has been established.");
            return connect;
        } catch (SQLException sqlE) {
            System.out.println("There was an error: " + sqlE);
            throw sqlE;
        }
    }
}
