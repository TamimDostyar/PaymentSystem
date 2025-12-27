// database connection happens here

package payment.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBinitialization extends Database {

    private String dbURL;

    public DBinitialization() {
        // Use /tmp directory for Render (writable) or local bank.db for development
        String dbPath = System.getenv("DB_PATH");
        if (dbPath != null && !dbPath.isEmpty()) {
            dbURL = "jdbc:sqlite:" + dbPath;
        } else {
            // Default: use /tmp for production (Render), fallback to local for dev
            String tmpDir = System.getProperty("java.io.tmpdir");
            if (tmpDir != null && !tmpDir.isEmpty()) {
                dbURL = "jdbc:sqlite:" + tmpDir + "/bank.db";
            } else {
                dbURL = "jdbc:sqlite:/tmp/bank.db";
            }
        }
        System.out.println("Using database: " + dbURL);
    }

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
