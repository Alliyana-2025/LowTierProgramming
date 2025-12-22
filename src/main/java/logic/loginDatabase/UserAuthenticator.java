package logic.loginDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAuthenticator {
    
    private DatabaseManager dbManager;
    
    public UserAuthenticator() {
        dbManager = new DatabaseManager();
    }
    
    public boolean authenticate(String emailOrUsername, String password) {
        String hashedPassword = PasswordHasher.hashPassword(password);
        if (hashedPassword == null) {
            return false;
        }
        
        String query = "SELECT password FROM users WHERE email = ? OR username = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, emailOrUsername);
            pstmt.setString(2, emailOrUsername);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHashedPassword = rs.getString("password");
                    return hashedPassword.equals(storedHashedPassword);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error during authentication");
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Get username by email or username 
    public String getUsername(String emailOrUsername) {
        String query = "SELECT username FROM users WHERE email = ? OR username = ?";
        
        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, emailOrUsername);
            pstmt.setString(2, emailOrUsername);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username");
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error retrieving username");
            e.printStackTrace();
        }
        
        return null;
    }

    public double getLatitude(String emailOrUsername) {
        String query = "SELECT latitude FROM users WHERE email = ? OR username = ?";

        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, emailOrUsername);
            pstmt.setString(2, emailOrUsername);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("latitude");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error retrieving latitude");
            e.printStackTrace();
        }

        return 0;
    }

    public double getLongitude(String emailOrUsername) {
        String query = "SELECT longitude FROM users WHERE email = ? OR username = ?";

        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, emailOrUsername);
            pstmt.setString(2, emailOrUsername);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("longitude");
                }
            }

        } catch (SQLException e) {
            System.err.println("Database error retrieving longitude");
            e.printStackTrace();
        }

        return 0;
    }
    
    // Updated registerUser method with all parameters
    public void registerUser(String username, String email, String password, 
                             String gender, String dateOfBirth, double latitude, double longitude) {
        
        String hashedPassword = PasswordHasher.hashPassword(password);
        
        // 1. Validate email format
        if (!isValidEmail(email)) {
            System.out.println("Invalid email format. Please enter a valid email address.");
            return;
        }
        
        // 2. Hash password
        if (hashedPassword == null) {
            System.out.println("Password hashing failed. Registration aborted.");
            return;
        }
        
        // 3. Check if user already exists
        if (isUsernameExists(username)) {
            System.out.println("Username already taken. Please choose a different username.");
            return;
        }
        
        // 4. Check if email already exists
        if (isEmailExists(email)) {
            System.out.println("Email already registered. Please use a different email.");
            return;
        }
        
        // 5. Validate date format
        if (!isValidDate(dateOfBirth)) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD format.");
            return;
        }
        
        // 6. Insert new user into database with all details
        String query = "INSERT INTO users (username, email, password, gender, date_of_birth, latitude, longitude) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            pstmt.setString(4, gender);
            pstmt.setDouble(6, latitude);
            pstmt.setDouble(7, longitude);
            
            // Convert String date to SQL Date
            if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
                java.sql.Date sqlDate = java.sql.Date.valueOf(dateOfBirth);
                pstmt.setDate(5, sqlDate);
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("User registered successfully!");
            } else {
                System.out.println("Registration failed.");
            }
            
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
        } catch (SQLException e) {
            System.err.println("Database error during registration");
            e.printStackTrace();
        }
    }
    
    // Simple email validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }
    
    // Validate date format (YYYY-MM-DD)
    private boolean isValidDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return true; // Allow empty dates
        }
        
        try {
            java.sql.Date.valueOf(dateStr);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    // Check if email exists in database
    private boolean isEmailExists(String email) {
        String query = "SELECT COUNT(*) FROM users WHERE email = ?";
        
        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error checking email existence");
            e.printStackTrace();
        }
        
        return false;
    }
    
    // Check if username exists in database
    private boolean isUsernameExists(String username) {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        
        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
            
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Database error checking username existence");
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void close() {
        dbManager.closeConnection();
    }
}