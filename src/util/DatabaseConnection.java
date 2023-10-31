package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static Connection conn;

    static {
        try {
            Properties properties = new Properties();
            properties.load(Files.newBufferedReader(Path.of("datasource.properties")));
            conn = DriverManager.getConnection(
                    properties.getProperty("db.url"),
                    properties.getProperty("db.username"),
                    properties.getProperty("db.password"));
        } catch (SQLException | IOException e) {
            e.getStackTrace();
        }
    }
    //Para que no se puedan crear instancias de la clase
    private DatabaseConnection() {}

    public static Connection getConn() {
        return conn;
    }
    public static void close() throws SQLException {
        conn.close();
    }
}
