package UI;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.LocalTime;

public class WelcomePage {

    private Scene scene;

    public WelcomePage(Stage stage) {

        /* ================= ROOT ================= */
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F3E8FF;");

        /* ================= SIDEBAR ================= */
        VBox sidebar = new VBox(18);
        sidebar.setPadding(new Insets(30));
        sidebar.setPrefWidth(260);
        sidebar.setStyle("-fx-background-color: #FAF7FF;");

        ImageView logoIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/UI/assets/icons/logo-daily.png"))
        );
        logoIcon.setFitWidth(32);
        logoIcon.setFitHeight(32);

        Label logoText = new Label("Mindful\nDaily Journal");
        logoText.setFont(Font.font("Segoe UI", 18));
        logoText.setTextFill(Color.web("#5B21B6"));
        logoText.setStyle("-fx-font-weight: bold;");

        HBox logoBox = new HBox(10, logoIcon, logoText);
        logoBox.setAlignment(Pos.CENTER_LEFT);

        Button homeBtn = navButton("Home", "house.png", true);
        Button newEntryBtn = navButton("New Entry", "pencil.png", false);
        Button journalBtn = navButton("My Journal", "book-open.png", false);

        newEntryBtn.setOnAction(e -> SceneNavigator.goToJournalCreate());
        journalBtn.setOnAction(e -> SceneNavigator.goToJournalIntro());

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        VBox quoteBox = new VBox(8);
        quoteBox.setPadding(new Insets(18));
        quoteBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;"
        );
        quoteBox.setEffect(new DropShadow(8, Color.rgb(0,0,0,0.1)));

        Label quoteTitle = new Label("Daily Quote");
        quoteTitle.setStyle("-fx-font-weight: bold;");
        quoteTitle.setTextFill(Color.web("#6D28D9"));

        Label quoteText = new Label(
            "\"The only journey is the one within.\"\n\n- LowTierProgramming"
        );
        quoteText.setWrapText(true);
        quoteText.setTextFill(Color.GRAY);

        quoteBox.getChildren().addAll(quoteTitle, quoteText);

        sidebar.getChildren().addAll(
            logoBox,
            homeBtn,
            newEntryBtn,
            journalBtn,
            spacer,
            quoteBox
        );

        /* ================= MAIN ================= */
        VBox main = new VBox(30);
        main.setPadding(new Insets(40));
        main.setAlignment(Pos.TOP_CENTER);

        /* ===== CONTENT WRAPPER ===== */
        VBox content = new VBox(30);
        content.setMaxWidth(1100);
        content.setAlignment(Pos.TOP_CENTER);

        /* ===== HEADER CARD ===== */
        HBox headerCard = new HBox(20);
        headerCard.setPadding(new Insets(30));
        headerCard.setAlignment(Pos.CENTER_LEFT);
        headerCard.setPrefWidth(1100);
        headerCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        headerCard.setEffect(new DropShadow(12, Color.rgb(0,0,0,0.12)));

        VBox greetBox = new VBox(6);

        Label greet = new Label(
            getGreeting() + ", " + UserSession.getUsername() + "!"
        );
        greet.setFont(Font.font("Segoe UI", 28));
        greet.setStyle("-fx-font-weight: bold;");
        greet.setTextFill(Color.web("#5B21B6"));

        Label sub = new Label("Ready to capture your thoughts today?");
        sub.setTextFill(Color.web("#7C3AED"));

        greetBox.getChildren().addAll(greet, sub);

        Button writeBtn = new Button("+ Write New Entry");
        writeBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #8B5CF6, #EC4899);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 24;" +
            "-fx-padding: 12 28;"
        );
        writeBtn.setOnAction(e -> SceneNavigator.goToJournalCreate());

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        headerCard.getChildren().addAll(greetBox, headerSpacer, writeBtn);

        /* ===== WEEKLY + RECENT ===== */
        HBox mainRow = new HBox(30);
        mainRow.setAlignment(Pos.CENTER);

        /* ===== WEEKLY CARD ===== */
        VBox weeklyCard = new VBox(15);
        weeklyCard.setPadding(new Insets(30));
        weeklyCard.setPrefWidth(700);
        weeklyCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        weeklyCard.setEffect(new DropShadow(12, Color.rgb(0,0,0,0.12)));

        Label weeklyTitle = new Label("Weekly Mood Summary");
        weeklyTitle.setFont(Font.font("Segoe UI", 22));
        weeklyTitle.setStyle("-fx-font-weight: bold;");
        weeklyTitle.setTextFill(Color.web("#5B21B6"));

        Label weeklySub = new Label("Track your emotional journey over the past week");
        weeklySub.setTextFill(Color.web("#7C3AED"));

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis(0, 10, 2);

        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setPrefHeight(260);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Thu", 8));
        series.getData().add(new XYChart.Data<>("Fri", 4));
        chart.getData().add(series);

        weeklyCard.getChildren().addAll(weeklyTitle, weeklySub, chart);

        /* ===== RECENT CARD ===== */
        VBox recentCard = new VBox(15);
        recentCard.setPadding(new Insets(30));
        recentCard.setPrefWidth(370);
        recentCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        recentCard.setEffect(new DropShadow(12, Color.rgb(0,0,0,0.12)));

        Label recentTitle = new Label("Recent Entries");
        recentTitle.setFont(Font.font("Segoe UI", 20));
        recentTitle.setStyle("-fx-font-weight: bold;");
        recentTitle.setTextFill(Color.web("#5B21B6"));

        Label recentSub = new Label("Your latest thoughts");
        recentSub.setTextFill(Color.web("#7C3AED"));

        recentCard.getChildren().addAll(recentTitle, recentSub);

        mainRow.getChildren().addAll(weeklyCard, recentCard);

        content.getChildren().addAll(headerCard, mainRow);
        main.getChildren().add(content);

        root.setLeft(sidebar);
        root.setCenter(main);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    /* ================= HELPERS ================= */
    private Button navButton(String text, String icon, boolean active) {
        ImageView iconView = new ImageView(
            new Image(getClass().getResourceAsStream("/UI/assets/icons/" + icon))
        );
        iconView.setFitWidth(16);
        iconView.setFitHeight(16);

        Button btn = new Button(text, iconView);
        btn.setPrefWidth(200);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setGraphicTextGap(12);

        if (active) {
            btn.setStyle(
                "-fx-background-color: #E9D5FF;" +
                "-fx-text-fill: #5B21B6;" +
                "-fx-font-weight: bold;" +
                "-fx-background-radius: 14;"
            );
        } else {
            btn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: #7C3AED;"
            );
        }
        return btn;
    }

    private String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour < 12) return "Good Morning";
        if (hour < 18) return "Good Afternoon";
        return "Good Evening";
    }

    public Scene getScene() {
        return scene;
    }
}
