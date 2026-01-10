package logic.loginDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.control.Alert;

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
            new Alert(Alert.AlertType.WARNING, "Wrong username or password! Please try again.").show();
            e.printStackTrace();
        }
        
        return false;
    }
    
    public UserSession getUserData(String emailOrUsername) {
        String query = "SELECT username, gender, date_of_birth, latitude, longitude FROM users WHERE email = ? OR username = ?";

        try (Connection conn = dbManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)) {
                
            pstmt.setString(1, emailOrUsername);
            pstmt.setString(2, emailOrUsername);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new UserSession(
                        rs.getString("username"),
                        rs.getString("gender"),
                        rs.getString("date_of_birth"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                    );
                }
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING, "User not found!").show();
            e.printStackTrace();
        }
        return null;
    }
    
    // Updated registerUser method with all parameters
    public void registerUser(String username, String email, String password, 
                             String gender, String dateOfBirth, double latitude, double longitude) {
        
        String hashedPassword = PasswordHasher.hashPassword(password);
        
        // 1. Validate email format
        if (!isValidEmail(email)) {
            new Alert(Alert.AlertType.INFORMATION,"Invalid email format. Please enter a valid email address.").show();;
            return;
        }
        
        // 2. Hash password
        if (hashedPassword == null) {
            new Alert(Alert.AlertType.INFORMATION,"Password hashing failed. Registration aborted.").show();;
            return;
        }
        
        // 3. Check if user already exists
        if (isUsernameExists(username)) {
            new Alert(Alert.AlertType.INFORMATION,"Username already taken. Please choose a different username.").show();
            return;
        }
        
        // 4. Check if email already exists
        if (isEmailExists(email)) {
            new Alert(Alert.AlertType.INFORMATION, "Email already registered. Please use a different email.").show();
            return;
        }
        
        // 5. Validate date format
        if (!isValidDate(dateOfBirth)) {
            new Alert(Alert.AlertType.INFORMATION,"Invalid date format. Please use YYYY-MM-DD format.").show();
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
            
        } catch (IllegalArgumentException e) {
            new Alert(Alert.AlertType.WARNING, "Invalid date format. Please use YYYY-MM-DD.").show();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.WARNING, "Database error during registration").show();
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
            new Alert(Alert.AlertType.WARNING, "Database error checking email existence").show();
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
            new Alert(Alert.AlertType.WARNING, "Database error checking username existence").show();
            e.printStackTrace();
        }
        
        return false;
    }
    
    public void close() {
        dbManager.closeConnection();
    }
}
