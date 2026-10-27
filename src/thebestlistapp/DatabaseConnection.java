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

	//Log in valifation mechanic
	public static boolean validateUser(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                // If rs.next() is true, a matching user was found
                return rs.next();
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        String createTasksTable = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                completed INTEGER NOT NULL DEFAULT 0
            )
            """;

		String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            )
            """;
//Made a quick change as for the table to made of strings to compare in the log in screen! Alex

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

			stmt.executeUpdate(createTasksTable);
            stmt.executeUpdate(createUsersTable);

            stmt.executeUpdate(sql);
            System.out.println("Database connected successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
}

	


