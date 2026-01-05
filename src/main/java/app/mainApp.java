package app;

import javafx.application.Application;
import javafx.stage.Stage;
import UI.SceneNavigator;

public class mainApp extends Application {

    @Override
    public void start(Stage stage) {
        SceneNavigator.setStage(stage);

        stage.setTitle("Mindful Journal");

        SceneNavigator.goToLogin();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
