package UI;

import java.time.LocalDate;

import API.WeatherAPI;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import logic.Journal.JournalEntries;
import logic.Journal.JournalHubPage;
import logic.Journal.JournalList;
import logic.Journal.JournalMode;
import logic.Journal.JournalView;
import logic.Journal.SummaryPage;
import logic.Journal.WelcomePage;
import logic.loginDatabase.LoginPage;
import logic.loginDatabase.RegisterPage;
import logic.loginDatabase.UserAuthenticator;
import logic.loginDatabase.UserSession;

public class SceneNavigator {

    private final Stage stage;
    private static boolean wasMaximized = true;
    private LocalDate date;
    private UserSession session;
    private JournalMode mode;
    private JournalEntries entry;

    public void setSession(String emailOrUsername) {
        UserAuthenticator auth = new UserAuthenticator();
        this.session = auth.getUserData(emailOrUsername);
        if (session == null) {
            new Alert(Alert.AlertType.WARNING, "User not found").show();
        }
        System.out.println("getUserData completed");
        WeatherAPI api = new WeatherAPI();
        api.getWeather(session.lat, session.lon);
        System.out.println("Get curr weather completed");
    }
    public UserSession getSession() {
        return session;
    }

    public void setDate(LocalDate date) { this.date = date; }
    public LocalDate getDate() { return date; }

    public void setMode(JournalMode mode) { this.mode = mode; }
    public JournalMode getMode() { return mode; }

    public void setEntry(JournalEntries entry) { this.entry = entry; }
    public JournalEntries getEntry() { return entry; }

    public SceneNavigator(Stage stage) {
        this.stage = stage;

        stage.setMinWidth(1200);
        stage.setMinHeight(750);
        stage.setMaximized(true);
    }

    private void switchScene(Scene scene) {
        wasMaximized = stage.isMaximized();

        stage.setScene(scene);

        stage.setMaximized(wasMaximized);
        stage.setFullScreen(false);
        stage.show();
    }

    // ====== NAVIGATION ======

    public void goToLogin() {
        switchScene(new LoginPage(stage, this).getScene());
    }

    public void goToRegister() {
        switchScene(new RegisterPage(stage, this).getScene());
    }

    public void goToWelcome() {
        System.out.println("trying to go to welcome page");
        switchScene(new WelcomePage(stage, this, mode, entry).getScene());
    }

    public void goToJournalHub() {
        switchScene(new JournalHubPage(stage, this).getScene());
    }

    public void goToJournalList() {
        switchScene(new JournalList(stage, this).getScene());
    }

    public void goToJournalView() {
        switchScene(new JournalView(stage, this, mode, entry).getScene());
    }

    public void goToSummary() {
        switchScene(new SummaryPage(stage, this).getScene());
    }
}
