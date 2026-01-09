package logic.loginDatabase;

public class UserSession {
    public final String username;
    public final String gender;
    public final String dob;
    public final double lat;
    public final double lon;

    private String cachedSummary;
    public String getCachedSummary() {
        return cachedSummary;
    }
    public void setCachedSummary(String cachedSummary) {
        this.cachedSummary = cachedSummary;
    }
    public void clearCache() {
        cachedSummary = null;
    }

    public UserSession(String username, String gender, String dob, double lat, double lon) {
        this.username = username;
        this.gender = gender;
        this.dob = dob;
        this.lat = lat;
        this.lon = lon;
    }
    @Override
    public String toString() {
        return "Username: " + username +
               ", Gender: " + gender +
               ", Date of Birth: " + dob +
               ", Latitude: " + lat +
               ", Longitude: " + lon;
    }
}
