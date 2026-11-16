package projectunov1;
import java.sql.Connection;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.sql.ResultSet;
import java.sql.PreparedStatement;


public class DatabaseConnection {

    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(
        		"jdbc:sqlite:src/projectunov1/database.db"
        );
    }
    // add tasks and use userid priority assigned and other stuff 


 // Simple addTask method for the GUI
    public static int addTask(String title, int userId) {
        return addTask(
            title,
            "",
            0,
            userId,
            userId,
            null,
            false
        );
    }
    // overloading method
    public static int addTask(String title, String description, int priority,  int userId, int assignedTo, LocalDate dueDate, boolean completed) {

        String sql = "INSERT INTO tasks (title, description, user_id, due_date, assigned_to, createdBy, priority, completed) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, title);
            pstmt.setString(2, description);
            pstmt.setInt(3, userId);
            if(dueDate != null) {
            pstmt.setString(4, dueDate.toString());
            }
            else {
            	pstmt.setNull(4, java.sql.Types.VARCHAR);
            }
            pstmt.setInt(5, assignedTo);
            pstmt.setInt(6, userId); // Assuming the creator is the current user
            pstmt.setInt(7, priority);
            pstmt.setBoolean(8, completed);
            
            
            
            

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }

            return -1;

        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    } 
    // delete tasks based on task id
    public static boolean deleteTask(int taskId, int currentUserId) {
    	String sql = "DELETE FROM tasks where id = ? AND user_id = ?";
    	try(Connection conn = connect();
    			PreparedStatement pstmt = conn.prepareStatement(sql)) {
    		pstmt.setInt(1, taskId);
    		pstmt.setInt(2,  currentUserId);
    		int rowsAffected = pstmt.executeUpdate();
    		return rowsAffected > 0;	

    	}
    	catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
    }

	//Log in valifation mechanic
    public static int validateUser(String username, String password) {
        String query = "SELECT id FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id"); // Returns the found UID
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Login failed
    }
	
	/**
     * Inserts a new user into the database and returns the generated UID.
     * Returns -1 if the registration fails (e.g., username already exists).
     */
    public static int registerUser(String username, String password, String email) {
        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, email);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1); // Return generated user ID (uid)
                    }
                }
            }
        } catch (SQLException e) {
        	System.out.println("Error during user registration: " + e.getMessage());
			e.printStackTrace();
        }
        return -1;
    }
    public static java.util.Map<Integer, String> getUsers()
            throws SQLException {

        java.util.Map<Integer, String> users =
            new java.util.LinkedHashMap<>();

        String sql =
            "SELECT id, username FROM users ORDER BY username COLLATE NOCASE";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                users.put(rs.getInt("id"), rs.getString("username"));
            }
        }

        return users;
    }
    // grab tasks based on userid and assigned id then return in a list
    public static java.util.List<Tasks> getTasks(int userId)
            throws SQLException {

        java.util.List<Tasks> tasks = new java.util.ArrayList<>();

        String sql =
            "SELECT * FROM tasks " +
            "WHERE user_id = ? OR assigned_to = ? ORDER BY id DESC";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String date = rs.getString("due_date");

                    LocalDate dueDate =
                        date == null || date.isBlank()
                            ? null
                            : LocalDate.parse(date);

                    Tasks task = new Tasks(
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getInt("priority"),
                        dueDate,
                        rs.getInt("createdBy"),
                        rs.getInt("assigned_to")
                    );

                    task.setId(rs.getInt("id"));
                    task.setCompleted(rs.getBoolean("completed"));

                    tasks.add(task);
                }
            }
        }

        return tasks;
    }

    public static void main(String[] args) {
    	try (Connection conn = connect();
    		     Statement stmt = conn.createStatement();
    		     ResultSet rs = stmt.executeQuery("PRAGMA database_list")) {

    		    while (rs.next()) {
    		        System.out.println("Database path: " + rs.getString("file"));
    		    }

    		} catch (SQLException e) {
    		    e.printStackTrace();
    		}


  
        String createTasksTable = """
            CREATE TABLE IF NOT EXISTS tasks (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                description TEXT,
                user_id INTEGER NOT NULL,
        		due_date TEXT,
                assigned_to INTEGER,
                createdBy INTEGER NOT NULL,
                priority INTEGER NOT NULL DEFAULT 0,
                completed INTEGER NOT NULL DEFAULT 0
            )
            """;

		String createUsersTable = """
            CREATE TABLE IF NOT EXISTS users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL,
                email TEXT
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

	


