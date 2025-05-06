package config;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DBConnection {
    private static Connection conn = null;
    private DBConnection() {}
    public static Connection getConnection() throws SQLException {
    if (conn == null || conn.isClosed()) {
        try {
            System.out.println("Attempting to connect to database...");
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/edusphere?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
    "root",
    "SPYBOT"
);
            DatabaseMetaData metaData = conn.getMetaData();
            System.out.println("✅ Connected to DB: " + metaData.getDatabaseProductName() +
                               " (Version " + metaData.getDatabaseProductVersion() + ")");
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed.");
            e.printStackTrace();
            throw e;
        }
    }
    return conn;
}
}
