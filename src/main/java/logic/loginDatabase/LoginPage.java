package logic.loginDatabase;

import java.util.*;

import API.WeatherAPI;
import API.WeatherAPI.GeoLocation;

public class LoginPage {
    
    public UserSession run(Scanner sc) {
        UserAuthenticator auth = new UserAuthenticator();
        
        System.out.println("=== User Authentication System ===");

        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            
            String input = sc.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
                continue;
            }
            
            switch (choice) {
                case 1: // Login
                    System.out.print("Enter email/username: ");
                    String loginId = sc.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = sc.nextLine();
                    
                    if (auth.authenticate(loginId, loginPassword)) {
                        UserSession session = auth.getUserData(loginId);
                        
                        if (session != null) {
                            System.out.println("Login successful!");
                            loggedIn = true;
                            return session;
                        } else {
                            System.out.println("Login failed! User data not found.");
                        }
                    } else {
                        System.out.println("Login failed! Invalid credentials.");
                    }
                    break;
                    
                case 2: // Register
                    System.out.print("Enter username: ");
                    String username = sc.nextLine();
                    System.out.print("Enter email: ");
                    String email = sc.nextLine();
                    System.out.print("Enter password: ");
                    String password = sc.nextLine();
                    
                    // Password confirmation
                    System.out.print("Confirm password: ");
                    String confirmPassword = sc.nextLine();
                    if (!password.equals(confirmPassword)) {
                        System.out.println("Passwords do not match! Please try again.");
                        break;  
                    }
                    
                    // Additional user details
                    API.WeatherAPI api = new WeatherAPI();
                    GeoLocation loc = api.userLocationRegistration(sc);

                    double latitude = loc.lat;
                    double longitude = loc.lon;

                    
                    System.out.print("Enter gender (Male/Female): ");
                    String gender = sc.nextLine();
                    
                    System.out.print("Enter date of birth (YYYY-MM-DD): ");
                    String dateOfBirth = sc.nextLine();
                
                    
                    // Call the updated registerUser method with all details
                    auth.registerUser(username, email, password, gender, dateOfBirth, latitude, longitude);
                    break;
                    
                case 3: // Exit
                    System.out.println("Exiting application...");
                    auth.close();
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } 
        return null;
    }
}