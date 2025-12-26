package logic.loginDatabase;

public class UserSession {
    public final String username;
    public final double lat;
    public final double lon;

    public UserSession(String username, double lat, double lon) {
        this.username = username;
        this.lat = lat;
        this.lon = lon;
    }
}
