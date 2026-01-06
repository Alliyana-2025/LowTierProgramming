package UI;

public class UserSession {

    private static String username;

    public static void login(String user) {
        username = user;
    }

    public static String getUsername() {
        return username;
    }

    public static void logout() {
        username = null;
    }

    public static boolean isLoggedIn() {
        return username != null;
    }
}
