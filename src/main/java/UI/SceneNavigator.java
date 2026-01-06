package UI;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneNavigator {

    private static Stage stage;
    private static boolean wasMaximized = true;

    public static void setStage(Stage primaryStage) {
        stage = primaryStage;

        stage.setMinWidth(1200);
        stage.setMinHeight(750);

        stage.setMaximized(true);
    }

    public static void switchScene(Scene scene) {

        wasMaximized = stage.isMaximized();

        stage.setScene(scene);

        stage.setMaximized(wasMaximized);

        stage.setFullScreen(false);

        stage.show();
    }

    // ====== NAVIGATION ======

    public static void goToLogin() {
        switchScene(new LoginPage(stage).getScene());
    }

    public static void goToRegister() {
        switchScene(new RegisterPage(stage).getScene());
    }

    public static void goToWelcome() {
        switchScene(new WelcomePage(stage).getScene());
    }

    public static void goToJournalIntro() {
        switchScene(new JournalIntroPage(stage).getScene());
    }

    public static void goToJournalHub() {
        switchScene(new JournalHubPage(stage).getScene());
    }

    public static void goToJournalDates() {
        switchScene(new JournalDatesPage(stage).getScene());
    }

    public static void goToJournalView() {
        switchScene(new JournalViewPage(stage).getScene());
    }

    public static void goToJournalCreate() {
        switchScene(new JournalCreatePage(stage).getScene());
    }

    public static void goToSummary() {
        switchScene(new SummaryPage(stage).getScene());
    }
}
