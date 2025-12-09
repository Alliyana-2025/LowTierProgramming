package smartjournal;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class SummaryPage extends Application {

    @Override
    public void start(Stage stage) {
        // --- Root VBox with gradient background ---
        VBox root = new VBox(30);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #FCEAFF, #E9E8FF);");

        // --- Title ---
        Label title = new Label("Weekly Summary");
        title.setFont(Font.font("Segoe UI", 28));
        title.setTextFill(Color.web("#FF5C8D"));

        Label subtitle = new Label("Review your week’s weather and mood 🌤💗");
        subtitle.setFont(Font.font("Segoe UI", 18));
        subtitle.setTextFill(Color.web("#8B72FF"));

        // --- Weather Cards (Scrollable) ---
        HBox weatherBox = new HBox(15);
        weatherBox.setAlignment(Pos.CENTER);

        String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        String[] weatherIcons = {"☀", "☁", "🌧", "🌤", "☀", "🌧", "⛅"};

        int currentDayIndex = getCurrentDayIndex();

        for (int i = 0; i < 7; i++) {
            VBox dayCard = new VBox(5);
            dayCard.setAlignment(Pos.CENTER);
            dayCard.setPadding(new Insets(12));
            dayCard.setPrefWidth(80);
            dayCard.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
            dayCard.setEffect(new DropShadow(5, Color.rgb(200, 200, 200, 0.3)));

            if (i == currentDayIndex) {
                dayCard.setStyle("-fx-background-color: #FFEDF3; -fx-background-radius: 20;");
            }

            Label dayLabel = new Label(days[i]);
            dayLabel.setFont(Font.font(14));
            dayLabel.setTextFill(Color.web("#34495E"));

            Label weatherLabel = new Label(weatherIcons[i] + " " + getWeatherText(weatherIcons[i]));
            weatherLabel.setFont(Font.font(16));

            dayCard.getChildren().addAll(dayLabel, weatherLabel);
            weatherBox.getChildren().add(dayCard);
        }

        ScrollPane weatherScroll = new ScrollPane(weatherBox);
        weatherScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        weatherScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        weatherScroll.setFitToHeight(true);
        weatherScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // --- Mood Chart ---
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Day");

        NumberAxis yAxis = new NumberAxis(1, 5, 1);
        yAxis.setLabel("Mood");
        yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis) {
            @Override
            public String toString(Number object) {
                switch (object.intValue()) {
                    case 1: return "😞";
                    case 2: return "😐";
                    case 3: return "🙂";
                    case 4: return "😊";
                    case 5: return "😍";
                }
                return "";
            }
        });

        LineChart<String, Number> moodChart = new LineChart<>(xAxis, yAxis);
        moodChart.setLegendVisible(false);
        moodChart.setAnimated(false);
        moodChart.setTitle("Mood Fluctuations");
        moodChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> moodSeries = new XYChart.Series<>();
        int[] moods = {3, 4, 2, 5, 3, 4, 3}; // Example mood data
        for (int i = 0; i < 7; i++) {
            moodSeries.getData().add(new XYChart.Data<>(days[i], moods[i]));
        }
        moodChart.getData().add(moodSeries);
        moodChart.setPrefHeight(250);

        // --- Tips Card ---
        VBox tipsCard = new VBox(10);
        tipsCard.setPadding(new Insets(20));
        tipsCard.setAlignment(Pos.CENTER_LEFT);
        tipsCard.setStyle("-fx-background-color: white; -fx-background-radius: 20;");
        tipsCard.setEffect(new DropShadow(5, Color.rgb(200, 200, 200, 0.3)));

        Label tip1 = new Label("💡 Reflect on patterns in your mood");
        Label tip2 = new Label("🌈 Notice how weather affects your day");
        Label tip3 = new Label("✨ Plan for a happier week ahead");

        tip1.setFont(Font.font(14));
        tip2.setFont(Font.font(14));
        tip3.setFont(Font.font(14));

        tipsCard.getChildren().addAll(tip1, tip2, tip3);

        // --- Assemble Root ---
        root.getChildren().addAll(title, subtitle, weatherScroll, moodChart, tipsCard);

        Scene scene = new Scene(root, 800, 650);
        stage.setTitle("Smart Journal - Weekly Summary");
        stage.setScene(scene);
        stage.show();
    }

    // --- Helper Methods ---
    private int getCurrentDayIndex() {
        DayOfWeek day = LocalDate.now().getDayOfWeek();
        return switch (day) {
            case MONDAY -> 0;
            case TUESDAY -> 1;
            case WEDNESDAY -> 2;
            case THURSDAY -> 3;
            case FRIDAY -> 4;
            case SATURDAY -> 5;
            case SUNDAY -> 6;
        };
    }

    private String getWeatherText(String icon) {
        return switch (icon) {
            case "☀" -> "Sunny";
            case "☁" -> "Cloudy";
            case "🌧" -> "Rainy";
            case "🌤" -> "Partly Cloudy";
            case "⛅" -> "Mostly Cloudy";
            default -> "";
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}
