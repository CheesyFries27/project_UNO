package thebestlistapp;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;



public class DatabaseConnection {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
            "jdbc:sqlite:src/database/database.db"
        );
    }

    public static void main(String[] args) {
        java.io.File db = new java.io.File(
                "src/database/database.db"
            );


        String sql = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                completed INTEGER NOT NULL DEFAULT 0
            )
            """;

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(sql);
            System.out.println("Database connected successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}

	


