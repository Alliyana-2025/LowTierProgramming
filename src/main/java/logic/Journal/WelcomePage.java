package logic.Journal;

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
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import API.WeatherAPI;
import API.WeatherAPI.WeatherResponse;
import UI.SceneNavigator;

public class WelcomePage {

    private Scene scene;
    private final Stage stage;
    private final SceneNavigator navigator;
    private JournalEntries entry;
    private JournalMode mode;
    private List<JournalEntries> lastSevenEntries = JournalList.getJournalEntries();

    public WelcomePage(Stage stage, SceneNavigator navigator, JournalMode mode, JournalEntries entry) {
        this.stage = stage;
        this.navigator = navigator;
        this.entry = entry;
        this.mode = mode;

        /* ================= ROOT ================= */
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F3E8FF;");

        /* ================= SIDEBAR ================= */
        VBox sidebar = new VBox(18);
        sidebar.setPadding(new Insets(30));
        sidebar.setPrefWidth(260);
        sidebar.setStyle("-fx-background-color: #FAF7FF;");

        ImageView logoIcon = new ImageView(
            new Image(getClass().getResourceAsStream("/images/logo-daily.png"))
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
        Button journalBtn = navButton("My Journal", "book-open.png", false);

        journalBtn.setOnAction(e -> navigator.goToJournalHub());

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
            getGreeting() + ", " + navigator.getSession().username + "!"
        );
        greet.setFont(Font.font("Segoe UI", 28));
        greet.setStyle("-fx-font-weight: bold;");
        greet.setTextFill(Color.web("#5B21B6"));

        Label sub = new Label("Ready to capture your thoughts today?");
        sub.setTextFill(Color.web("#7C3AED"));

        greetBox.getChildren().addAll(greet, sub);

        Button writeBtn = new Button("See Journals");
        writeBtn.setStyle(
            "-fx-background-color: linear-gradient(to right, #8B5CF6, #EC4899);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 24;" +
            "-fx-padding: 12 28;"
        );
        LocalDate date = LocalDate.now();
        writeBtn.setOnAction(e -> {
            navigator.setDate(date);
            navigator.goToJournalList();
        });

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);

        headerCard.getChildren().addAll(greetBox, headerSpacer, writeBtn);

        /* ===== WEEKLY + WEATHER ===== */
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
        NumberAxis yAxis = new NumberAxis(1, 10, 1);

        LineChart<String, Number> moodChart =
            new LineChart<>(xAxis, yAxis);
        moodChart.setLegendVisible(false);
        moodChart.setAnimated(false);
        moodChart.setPrefHeight(260);

        XYChart.Series<String, Number> series = new XYChart.Series<>();

        Collections.reverse(lastSevenEntries);
        for (JournalEntries ent : lastSevenEntries) {
            try {
                int rating = Integer.parseInt(ent.getRating());
                String label = ent.getDate();
                series.getData().add(new XYChart.Data<>(label, rating));
            } catch (NumberFormatException e) {
                new Alert(Alert.AlertType.WARNING, "Couldn't produce mood graph!");
            }
        }
        moodChart.getData().add(series);

        weeklyCard.getChildren().addAll(weeklyTitle, weeklySub, moodChart);

        /* ===== WEATHER CARD ===== */
        WeatherAPI api = new WeatherAPI();
        WeatherResponse weatherData = api.getWeatherCached(navigator.getSession().lat, navigator.getSession().lon);

        VBox weatherCard = new VBox(18);
        weatherCard.setPadding(new Insets(30));
        weatherCard.setPrefWidth(370);
        weatherCard.setAlignment(Pos.TOP_LEFT);
        weatherCard.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 28;"
        );
        weatherCard.setEffect(new DropShadow(12, Color.rgb(0, 0, 0, 0.12)));

        Label weatherTitle = new Label("Today's Weather");
        weatherTitle.setFont(Font.font("Segoe UI", 20));
        weatherTitle.setStyle("-fx-font-weight: bold;");
        weatherTitle.setTextFill(Color.web("#5B21B6"));

        // quick func to convert country ISO code to actual name
        Locale locale = new Locale("", weatherData.sys.country);
        String countryName = locale.getDisplayCountry();

        Label locationLabel = new Label(weatherData.name + ", " + countryName);
        locationLabel.setFont(Font.font("Segoe UI", 18));
        locationLabel.setTextFill(Color.web("#7C3AED"));

        HBox tempRow = new HBox(10);
        tempRow.setAlignment(Pos.CENTER);

        ImageView weatherIcon = new ImageView(
            new Image("https://openweathermap.org/img/wn/" + weatherData.weather[0].icon + "@2x.png", true)
        );
        weatherIcon.setFitWidth(80);
        weatherIcon.setFitHeight(80);

        Label tempLabel = new Label(Double.toString(weatherData.main.temp) + "°C");
        tempLabel.setFont(Font.font("Segoe UI", 38));
        tempLabel.setStyle("-fx-font-weight: bold;");
        tempLabel.setTextFill(Color.web("#7C3AED"));

        tempRow.getChildren().addAll(weatherIcon, tempLabel);

        /* --- Condition --- */
        Label conditionLabel = new Label(toSentenceCase(weatherData.weather[0].description));
        conditionLabel.setFont(Font.font("Segoe UI", 16 ));
        conditionLabel.setTextFill(Color.web("#7C3AED"));

        /* --- Extra Info --- */
        Label feelsLikeLabel = new Label("Feels like " + Double.toString(weatherData.main.feels_like) + "°C");
        feelsLikeLabel.setFont(Font.font("Segoe UI", 15));
        feelsLikeLabel.setTextFill(Color.web("#7C3AED"));

        Label humidityLabel = new Label("Humidity: " + Double.toString(weatherData.main.humidity) + "%");
        humidityLabel.setFont(Font.font("Segoe UI", 15));
        humidityLabel.setTextFill(Color.web("#7C3AED"));

        StackPane centeredCondition = new StackPane(conditionLabel);
        StackPane centeredFeelsLike = new StackPane(feelsLikeLabel);
        StackPane centeredHumidity = new StackPane(humidityLabel);

        weatherCard.getChildren().addAll(
            weatherTitle,
            locationLabel,
            tempRow,
            centeredCondition,
            centeredFeelsLike,
            centeredHumidity
        );

        mainRow.getChildren().addAll(weeklyCard, weatherCard);
        content.getChildren().addAll(headerCard, mainRow);
        main.getChildren().add(content);

        root.setLeft(sidebar);
        root.setCenter(main);

        scene = new Scene(root, stage.getWidth(), stage.getHeight());
    }

    /* ================= HELPERS ================= */
    private Button  navButton(String text, String icon, boolean active) {
        ImageView iconView = new ImageView(
            new Image(getClass().getResourceAsStream("/images/" + icon))
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

    private static String toSentenceCase(String text) {
        if (text == null || text.isEmpty()) return text;
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public Scene getScene() {
        return scene;
    }
}
