package akatsuki;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/sakila"; // Database URL
    private static final String USERNAME = "root";  // MySQL username
    private static final String PASSWORD = "student123";  // MySQL password

    /**
     * Establishes a connection to the MySQL database.
     * @return Connection object to interact with the database
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = null;
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish and return the connection
            connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
            System.out.println("Database connection established successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection.");
            e.printStackTrace();
            throw e; // Re-throw the exception to propagate it further if needed
        }
        return connection;
    }
}