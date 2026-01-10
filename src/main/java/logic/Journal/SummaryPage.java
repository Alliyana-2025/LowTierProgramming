package logic.Journal;

import java.util.Collections;
import java.util.List;

import API.GeminiAPI;
import UI.SceneNavigator;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.Cursor;
import javafx.stage.Stage;

public class SummaryPage {

    private Scene scene;
    private final Stage stage;
    private final SceneNavigator navigator;
    private final List<JournalEntries> lastSevenEntries = JournalList.getJournalEntries();

    public SummaryPage(Stage stage, SceneNavigator navigator) {
        this.stage = stage;
        this.navigator = navigator;

        /* ================= ROOT ================= */
        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);"
        );

        /* ================= HEADER ================= */
        Label headerTitle = new Label("Weekly Summary");
        headerTitle.setFont(Font.font("Segoe UI", 26));
        headerTitle.setTextFill(Color.WHITE);

        ImageView headerStar = new ImageView(
            new Image(getClass().getResourceAsStream("/images/star.png"))
        );
        headerStar.setFitWidth(14);
        headerStar.setFitHeight(14);

        Label headerSubText = new Label("A reflection of your mood & journaling");
        headerSubText.setFont(Font.font(14));
        headerSubText.setTextFill(Color.WHITE);

        HBox headerSub = new HBox(6, headerSubText, headerStar);
        headerSub.setAlignment(Pos.CENTER_LEFT);

        VBox headerText = new VBox(8, headerTitle, headerSub);

        Button backBtn = new Button("← Back");
        backBtn.setCursor(Cursor.HAND);
        backBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.25);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 18;" +
            "-fx-padding: 8 20;"
        );
        backBtn.setOnAction(e -> navigator.goToJournalHub());

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox headerContent = new HBox(20, headerText, spacer, backBtn);
        headerContent.setAlignment(Pos.CENTER_LEFT);

        VBox headerCard = new VBox(headerContent);
        headerCard.setPadding(new Insets(25));
        headerCard.setMaxWidth(900);
        headerCard.setStyle(
            "-fx-background-color: linear-gradient(to right, #FF5C8D, #8B72FF);" +
            "-fx-background-radius: 28;"
        );
        headerCard.setEffect(new DropShadow(15, Color.rgb(0,0,0,0.25)));

        /* ================= CHART ================= */

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(1, 10, 1);

        LineChart<String, Number> moodChart =
            new LineChart<>(xAxis, yAxis);
        moodChart.setLegendVisible(false);
        moodChart.setAnimated(false);
        moodChart.setPrefHeight(260);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        Collections.reverse(lastSevenEntries);
        for (JournalEntries entry : lastSevenEntries) {
            try {
                int rating = Integer.parseInt(entry.getRating());
                String label = entry.getDate();
                series.getData().add(new XYChart.Data<>(label, rating));
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Couldn't produce mood graph!");
            }
        }

        moodChart.getData().add(series);

        ImageView summaryIcon = new ImageView(
            new Image(getClass().getResource("/images/summary.png").toExternalForm())
        );
        summaryIcon.setFitWidth(18);
        summaryIcon.setFitHeight(18);

        Label chartTitleText = new Label("Mood Trend");
        chartTitleText.setFont(Font.font("Segoe UI", 16));

        HBox chartTitle = new HBox(8, summaryIcon, chartTitleText);
        chartTitle.setAlignment(Pos.CENTER_LEFT);

        VBox chartCard = new VBox(18, chartTitle, moodChart);
        chartCard.setPadding(new Insets(30));
        chartCard.setMaxWidth(900);
        chartCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        chartCard.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));

        /* ================= INSIGHT ================= */
        ImageView insightStar = new ImageView(
            new Image(getClass().getResourceAsStream("/images/star.png"))
        );
        insightStar.setFitWidth(16);
        insightStar.setFitHeight(16);

        Label insightTitleText = new Label("Weekly Insight");
        insightTitleText.setFont(Font.font("Segoe UI", 18));

        HBox insightTitle = new HBox(8, insightStar, insightTitleText);
        insightTitle.setAlignment(Pos.CENTER_LEFT);

        // pulling 7 days worth of journal entries
        if (lastSevenEntries.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No journal entries found!").show();
        }

        // calling gemini AI for summary

        GeminiAPI api = new GeminiAPI();

        String prompt = "Write a short summary of the user's mood for the week using these entries, "
                        + "take into account the user's data, such as AGE (given the DoB) and where user lives (given Latitude and Longitude) that is also given if it helps with better response. "
                        + "Do not include the user's data EXCEPT the name, inside the response text "
                        + "Suggest improvements that the user can implement to improve their mood and coming weeks based on their data/profile. "
                        + "Write in less than 100 words. \n"
                        + lastSevenEntries.toString() + "\n"
                        + navigator.getSession();
        Task<String> task = new Task<>() {
            @Override
            protected String call() {
                return api.getSummaryForSession(prompt, navigator.getSession());
            }
        };        

        Label insightText = new Label("");
        insightText.setWrapText(true);
        insightText.setTextFill(Color.GRAY);

        task.setOnSucceeded(ev -> insightText.setText(task.getValue()));
        new Thread(task).start();

        VBox insightCard = new VBox(12, insightTitle, insightText);
        insightCard.setPadding(new Insets(30));
        insightCard.setMaxWidth(900);
        insightCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        insightCard.setEffect(new DropShadow(10, Color.rgb(0,0,0,0.2)));

        /* ================= ASSEMBLE ================= */
        root.getChildren().addAll(
            headerCard,
            chartCard,
            insightCard
        );

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    public Scene getScene() {
        return scene;
    }
}
